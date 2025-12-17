package com.cm.service;

import java.util.Map;

/**
 * @author 31373
 */
public interface VerifyCodeService {
    Map<String, String> generateVerifyCode();

    String generateVerifyCodeByUuid(String uuid);

    boolean checkVerifyCode(String uuid, String code);
}
