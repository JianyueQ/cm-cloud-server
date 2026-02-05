package com.cm.message.controller.teacher;

import com.cm.common.core.context.BaseContext;
import com.cm.common.core.result.Result;
import com.cm.message.service.KnowledgeQaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * @author 31373
 */
@Slf4j
@RestController("teacherKnowledgeQaController")
@RequestMapping("/teacher/qa")
public class KnowledgeQaController {

    private final KnowledgeQaService knowledgeQaService;

    public KnowledgeQaController(KnowledgeQaService knowledgeQaService) {
        this.knowledgeQaService = knowledgeQaService;
    }

    @PostMapping("/ask")
    public Result<String> askQuestion(@RequestBody Map<String, String> request) {
        log.info("question: {}", request.get("question"));
        return Result.success(knowledgeQaService.answerQuestion(request));
    }

    @PostMapping(value = "/ask-stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter askQuestionStream(@RequestBody Map<String, String> request) {
        log.info("问题: {}", request.get("question"));
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        Long currentId = BaseContext.getCurrentId();
        CompletableFuture.runAsync(() -> {
            try {
                Stream<String> stringStream = knowledgeQaService.answerQuestionStream(request, currentId);
                stringStream.forEach(chunk -> {
                    try {
                        emitter.send(chunk);
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                });
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

}
