package com.cm.message.service.serviceImpl;

import com.cm.common.aop.annotations.RedisCache;
import com.cm.common.aop.annotations.RedisCacheEvict;
import com.cm.common.core.constant.MapConstant;
import com.cm.common.core.constant.ParametersQuestionConstant;
import com.cm.common.core.constant.RedisConstant;
import com.cm.common.core.context.BaseContext;
import com.cm.common.core.exception.ParametersQuestionException;
import com.cm.common.core.result.PageResult;
import com.cm.dto.AnnouncementDTO;
import com.cm.dto.AnnouncementPageDTO;
import com.cm.entity.Announcement;
import com.cm.message.mapper.AnnouncementMapper;
import com.cm.message.service.AnnouncementService;
import com.cm.vo.AnnouncementDetailsVO;
import com.cm.vo.AnnouncementPageVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 31373
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementMapper announcementMapper;
    private final RedisTemplate<String, String> redisTemplate;

    public AnnouncementServiceImpl(AnnouncementMapper announcementMapper, RedisTemplate<String, String> redisTemplate) {
        this.announcementMapper = announcementMapper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 添加
     *
     * @param announcementDTO 添加的数据
     */
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "announcement:list:", isPattern = true)
    })
    @Override
    public void addAnnouncement(AnnouncementDTO announcementDTO) {
        //校验
        checkParam(announcementDTO);
        Long currentId = BaseContext.getCurrentId();
        Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisConstant.JWT_ID_KEY + currentId);
        String realName = map.get(MapConstant.REAL_NAME).toString();
        Announcement announcement = new Announcement();
        BeanUtils.copyProperties(announcementDTO, announcement);
        announcement.setPublisherId(currentId);
        announcement.setPublisherName(realName);
        announcement.setPublishTime(LocalDateTime.now());
        announcementMapper.addAnnouncement(announcement);
    }

    /**
     * 修改
     *
     * @param announcementDTO 修改的数据
     */
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "announcement:details:", keyParts = "#announcementDTO.id"),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "announcement:list:", isPattern = true)
    })
    @Override
    public void updateAnnouncement(AnnouncementDTO announcementDTO) {
        checkParam(announcementDTO);
        Announcement announcement = new Announcement();
        BeanUtils.copyProperties(announcementDTO, announcement);
        announcementMapper.updateAnnouncement(announcement);
    }

    /**
     * 删除
     *
     * @param ids 删除的id
     */
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "announcement:details:", keyParts = "#ids"),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "announcement:list:", isPattern = true)
    })
    @Override
    public void deleteAnnouncement(List<Long> ids) {
        announcementMapper.deleteAnnouncement(ids);
    }

    /**
     * 详情
     *
     * @param id 详情的id
     * @return 详情数据
     */
    @RedisCache(keyPrefix = "announcement:details:",
            keyParts = {"#id"},
            expireTime = 1,
            timeUnit = TimeUnit.HOURS)
    @Override
    public AnnouncementDetailsVO getAnnouncementDetails(Long id) {
        Announcement announcement = announcementMapper.getAnnouncementDetails(id);
        if (announcement == null){
            throw new ParametersQuestionException(ParametersQuestionConstant.ANNOUNCEMENT_NOT_EXIST);
        }
//        Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisConstant.JWT_ID_KEY + announcement.getUpdateUser());
//        String realName = map.get(MapConstant.REAL_NAME).toString();
        AnnouncementDetailsVO announcementDetailsVO = new AnnouncementDetailsVO();
        BeanUtils.copyProperties(announcement, announcementDetailsVO);
//        announcementDetailsVO.setUpdateUserName(realName);

        return announcementDetailsVO;
    }

    /**
     * 列表
     *
     * @param announcementPageDTO 查询条件
     * @return 列表数据
     */
    @RedisCache(keyPrefix = "announcement:list:",
            keyParts = {"#announcementPageDTO.pageNum", "#announcementPageDTO.pageSize",
                    "#announcementPageDTO.title", "#announcementPageDTO.status",
                    "#announcementPageDTO.announcementType", "#announcementPageDTO.publisherName"},
            expireTime = 1,
            timeUnit = TimeUnit.HOURS)
    @Override
    public PageResult list(AnnouncementPageDTO announcementPageDTO) {
        PageHelper.startPage(announcementPageDTO.getPageNum(), announcementPageDTO.getPageSize());
        Page<AnnouncementPageVO> page = announcementMapper.page(announcementPageDTO);
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    /**
     * 获取浏览量
     *
     * @return 浏览量
     */
    @Override
    public Integer browseCount(Long id) {
        Announcement announcement = announcementMapper.getAnnouncementWithReadCount(id);
        String redisKey = RedisConstant.ANNOUNCEMENT_READ_COUNT;
        String field = String.valueOf(announcement.getId());
        //向redis添加浏览次数
        redisTemplate.opsForHash().increment(redisKey, field, RedisConstant.ANNOUNCEMENT_READ_COUNT_INCREASE);
        // 获取阅读计数（先从Redis获取，如果Redis中没有则从数据库获取）
        Integer readCountObj = Integer.valueOf(Objects.requireNonNull(redisTemplate.opsForHash().get(redisKey, field)).toString());
        Integer readCount;
        // 如果Redis中的计数小于数据库中的计数，则使用数据库中的计数
        if (announcement.getReadCount() > readCountObj) {
            readCount = announcement.getReadCount();
            //将数据库中的浏览数量赋给Redis
            redisTemplate.opsForHash().put(redisKey, field, String.valueOf(readCount));
        } else {
            //将Redis中的浏览数量返回
            readCount = readCountObj;
        }
        return readCount;
    }


    /**
     * 校验函数
     */
    private void checkParam(AnnouncementDTO announcementDTO) {
        if (announcementDTO.getTitle() == null) {
            // 标题不能为空
            throw new ParametersQuestionException(ParametersQuestionConstant.TITLE_NOT_NULL);
        }
        if (announcementDTO.getContent() == null) {
            // 内容不能为空
            throw new ParametersQuestionException(ParametersQuestionConstant.CONTENT_NOT_NULL);
        }
        if (announcementDTO.getStatus() == null) {
            // 公告状态不能为空
            throw new ParametersQuestionException(ParametersQuestionConstant.ANNOUNCEMENT_STATUS_NOT_NULL);
        }
        if (announcementDTO.getAnnouncementType() == null) {
            // 公告类型不能为空
            throw new ParametersQuestionException(ParametersQuestionConstant.ANNOUNCEMENT_TYPE_NOT_NULL);
        }
        if (announcementDTO.getPriority() == null) {
            // 优先级不能为空
            throw new ParametersQuestionException(ParametersQuestionConstant.PRIORITY_NOT_NULL);
        }
    }
}
