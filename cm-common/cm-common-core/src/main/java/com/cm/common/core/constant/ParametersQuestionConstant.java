package com.cm.common.core.constant;

/**
 * @author 31373
 */
public class ParametersQuestionConstant {

    /**
     * 权限,账户校验
     */
    public static final String NO_PERMISSION = "无权限操作";
    public static final String USERNAME_EXIST = "用户名已存在";
    public static final String USER_NOT_EXIST = "用户不存在";

    /**
     * 基本资料
     */
    public static final String EMAIL_NOT_NULL = "邮箱地址不能为空";
    public static final String EMAIL_ERROR = "邮件格式错误";
    public static final String PHONE_NOT_NULL = "手机号不能为空";
    public static final String PHONE_ERROR = "手机号格式不正确";
    public static final String GENDER_NOT_NULL = "性别不能为空";
    public static final String REAL_NAME_NOT_NULL = "真实姓名不能为空";
    public static final String REAL_NAME_NOT_RIGHT = "真实姓名不能包含数字或者字母以及其他标点符号";
    public static final String PARAMETERS_NOT_NULL = "参数不能为空";
    public static final String OLD_PASSWORD_NOT_NULL = "旧密码不为空";
    public static final String NEW_PASSWORD_NOT_NULL = "新密码不能为空";
    public static final String RE_PASSWORD_NOT_NULL = "确认密码不能为空";
    public static final String NEW_PASSWORD_SAME_AS_RE_PASSWORD = "新密码与确认密码不相同";
    public static final String NEW_PASSWORD_SAME_AS_OLD_PASSWORD = "新密码不能与旧密码相同";
    public static final String PASSWORD_ERROR = "旧密码错误";


    /**
     * 公告
     */
    public static final String TITLE_NOT_NULL = "公告标题不能为空";
    public static final String CONTENT_NOT_NULL = "公告内容不能为空";
    public static final String ANNOUNCEMENT_STATUS_NOT_NULL = "公告状态不能为空";
    public static final String ANNOUNCEMENT_TYPE_NOT_NULL = "公告类型不能为空";
    public static final String PRIORITY_NOT_NULL = "公告优先级不能为空";

    /**
     * 管理员用户信息
     */
    public static final String USERNAME_NOT_NULL = "用户名不能为空";
    public static final String USERNAME_NOT_RIGHT = "用户名只能包含字母、数字、下划线、中划线、点、@、_、-、.";
    public static final String PASSWORD_NOT_NULL = "密码不能为空";
    public static final String PASSWORD_NOT_RIGHT = "密码只能包含字母、数字、下划线、中划线、点、@、_、-、.";
    public static final String POSITION_NOT_NULL = "职位不能为空";

    /**
     * excel校验
     */
    public static final String EXCEL_EMPTY = "excel文件内没有有效数据";
    public static final String EXCEL_ERROR = "导入失败：";

    /**
     * 课程校验
     */
    public static final String SELECTION_START_TIME_NOT_NULL = "选课开始时间不能为空";
    public static final String SELECTION_END_TIME_NOT_NULL = "选课结束时间不能为空";
    public static final String TEACHER_NOT_EXIST = "请先选择教师";
    public static final String TEACHER_NOT_INCUMBENCY = "请检查所选择的教师是否为在职状态";
    public static final String CLASS_NOT_EXIST = "请选择班级";
    public static final String MAX_STUDENT_COUNT_LESS_THAN_CURRENT_STUDENT_COUNT = "最大学生数不能小于当前学生数";
    public static final String SELECT_COURSE_EXCEED_MAX_COUNT = "课程已达到最大选课人数限制";
    public static final String SELECT_COURSE_NOT_EXIST = "无法获取课程的选课信息";
    public static final String SELECT_COURSE_ALREADY_SELECTED = "课程已选";
    public static final String SELECT_COURSE_NOT_IN_TIME = "课程不在选课时间内";
    public static final String SELECTION_NOT_IN_TIME = "课程不在规定时间内,无法取消";
    public static final String ATTENDANCE_EXIST = "该课程已存在一个正在进行的签到";
    public static final String ATTENDANCE_ERROR = "课程发起签到异常";
    public static final String STUDENT_HAS_COURSE = "学生存在课程信息无法删除";

    /**
     * 签到校验
     */
    public static final String ATTENDANCE_EXPIRED = "签到已过期";
    public static final String ATTENDANCE_PASSWORD_ERROR = "签到密码错误";
    public static final String ATTENDANCE_ALREADY_SIGN_IN = "已签到";

    /**
     * 成绩校验
     */
    public static final String STUDENT_EXIST_COURSE_RECORD = "该学生已存在课程成绩";
    public static final String STUDENT_NOT_EXIST_COURSE_RECORD = "该学生不存在课程成绩,无法修改";

    public static final String ANNOUNCEMENT_NOT_EXIST = "公告不存在";
}
