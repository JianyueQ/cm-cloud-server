package com.cm.mapper;

import com.cm.Excel.pojo.vo.TeacherExcelVO;
import com.cm.annotations.AutoFile;
import com.cm.dto.CollegeMajorClassPageDTO;
import com.cm.dto.UserWithTeacherInfoPageDTO;
import com.cm.entity.Teacher;
import com.cm.entity.User;
import com.cm.enumeration.OperationType;
import com.cm.vo.CollegeMajorClassVO;
import com.cm.vo.MajorVO;
import com.cm.vo.UserWithTeacherInfoPageVO;
import com.cm.vo.UserWithTeacherInfoVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface TeacherManagementMapper {
    /**
     * 获取学院专业信息
     * @param collegeMajorClassPageDTO 筛选条件
     * @return 筛选结果
     */
    Page<CollegeMajorClassVO> getCollegeMajor(CollegeMajorClassPageDTO collegeMajorClassPageDTO);

    /**
     * 根据学院ID获取专业列表
     *
     * @param collegeId 学院ID
     * @return 专业列表
     */
    List<MajorVO> getMajorsByCollegeId(@Param("collegeId") Long collegeId,
                                       @Param("majorName") String majorName,
                                       @Param("majorCode") String majorCode);

    /**
     * 根据username获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(String username);

    /**
     * 添加用户
     * @param user 用户信息
     */
    @AutoFile(OperationType.INSERT)
    void insertUser(User user);

    /**
     * 添加教师信息
     * @param teacher 教师信息
     */
    @AutoFile(OperationType.INSERT)
    void insertTeacher(Teacher teacher);

    /**
     * 获取教师信息-分页查询
     * @param userWithTeacherInfoPageDTO 查询条件
     * @return 教师信息
     */
    Page<UserWithTeacherInfoPageVO> listTeacherAccount(UserWithTeacherInfoPageDTO userWithTeacherInfoPageDTO);

    /**
     * 根据ID获取学院ID
     * @param collegeName 学院名称
     * @return 学院ID
     */
    Long getCollegeIdByName(String collegeName);

    /**
     * 根据学院ID和专业名称获取专业ID
     * @param collegeId 学院ID
     * @param majorName 专业名称
     * @return 专业ID
     */
    Long getMajorIdByNameWithCollegeId(Long collegeId, String majorName);

    /**
     * 根据ID获取教师信息
     * @param id 教师ID
     * @return 教师信息
     */
    UserWithTeacherInfoVO getTeacherAccountDetail(String id);

    /**
     * 修改用户信息
     * @param user 用户信息
     */
    @AutoFile(OperationType.UPDATE)
    void updateUser(User user);

    /**
     * 修改教师信息
     * @param teacher 教师信息
     */
    @AutoFile(OperationType.UPDATE)
    void updateTeacher(Teacher teacher);

    /**
     * 删除用户
     * @param ids 用户ID
     */
    void deleteUser(List<Long> ids);

    /**
     * 删除教师信息
     * @param ids 教师ID
     */
    void deleteTeacher(List<Long> ids);

    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    User selectById(String id);

    /**
     * 导出教师信息
     * @param userWithStudentInfoPageDTO 查询条件
     * @return 教师信息
     */
    Page<TeacherExcelVO> exportStudentAccount(UserWithTeacherInfoPageDTO userWithStudentInfoPageDTO);
}
