package com.cm.user.controller.teacher;

import com.cm.common.core.result.PageResult;
import com.cm.common.core.result.Result;
import com.cm.dto.ListStudentPageDTO;
import com.cm.user.service.StudentManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 31373
 */
@Slf4j
@RestController("teacherStudentManagementController")
@RequestMapping("/teacher/student")
public class StudentManagementController {

    private final StudentManagementService studentManagementService;

    public StudentManagementController(StudentManagementService studentManagementService) {
        this.studentManagementService = studentManagementService;
    }

    /**
     * 获取学生列表-分页查询
     */
    @GetMapping("/list")
    public Result<PageResult> listStudent(ListStudentPageDTO listStudentPageDTO) {
        log.info("获取学生列表:{}", listStudentPageDTO);
        return Result.success(studentManagementService.listStudent(listStudentPageDTO));
    }
}
