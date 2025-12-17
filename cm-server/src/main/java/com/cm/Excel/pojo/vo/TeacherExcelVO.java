package com.cm.Excel.pojo.vo;

import com.cm.annotations.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherExcelVO implements Serializable {

    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String username;

    /**
     * 真实姓名
     */
    @Excel(name = "真实姓名")
    private String realName;

    /**
     * 手机号
     */
    @Excel(name = "手机号")
    private String phone;

    /**
     * 邮箱
     */
    @Excel(name = "邮箱")
    private String email;

    /**
     * 性别（1:男, 2:女, 0:未知）
     */
    @Excel(name = "性别")
    private String gender;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 工号
     */
    @Excel(name = "工号")
    private String teacherNo;

    /**
     * 职称
     */
    @Excel(name = "职称")
    private String title;

    /**
     * 入职日期
     */
    @Excel(name = "入职日期")
    private LocalDate hireDate;

    /**
     * 在职状态（1:在职, 2:离职, 3:退休）
     */
    @Excel(name = "在职状态")
    private String status;

    /**
     * 学院名称
     */
    @Excel(name = "学院")
    private String collegeName;

    /**
     * 专业名称
     */
    @Excel(name = "专业")
    private String majorName;

}
