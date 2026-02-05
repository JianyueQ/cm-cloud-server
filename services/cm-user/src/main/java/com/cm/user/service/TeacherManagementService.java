package com.cm.user.service;

import com.cm.common.core.result.PageResult;
import com.cm.dto.CollegeMajorClassPageDTO;
import com.cm.dto.TeacherAccountStatusDTO;
import com.cm.dto.TeacherDTO;
import com.cm.dto.UserWithTeacherInfoPageDTO;
import com.cm.vo.UserWithTeacherInfoVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 31373
 */
public interface TeacherManagementService {

    /**
     * 获取学院、专业信息
     * @param collegeMajorClassPageDTO dto
     * @return PageResult
     */
    PageResult getCollegeMajor(CollegeMajorClassPageDTO collegeMajorClassPageDTO);

    /**
     * 添加教师账号
     * @param teacherDTO dto
     */
    void addTeacherAccount(TeacherDTO teacherDTO);

    /**
     * 获取教师账号信息
     * @param userWithTeacherInfoPageDTO dto
     * @return PageResult
     */
    PageResult listTeacherAccount(UserWithTeacherInfoPageDTO userWithTeacherInfoPageDTO);

    /**
     * 下载教师模板
     * @return ResponseEntity
     */
    ResponseEntity<byte[]> downloadTemplate();

    /**
     * 批量导入教师
     * @param file  文件
     * @return  String
     */
    String batchImportTeacher(MultipartFile file);

    /**
     * 获取教师账号详情
     * @param id  id
     * @return UserWithStudentInfoVO
     */
    UserWithTeacherInfoVO getTeacherAccountDetail(String id);

    /**
     * 修改教师账号
     * @param teacherDTO dto
     */
    void updateTeacherAccount(TeacherDTO teacherDTO);

    /**
     * 删除教师账号
     * @param ids id集合
     */
    void deleteTeacherAccount(List<Long> ids);

    /**
     * 重置教师密码
     * @param id  id
     * @return  String
     */
    String resetTeacherPassword(String id);

    /**
     * 修改教师账号状态
     * @param teacherAccountStatusDTO dto
     */
    void updateTeacherAccountStatus(TeacherAccountStatusDTO teacherAccountStatusDTO);

    /**
     * 导出教师账号
     * @param userWithStudentInfoPageDTO dto
     * @return ResponseEntity
     */
    ResponseEntity<byte[]> exportTeacherAccount(UserWithTeacherInfoPageDTO userWithStudentInfoPageDTO);

}
