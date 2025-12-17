package com.cm.utils;

/**
 * 参数校验工具类
 * @author 31373
 */
public class ParametersUtil {

    /**
     * 邮箱正则
     */
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    /**
     * 真实姓名正则
     */
    private static final String REAL_NAME_REGEX = "^[\\u4e00-\\u9fa5]+$";

    public static boolean isEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    public static boolean isRealName(String realName) {
        return realName != null && realName.matches(REAL_NAME_REGEX);
    }

    public static boolean isUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9_]+$");
    }
}
