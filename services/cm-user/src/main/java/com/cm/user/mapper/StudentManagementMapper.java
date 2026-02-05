package com.cm.user.mapper;

import com.cm.common.aop.annotations.AutoFile;
import com.cm.common.enumeration.OperationType;
import com.cm.dto.CollegeMajorClassPageDTO;
import com.cm.dto.ListStudentPageDTO;
import com.cm.dto.UserWithStudentInfoPageDTO;
import com.cm.entity.Student;
import com.cm.entity.User;
import com.cm.user.domain.StudentExcelVO;
import com.cm.vo.*;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface StudentManagementMapper {
    /**
     * 获取学院专业班级信息-分页查询
     *
     * @param collegeMajorClassPageDTO 查询条件
     * @return 学院-专业-班级信息
     */
    Page<CollegeMajorClassVO> getCollegeMajorClass(CollegeMajorClassPageDTO collegeMajorClassPageDTO);

    /**
     * 根据学院ID获取专业列表
     *
     * @param collegeId 学院ID
     * @return 专业列表
     */
    List<MajorVO> getMajorsByCollegeId(@Param("collegeId") Long collegeId,
                                       @Param("majorName") String majorName,
                                       @Param("majorCode") String majorCode,
                                       @Param("className") String className,
                                       @Param("classCode") String classCode);

    /**
     * 根据专业ID获取班级列表
     *
     * @param majorId   专业ID
     * @param className 班级名称
     * @param classCode 班级编码
     * @return 班级列表
     */
    List<ClassVO> getClassesByMajorId(@Param("majorId") Long majorId,
                                      @Param("className") String className,
                                      @Param("classCode") String classCode);

    /**
     * 添加学生账户
     * @param user 添加信息
     */
    @AutoFile(OperationType.INSERT)
    void insertUser(User user);

    /**
     * 添加学生信息
     * @param classId 班级ID
     * @return 年级
     */
    Student getGradeByClassId(Long classId);

    /**
     * 添加学生信息
     * @param student 添加信息
     */
    @AutoFile(OperationType.INSERT)
    void insertStudent(Student student);

    /**
     * 获取学生信息-分页查询
     * @param userWithStudentInfoPageDTO 查询条件
     * @return 学生信息
     */
    Page<UserWithStudentInfoPageVO> listStudentAccount(UserWithStudentInfoPageDTO userWithStudentInfoPageDTO);

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(String username);

    /**
     * 根据学院名称查询学院ID
     * @param collegeName 学院名称
     * @return 学院ID
     */
    Long getCollegeIdByName(String collegeName);

    /**
     * 根据学院ID和专业名称查询专业ID
     * @param collegeId 学院ID
     * @param majorName 专业名称
     * @return 专业ID
     */
    Long getMajorIdByNameWithCollegeId(Long collegeId, String majorName);

    /**
     * 根据专业ID查询班级ID
     * @param majorId 专业ID
     * @param className 班级名称
     * @return 班级ID
     */
    Long getClassIdByNameWithMajorId(Long majorId, String className);

    /**
     * 获取学生信息详情
     * @param id 学生ID
     * @return 学生信息
     */
    UserWithStudentInfoVO getStudentAccountDetail(String id);

    /**
     * 修改学生信息
     * @param user 修改信息
     */
    @AutoFile(OperationType.UPDATE)
    void updateUser(User user);

    /**
     * 修改学生信息
     * @param student 修改信息
     */
    @AutoFile(OperationType.UPDATE)
    void updateStudent(Student student);

    /**
     * 删除学生信息
     * @param ids 学生ID
     */
    void deleteUser(List<Long> ids);

    /**
     * 删除学生信息
     * @param ids 学生ID
     */
    void deleteStudent(List<Long> ids);

    /**
     * 根据ID查询用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    User selectById(String id);

    /**
     * 导出学生信息
     * @param userWithStudentInfoPageDTO 查询条件
     * @return 学生信息
     */
    Page<StudentExcelVO> exportStudentAccount(UserWithStudentInfoPageDTO userWithStudentInfoPageDTO);

    /**
     * 获取学生列表
     *
     * @param listStudentPageDTO 查询条件
     * @return 学生列表
     */
    Page<StudentListVO> listStudent(ListStudentPageDTO listStudentPageDTO);
}
