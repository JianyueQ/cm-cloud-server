package com.cm.Excel.pojo.vo;

import com.cm.annotations.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 31373
 */
@Data
public class StudentExcelVO implements Serializable {

    @Excel(name = "学号", sort = 1)
    private String studentNo;

    @Excel(name = "姓名", sort = 2)
    private String realName;

    @Excel(name = "用户名", sort = 3)
    private String username;

    @Excel(name = "手机号", sort = 4)
    private String phone;

    @Excel(name = "邮箱", sort = 5)
    private String email;

    @Excel(name = "性别", sort = 6)
    private String gender;

    @Excel(name = "学院", sort = 7)
    private String collegeName;

    @Excel(name = "专业", sort = 8)
    private String majorName;

    @Excel(name = "班级", sort = 9)
    private String className;

    @Excel(name = "入学年份", sort = 10)
    private Integer enrollmentYear;

    @Excel(name = "年级", sort = 11)
    private String grade;

    @Excel(name = "学历层次", sort = 12)
    private String educationLevel;

    @Excel(name = "学籍状态", sort = 13)
    private String status;
}
