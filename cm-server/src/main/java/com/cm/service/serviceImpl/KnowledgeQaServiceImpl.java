package com.cm.service.serviceImpl;

import com.cm.constant.MapConstant;
import com.cm.constant.RedisConstant;
import com.cm.constant.TypeConstant;
import com.cm.context.BaseContext;
import com.cm.service.KnowledgeQaService;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author 31373
 */
@Service
public class KnowledgeQaServiceImpl implements KnowledgeQaService {

    private final DeepSeekChatModel deepSeekChatModel;
    private final RedisTemplate<String, String> redisTemplate;

    public KnowledgeQaServiceImpl(DeepSeekChatModel deepSeekChatModel, RedisTemplate<String, String> redisTemplate) {
        this.deepSeekChatModel = deepSeekChatModel;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public String answerQuestion(Map<String, String> request) {
        Integer userTypeWithInteger = Integer.valueOf(Objects.requireNonNull(redisTemplate.opsForHash().get(RedisConstant.JWT_ID_KEY + BaseContext.getCurrentId(), MapConstant.USER_TYPE)).toString());
        // 根据用户类型获取不同的回答风格
        String userType = userTypeWithInteger.equals(TypeConstant.USER_TYPE_ADMIN) ? "管理员" : userTypeWithInteger.equals(TypeConstant.USER_TYPE_TEACHER) ? "教师" : "学生";
        String question = request.get("question");
        // 构建提示词，根据用户类型定制回答风格
        String prompt = String.format(
                "你是一个校园助手，专门为%s解答问题。请用简洁明了的语言回答以下问题：%s",
                userType,
                question
        );
        // 调用DeepSeek模型获取回答
        String call = deepSeekChatModel.call(prompt);
        return call;
    }

    @Override
    public Stream<String> answerQuestionStream(Map<String, String> request, Long currentId) {

        Integer userTypeWithInteger = Integer.valueOf(Objects.requireNonNull(
                redisTemplate.opsForHash().get(
                        RedisConstant.JWT_ID_KEY + currentId,
                        MapConstant.USER_TYPE
                )
        ).toString());

        String userType = userTypeWithInteger.equals(TypeConstant.USER_TYPE_ADMIN) ? "管理员" :
                userTypeWithInteger.equals(TypeConstant.USER_TYPE_TEACHER) ? "教师" : "学生";

        String question = request.get("question");
        // 优化提示词，强调学习导向
        String prompt = String.format(
                "你是一个高校课程通的AI学习助手，专门为%s解答学习相关问题。请用简洁明了的语言回答以下问题：%s%n%n" +
                        "注意事项：%n" +
                        "1. 如果问题是学习相关的（如课程疑问、作业难题、学习方法等），请认真回答%n" +
                        "2. 如果问题与学习无关，请礼貌地提醒用户询问学习相关的问题%n" +
                        "3. 回答应专注校园学习场景，避免涉及敏感话题" +
                        "4. 使用标准Markdown格式进行回答，包括但不限于：使用#表示标题，使用*或-表示无序列表，使用数字加点表示有序列表，使用`表示代码块，使用**或__表示粗体，使用*或_表示斜体等 %n" +
                        "5. 每个数据块应该尽可能完整，避免将单个Markdown元素分割成多个数据块发送 %n",
                userType,
                question
        );

        // 使用流式调用
        return deepSeekChatModel.stream(prompt).toStream().map(chunk ->{
            return chunk;
        });
    }
}
