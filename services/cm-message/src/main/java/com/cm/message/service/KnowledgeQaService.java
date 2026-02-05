package com.cm.message.service;

import java.util.Map;
import java.util.stream.Stream;

/**
 * @author 31373
 */
public interface KnowledgeQaService {
    String answerQuestion(Map<String, String> request);

    Stream<String> answerQuestionStream(Map<String, String> request, Long currentId);
}
