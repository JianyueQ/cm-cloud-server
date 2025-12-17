package com.cm.controller.admin;

import com.cm.annotations.Log;
import com.cm.dto.CollegeMajorClassPageDTO;
import com.cm.dto.StudentAccountStatusDTO;
import com.cm.dto.StudentDTO;
import com.cm.dto.UserWithStudentInfoPageDTO;
import com.cm.enumeration.BusinessType;
import com.cm.result.PageResult;
import com.cm.result.Result;
import com.cm.service.StudentManagementService;
import com.cm.vo.UserWithStudentInfoVO;
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
@RestController("adminStudentManagementController")
@RequestMapping("/admin/student")
public class StudentManagementController {

    @Autowired
    private StudentManagementService studentManagementService;

    /**
     * 获取学院-专业-班级信息
     */
    @GetMapping("/getCollegeMajorClass")
    public Result<PageResult> getCollegeMajorClass(CollegeMajorClassPageDTO collegeMajorClassPageDTO){
        log.info("获取学院-专业-班级信息:{}", collegeMajorClassPageDTO);
        return Result.success(studentManagementService.getCollegeMajorClass(collegeMajorClassPageDTO));
    }

    /**
     * 添加学生账号信息
     */
    @Log(title = "学生账号信息-添加", businessType = BusinessType.INSERT)
    @PostMapping("/addStudentAccount")
    public Result<String> addStudentAccount(@RequestBody StudentDTO studentDTO){
        log.info("添加学生账号信息:{}",studentDTO);
        studentManagementService.addStudentAccount(studentDTO);
        return Result.success();
    }

    /**
     * 获取学生账号信息-分页查询
     */
    @GetMapping("/listStudentAccount")
    public Result<PageResult> listStudentAccount(UserWithStudentInfoPageDTO userWithStudentInfoPageDTO){
        log.info("获取学生账号信息-分页查询:{}",userWithStudentInfoPageDTO);
        return Result.success(studentManagementService.listStudentAccount(userWithStudentInfoPageDTO));
    }

    /**
     * 下载学生账号信息模板
     */
    @GetMapping("/downloadStudentTemplate")
    public ResponseEntity<byte[]> downloadTemplate(){
        log.info("下载学生账号信息模板");
        return studentManagementService.downloadTemplate();
    }

    /**
     * 批量导入学生账号信息
     */
    @Log(title = "学生账号信息-批量导入", businessType = BusinessType.INSERT)
    @PostMapping("/batchImportStudent")
    public Result<String> batchImportStudent(@RequestParam MultipartFile file){
        log.info("批量导入学生账号信息:{}",file);
        return Result.success(studentManagementService.batchImportStudent(file));
    }

    /**
     * 查看学生账号信息的详情
     */
    @GetMapping("/getStudentAccountDetail/{id}")
    public Result<UserWithStudentInfoVO> getStudentAccountDetail(@PathVariable String id){
        log.info("查看学生账号信息的详情:{}",id);
        return Result.success(studentManagementService.getStudentAccountDetail(id));
    }

    /**
     * 修改学生账号信息
     */
    @Log(title = "学生账号信息-修改", businessType = BusinessType.UPDATE)
    @PutMapping("/updateStudentAccount")
    public Result<String> updateStudentAccount(@RequestBody StudentDTO studentDTO){
        log.info("修改学生账号信息:{}",studentDTO);
        studentManagementService.updateStudentAccount(studentDTO);
        return Result.success();
    }

    /**
     * 批量删除学生账号信息
     */
    @Log(title = "学生账号信息-删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteStudentAccount")
    public Result<String> deleteStudentAccount(@RequestParam List<Long> ids){
        log.info("批量删除学生账号信息:{}",ids);
        studentManagementService.deleteStudentAccount(ids);
        return Result.success();
    }

    /**
     * 重置学生账号密码
     */
    @Log(title = "学生账号信息-重置密码", businessType = BusinessType.UPDATE)
    @PutMapping("/resetStudentPassword/{id}")
    public Result<String> resetStudentPassword(@PathVariable String id){
        log.info("重置学生账号密码:{}",id);
        return Result.success(studentManagementService.resetStudentPassword(id));
    }

    /**
     * 修改学生账号状态
     */
    @Log(title = "学生账号信息-修改状态", businessType = BusinessType.UPDATE)
    @PutMapping("/updateStudentAccountStatus")
    public Result<String> updateStudentAccountStatus(@RequestBody StudentAccountStatusDTO studentAccountStatusDTO){
        log.info("修改学生账号状态:{}",studentAccountStatusDTO);
        studentManagementService.updateStudentAccountStatus(studentAccountStatusDTO);
        return Result.success();
    }

    /**
     * 导出学生信息
     */
    @GetMapping("/exportStudent")
    public ResponseEntity<byte[]> exportStudentAccount(UserWithStudentInfoPageDTO userWithStudentInfoPageDTO) {
        log.info("导出学生账号信息到Excel:{}", userWithStudentInfoPageDTO);
        return studentManagementService.exportStudentAccount(userWithStudentInfoPageDTO);
    }

}
