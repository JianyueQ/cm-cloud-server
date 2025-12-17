package com.cm.constant;

/**
 * 状态常量，启用或者禁用
 * @author 31373
 */
public class StatusConstant {
    /**
     * 启用
     */
    public static final Integer STATUS_NORMAL = 1;
    /**
     * 禁用
     */
    public static final Integer STATUS_DISABLE = 0;

    /**
     * 在职
     */
    public static final Integer STATUS_INCUMBENCY = 1;

    /**
     * 已选
     */
    public static final Integer STATUS_ELECTED = 1;

    /**
     * 进行中
     */
    public static final Integer ATTENDANCE_STATUS_WAITING = 1;

    /**
     * 已结束
     */
    public static final Integer ATTENDANCE_STATUS_END = 2;

    /**
     * 未签到
     */
    public static final Integer ATTENDANCE_STATUS_NOT_SIGN_IN = 3;

    /**
     * 已签到
     */
    public static final Integer ATTENDANCE_STATUS_SIGN_IN = 1;

    /**
     * 迟到
     */
    public static final Integer ATTENDANCE_STATUS_LATE = 2;

    /**
     * 请假
     */
    public static final Integer ATTENDANCE_STATUS_LEAVE = 4;
}
