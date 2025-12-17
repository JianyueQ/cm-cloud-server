package com.cm.service;

import com.cm.dto.*;
import com.cm.result.PageResult;
import com.cm.vo.UserWithStudentInfoVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 31373
 */
public interface StudentManagementService {
    /**
     * 获取学院专业班级信息-分页
     * @param collegeMajorClassPageDTO 学院专业班级信息
     * @return PageResult
     */
    PageResult getCollegeMajorClass(CollegeMajorClassPageDTO collegeMajorClassPageDTO);

    /**
     * 添加学生账号
     * @param studentDTO 添加信息
     */
    void addStudentAccount(StudentDTO studentDTO);

    /**
     * 获取学生账号信息-分页
     *
     * @param userWithStudentInfoPageDTO 获取信息
     * @return PageResult
     */
    PageResult listStudentAccount(UserWithStudentInfoPageDTO userWithStudentInfoPageDTO);

    /**
     * 下载导入模板
     * @return ResponseEntity
     */
    ResponseEntity<byte[]> downloadTemplate();

    /**
     * 批量导入学生信息
     *
     * @param file 文件
     * @return  String
     */
    String batchImportStudent(MultipartFile file);

    /**
     * 获取学生账号信息-详情
     * @param id 学生id
     * @return StudentDTO
     */
    UserWithStudentInfoVO getStudentAccountDetail(String id);

    /**
     * 修改学生账号信息
     * @param studentDTO 修改信息
     */
    void updateStudentAccount(StudentDTO studentDTO);

    /**
     * 删除学生账号信息
     * @param ids 删除id
     */
    void deleteStudentAccount(List<Long> ids);

    /**
     * 重置学生密码
     * @param id 学生id
     * @return  String
     */
    String resetStudentPassword(String id);

    /**
     * 修改学生账号状态
     * @param studentAccountStatusDTO 修改信息
     */
    void updateStudentAccountStatus(StudentAccountStatusDTO studentAccountStatusDTO);

    /**
     * 导出学生账号信息
     * @param userWithStudentInfoPageDTO 获取信息
     * @return ResponseEntity
     */
    ResponseEntity<byte[]> exportStudentAccount(UserWithStudentInfoPageDTO userWithStudentInfoPageDTO);

    /**
     * 获取学生列表-分页查询
     * @param listStudentPageDTO 分页数据
     * @return 学生列表
     */
    PageResult listStudent(ListStudentPageDTO listStudentPageDTO);
}
