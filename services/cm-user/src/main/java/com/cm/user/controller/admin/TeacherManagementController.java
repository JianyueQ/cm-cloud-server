package com.cm.user.controller.admin;

import com.cm.common.aop.annotations.Log;
import com.cm.common.core.result.PageResult;
import com.cm.common.core.result.Result;
import com.cm.common.enumeration.BusinessType;
import com.cm.dto.CollegeMajorClassPageDTO;
import com.cm.dto.TeacherAccountStatusDTO;
import com.cm.dto.TeacherDTO;
import com.cm.dto.UserWithTeacherInfoPageDTO;
import com.cm.user.service.TeacherManagementService;
import com.cm.vo.UserWithTeacherInfoVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 31373
 */
@Slf4j
@RestController
@RequestMapping("/admin/teacher")
public class TeacherManagementController {

    @Autowired
    private TeacherManagementService teacherManagementService;

    /**
     * 获取学院-专业信息
     */
    @GetMapping("/getCollegeMajor")
    public Result<PageResult> getCollegeMajor(CollegeMajorClassPageDTO collegeMajorClassPageDTO){
        log.info("获取学院-专业-班级信息:{}", collegeMajorClassPageDTO);
        return Result.success(teacherManagementService.getCollegeMajor(collegeMajorClassPageDTO));
    }

    /**
     * 添加教师账号信息
     */
    @Log(title = "教师账号信息-添加", businessType = BusinessType.INSERT)
    @PostMapping("/addTeacherAccount")
    public Result<String> addTeacherAccount(@Valid @RequestBody TeacherDTO teacherDTO) {
        log.info("添加教师账号信息:{}", teacherDTO);
        teacherManagementService.addTeacherAccount(teacherDTO);
        return Result.success();
    }

    /**
     * 获取教师账号信息-分页查询
     */
    @GetMapping("/listTeacherAccount")
    public Result<PageResult> listTeacherAccount(UserWithTeacherInfoPageDTO userWithTeacherInfoPageDTO) {
        log.info("获取教师账号信息-分页查询:{}", userWithTeacherInfoPageDTO);
        return Result.success(teacherManagementService.listTeacherAccount(userWithTeacherInfoPageDTO));
    }

    /**
     * 下载教师账号信息模板
     */
    @GetMapping("/downloadTeacherTemplate")
    public ResponseEntity<byte[]> downloadTemplate() {
        log.info("下载教师账号信息模板");
        return teacherManagementService.downloadTemplate();
    }

    /**
     * 批量导入教师账号信息
     */
    @Log(title = "教师账号信息-批量导入", businessType = BusinessType.INSERT)
    @PostMapping("/batchImportTeacher")
    public Result<String> batchImportTeacher(@RequestParam MultipartFile file) {
        log.info("批量导入教师账号信息:{}", file);
        return Result.success(teacherManagementService.batchImportTeacher(file));
    }

    /**
     * 查看教师账号信息的详情
     */
    @GetMapping("/getTeacherAccountDetail/{id}")
    public Result<UserWithTeacherInfoVO> getTeacherAccountDetail(@PathVariable String id) {
        log.info("查看教师账号信息的详情:{}", id);
        return Result.success(teacherManagementService.getTeacherAccountDetail(id));
    }

    /**
     * 修改教师账号信息
     */
    @Log(title = "教师账号信息-修改", businessType = BusinessType.UPDATE)
    @PutMapping("/updateTeacherAccount")
    public Result<String> updateTeacherAccount(@RequestBody TeacherDTO teacherDTO) {
        log.info("修改教师账号信息:{}", teacherDTO);
        teacherManagementService.updateTeacherAccount(teacherDTO);
        return Result.success();
    }

    /**
     * 批量删除教师账号信息
     */
    @Log(title = "教师账号信息-删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteTeacherAccount")
    public Result<String> deleteTeacherAccount(@RequestParam List<Long> ids) {
        log.info("批量删除教师账号信息:{}", ids);
        teacherManagementService.deleteTeacherAccount(ids);
        return Result.success();
    }

    /**
     * 重置教师账号密码
     */
    @Log(title = "教师账号信息-重置密码", businessType = BusinessType.UPDATE)
    @PutMapping("/resetTeacherPassword/{id}")
    public Result<String> resetTeacherPassword(@PathVariable String id) {
        log.info("重置教师账号密码:{}", id);
        return Result.success(teacherManagementService.resetTeacherPassword(id));
    }

    /**
     * 修改教师账号状态
     */
    @Log(title = "教师账号信息-修改状态", businessType = BusinessType.UPDATE)
    @PutMapping("/updateTeacherAccountStatus")
    public Result<String> updateTeacherAccountStatus(@RequestBody TeacherAccountStatusDTO teacherAccountStatusDTO) {
        log.info("修改教师账号状态:{}", teacherAccountStatusDTO);
        teacherManagementService.updateTeacherAccountStatus(teacherAccountStatusDTO);
        return Result.success();
    }

    /**
     * 导出教师信息
     */
    @GetMapping("/exportTeacher")
    public ResponseEntity<byte[]> exportTeacherAccount(UserWithTeacherInfoPageDTO userWithStudentInfoPageDTO) {
        log.info("导出教师账号信息到Excel:{}", userWithStudentInfoPageDTO);
        return teacherManagementService.exportTeacherAccount(userWithStudentInfoPageDTO);
    }
}
