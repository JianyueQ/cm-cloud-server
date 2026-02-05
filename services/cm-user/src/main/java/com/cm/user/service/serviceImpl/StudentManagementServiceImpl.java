package com.cm.user.service.serviceImpl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.cm.common.aop.annotations.RedisCache;
import com.cm.common.aop.annotations.RedisCacheEvict;
import com.cm.common.core.constant.*;
import com.cm.common.core.exception.ParametersQuestionException;
import com.cm.common.core.result.PageResult;
import com.cm.common.utils.ExcelExportUtil;
import com.cm.dto.*;
import com.cm.entity.Student;
import com.cm.entity.User;
import com.cm.user.domain.StudentExcelVO;
import com.cm.user.mapper.StudentManagementMapper;
import com.cm.user.service.StudentManagementService;
import com.cm.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 31373
 */
@Service
public class StudentManagementServiceImpl implements StudentManagementService {

    @Autowired
    private StudentManagementMapper studentManagementMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取学院专业班级信息
     *
     * @param collegeMajorClassPageDTO 学院专业班级信息
     * @return PageResult
     */
    @RedisCache(keyPrefix = "collegeMajorClass:",
            keyParts = {
                    "#collegeMajorClassPageDTO.collegeName", "#collegeMajorClassPageDTO.collegeCode",
                    "#collegeMajorClassPageDTO.majorName", "#collegeMajorClassPageDTO.majorCode",
                    "#collegeMajorClassPageDTO.className", "#collegeMajorClassPageDTO.classCode"},
            expireTime = 1, timeUnit = TimeUnit.HOURS)
    @Override
    public PageResult getCollegeMajorClass(CollegeMajorClassPageDTO collegeMajorClassPageDTO) {
        // 开始分页
        PageHelper.startPage(collegeMajorClassPageDTO.getPageNum(), collegeMajorClassPageDTO.getPageSize());

        // 查询学院列表（根据条件过滤）
        Page<CollegeMajorClassVO> collegePage = studentManagementMapper.getCollegeMajorClass(collegeMajorClassPageDTO);

        // 为每个学院获取专业，为每个专业获取班级
        List<CollegeMajorClassVO> result = collegePage.getResult().stream().map(college -> {
            // 获取该学院下的所有专业（根据查询条件过滤）
            List<MajorVO> majors = studentManagementMapper.getMajorsByCollegeId(
                    college.getId(),
                    collegeMajorClassPageDTO.getMajorName(),
                    collegeMajorClassPageDTO.getMajorCode(),
                    collegeMajorClassPageDTO.getClassName(),
                    collegeMajorClassPageDTO.getClassCode());

            // 为每个专业获取班级信息
            List<MajorVO> majorsWithClasses = majors.stream().map(major -> {
                List<ClassVO> classes = studentManagementMapper.getClassesByMajorId(
                        major.getId(),
                        collegeMajorClassPageDTO.getClassName(),
                        collegeMajorClassPageDTO.getClassCode());
                major.setClassVO(classes);
                return major;
            }).collect(Collectors.toList());

            college.setMajorVO(majorsWithClasses);
            return college;
        }).collect(Collectors.toList());

        return PageResult.builder()
                .total(collegePage.getTotal())
                .records(result)
                .build();
    }

    /**
     * 添加学生账号
     *
     * @param studentDTO 添加信息
     */
    @RedisCacheEvict({@RedisCacheEvict.CacheKeyConfig(keyPrefix = "studentAccount:list:", isPattern = true)})
    @Override
    @Transactional
    public void addStudentAccount(StudentDTO studentDTO) {
        //查看是否已存在该用户
        User username = studentManagementMapper.selectByUsername(studentDTO.getUsername());
        if (username != null) {
            throw new ParametersQuestionException(ParametersQuestionConstant.USERNAME_EXIST);
        }
        User user = new User();
        BeanUtils.copyProperties(studentDTO, user);
        user.setUserType(TypeConstant.USER_TYPE_STUDENT);
        user.setStatus(StatusConstant.STATUS_NORMAL);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        studentManagementMapper.insertUser(user);

        Student student = new Student();
        BeanUtils.copyProperties(studentDTO, student);
        //获取根据班级id获取年级
        student.setGrade(studentManagementMapper.getGradeByClassId(student.getClassId()).getGrade());
        student.setUserId(user.getId());
        studentManagementMapper.insertStudent(student);
    }

    /**
     * 获取学生账号信息-分页查询
     *
     * @param userWithStudentInfoPageDTO 查询条件
     * @return PageResult
     */
    @RedisCache(keyPrefix = "studentAccount:list:",keyParts = {
            "#userWithStudentInfoPageDTO.username", "#userWithStudentInfoPageDTO.realName",
            "#userWithStudentInfoPageDTO.status", "#userWithStudentInfoPageDTO.studentNo",
            "#userWithStudentInfoPageDTO.pageNum", "#userWithStudentInfoPageDTO.pageSize"
    },expireTime = 1, timeUnit = TimeUnit.HOURS)
    @Override
    public PageResult listStudentAccount(UserWithStudentInfoPageDTO userWithStudentInfoPageDTO) {
        PageHelper.startPage(userWithStudentInfoPageDTO.getPageNum(), userWithStudentInfoPageDTO.getPageSize());
        Page<UserWithStudentInfoPageVO> userWithStudentInfoPage = studentManagementMapper.listStudentAccount(userWithStudentInfoPageDTO);
        return PageResult.builder()
                .total(userWithStudentInfoPage.getTotal())
                .records(userWithStudentInfoPage.getResult())
                .build();
    }

    /**
     * 下载导入的模板
     *
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<byte[]> downloadTemplate() {
        // 创建Excel写入器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 创建表头和示例行
        List<Map<String, Object>> rows = new ArrayList<>();
        Map<String, Object> headerMap = getStringObjectMap();
        rows.add(headerMap);
        // 写入数据
        writer.write(rows, true);

        //设置列宽自适应
        for (int i = 0; i < headerMap.size(); i++) {
            writer.setColumnWidth(i, 50);
        }
        // 输出到字节数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writer.flush(outputStream);
        writer.close();

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "student_template.xlsx");
        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

    /**
     * 批量导入学生账号
     *
     * @param file 文件
     * @return 错误信息
     */
    @RedisCacheEvict({@RedisCacheEvict.CacheKeyConfig(keyPrefix = "studentAccount:list:", isPattern = true)})
    @Override
    @Transactional
    public String batchImportStudent(MultipartFile file) {
        try {
            //读取excel文件
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            //读取所有行数据,跳过示例数据
            List<List<Object>> readAll = reader.read(2);
            //如果没有数据,返回错误
            if (readAll.isEmpty()) {
                reader.close();
                throw new ParametersQuestionException(ParametersQuestionConstant.EXCEL_EMPTY);
            }
            //计数器
            int successCount = 0;
            int failCount = 0;
            //创建一个StringBuilder对象，用于存储错误信息
            StringBuilder errorMessage = new StringBuilder();
            //遍历每一行数据
            for (int i = 0; i < readAll.size(); i++) {
                List<Object> rowData = readAll.get(i);
                //确保数据有足够列
                if (rowData.size() < 13) {
                    failCount++;
                    errorMessage.append("第").append(i + 2).append("行数据列数不足");
                    continue;
                }

                try {
                    User user = new User();
                    Student student = new Student();

                    // 解析数据并填充到User实体类中
                    user.setUsername(getStringValue(rowData.get(0)));
                    //加密密码
                    user.setPassword(DigestUtils.md5DigestAsHex(getStringValue(rowData.get(1)).getBytes()));
                    user.setRealName(getStringValue(rowData.get(2)));
                    user.setPhone(getStringValue(rowData.get(3)));
                    user.setEmail(getStringValue(rowData.get(4)));
                    user.setGender(parseGender(getStringValue(rowData.get(5))));
                    // 设置为学生类型
                    user.setUserType(TypeConstant.USER_TYPE_STUDENT);
                    // 默认启用状态
                    user.setStatus(StatusConstant.STATUS_NORMAL);

                    // 解析数据并填充到Student实体类中
                    student.setStudentNo(getStringValue(rowData.get(6)));
                    Long collegeId = getCollegeIdByName(getStringValue(rowData.get(7)));
                    student.setCollegeId(collegeId);
                    Long majorId = getMajorIdByName(collegeId, getStringValue(rowData.get(8)));
                    student.setMajorId(majorId);
                    student.setClassId(getClassIdByName(majorId, getStringValue(rowData.get(9))));
                    student.setEnrollmentYear(parseInteger(getStringValue(rowData.get(10))));
                    student.setEducationLevel(parseInteger(getStringValue(rowData.get(11))));
                    student.setStatus(parseInteger(getStringValue(rowData.get(12))));

                    // 检查用户名是否已存在
                    User existingUser = studentManagementMapper.selectByUsername(user.getUsername());
                    if (existingUser != null) {
                        failCount++;
                        errorMessage.append("第").append(i + 3).append("行用户名已存在; ");
                        continue;
                    }
                    // 插入用户信息
                    studentManagementMapper.insertUser(user);
                    // 设置学生表的用户ID
                    student.setUserId(user.getId());
                    // 获取年级信息
                    Student gradeInfo = studentManagementMapper.getGradeByClassId(student.getClassId());
                    if (gradeInfo != null) {
                        student.setGrade(gradeInfo.getGrade());
                    }
                    // 插入学生信息
                    studentManagementMapper.insertStudent(student);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    errorMessage.append("第").append(i + 3).append("行数据错误: ").append(e.getMessage()).append("; ");
                }
            }
            reader.close();
            // 返回结果
            String resultMsg = "导入完成，成功:" + successCount + "条，失败:" + failCount + "条。";
            if (!errorMessage.isEmpty()) {
                resultMsg += " 错误信息：" + errorMessage.toString();
                throw new ParametersQuestionException(resultMsg);
            }
            return resultMsg;
        } catch (Exception e) {
            throw new ParametersQuestionException(ParametersQuestionConstant.EXCEL_ERROR + e.getMessage());
        }
    }

    /**
     * 获取学生账号详情
     *
     * @param id 学生id
     * @return 学生账号详情
     */
    @RedisCache(keyPrefix = "studentAccount:detail:",keyParts = "#id",expireTime = 1, timeUnit = TimeUnit.HOURS)
    @Override
    public UserWithStudentInfoVO getStudentAccountDetail(String id) {
        return studentManagementMapper.getStudentAccountDetail(id);
    }

    /**
     * 修改学生账号信息
     *
     * @param studentDTO 修改信息
     */
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "studentAccount:list:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "studentAccount:detail:", keyParts = "#studentDTO.id")
    })
    @Override
    public void updateStudentAccount(StudentDTO studentDTO) {
        User user = new User();
        user.setId(studentDTO.getId());
        user.setUsername(studentDTO.getUsername());
        user.setRealName(studentDTO.getRealName());
        user.setPhone(studentDTO.getPhone());
        user.setEmail(studentDTO.getEmail());
        user.setGender(studentDTO.getGender());
        studentManagementMapper.updateUser(user);
        Student student = new Student();
        BeanUtils.copyProperties(studentDTO, student);
        student.setUserId(user.getId());
        //获取根据班级id获取年级
        student.setGrade(studentManagementMapper.getGradeByClassId(student.getClassId()).getGrade());
        studentManagementMapper.updateStudent(student);
    }

    /**
     * 删除学生账号
     *
     * @param ids 删除id
     */
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "studentAccount:list:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "studentAccount:detail:", keyParts = "#ids")
    })
    @Override
    @Transactional
    public void deleteStudentAccount(List<Long> ids) {
        studentManagementMapper.deleteUser(ids);
        studentManagementMapper.deleteStudent(ids);
    }

    /**
     * 重置学生密码
     *
     * @param id 学生id
     * @return 重置密码
     */
    @Override
    public String resetStudentPassword(String id) {
        //根据id查询用户
        User user = studentManagementMapper.selectById(id);
        if (user == null) {
            throw new ParametersQuestionException(ParametersQuestionConstant.USER_NOT_EXIST);
        }
        //生成随机密码,密码只能是英文+数字
        String password = RandomStringUtils.randomAlphanumeric(11);
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        studentManagementMapper.updateUser(user);
        return password;
    }

    /**
     * 修改学生账号状态
     *
     * @param studentAccountStatusDTO 修改信息
     */
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "studentAccount:list:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "studentAccount:detail:", keyParts = "#studentAccountStatusDTO.id")
    })
    @Override
    public void updateStudentAccountStatus(StudentAccountStatusDTO studentAccountStatusDTO) {
        User user = new User();
        BeanUtils.copyProperties(studentAccountStatusDTO, user);
        studentManagementMapper.updateUser(user);
        //删除redis中存储的用户信息
        Map<Object, Object> existingUserSession = redisTemplate.opsForHash().entries(RedisConstant.JWT_ID_KEY + studentAccountStatusDTO.getId());
        if (!existingUserSession.isEmpty()) {
            Object existingToken = existingUserSession.get(MapConstant.USER_TOKEN);
            if (existingToken != null) {
                // 删除旧的token
                redisTemplate.delete(RedisConstant.JWT_TOKEN_KEY + existingToken);
            }
        }
    }

    /**
     * 导出学生账号
     * @param userWithStudentInfoPageDTO 获取信息
     * @return 导出文件
     */
    @Override
    public ResponseEntity<byte[]> exportStudentAccount(UserWithStudentInfoPageDTO userWithStudentInfoPageDTO) {
        try {
            PageHelper.startPage(userWithStudentInfoPageDTO.getPageNum(), userWithStudentInfoPageDTO.getPageSize());
            Page<StudentExcelVO> page = studentManagementMapper.exportStudentAccount(userWithStudentInfoPageDTO);
            List<StudentExcelVO> excelDataList = new ArrayList<>();
            for (StudentExcelVO vo : page.getResult()) {
                StudentExcelVO excelVO = new StudentExcelVO();
                BeanUtils.copyProperties(vo, excelVO);
                // 处理性别显示
                if (vo.getGender() != null) {
                    switch (parseInteger(vo.getGender())) {
                        case 1:
                            excelVO.setGender("男");
                            break;
                        case 2:
                            excelVO.setGender("女");
                            break;
                        default:
                            excelVO.setGender("未知");
                            break;
                    }
                }
                // 处理学历层次显示
                if (vo.getEducationLevel() != null) {
                    switch (parseInteger(vo.getEducationLevel())) {
                        case 1:
                            excelVO.setEducationLevel("专科");
                            break;
                        case 2:
                            excelVO.setEducationLevel("本科");
                            break;
                        case 3:
                            excelVO.setEducationLevel("研究生");
                            break;
                        default:
                            excelVO.setEducationLevel("");
                            break;
                    }
                }
                // 处理学籍状态显示
                if (vo.getStatus() != null) {
                    switch (parseInteger(vo.getStatus())) {
                        case 1:
                            excelVO.setStatus("在读");
                            break;
                        case 2:
                            excelVO.setStatus("休学");
                            break;
                        case 3:
                            excelVO.setStatus("退学");
                            break;
                        case 4:
                            excelVO.setStatus("毕业");
                            break;
                        default:
                            excelVO.setStatus("");
                            break;
                    }
                }
                excelDataList.add(excelVO);
            }
            // 导出Excel
            byte[] excelBytes = ExcelExportUtil.exportExcel(excelDataList, StudentExcelVO.class);

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "student_data.xlsx");
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new ParametersQuestionException("导出失败: " + e.getMessage());
        }
    }



    /**
     * 获取学生列表
     *
     * @param listStudentPageDTO 分页数据
     * @return 分页数据
     */
    @Override
    public PageResult listStudent(ListStudentPageDTO listStudentPageDTO) {
        PageHelper.startPage(listStudentPageDTO.getPageNum(), listStudentPageDTO.getPageSize());
        Page<StudentListVO> page = studentManagementMapper.listStudent(listStudentPageDTO);
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    /**
     * 获取字符串值
     */
    private String getStringValue(Object obj) {
        return obj != null ? obj.toString().trim() : null;
    }

    /**
     * 解析性别
     */
    private Integer parseGender(String genderStr) {
        if (genderStr == null || genderStr.isEmpty()) {
            return 0;
        }
        return switch (genderStr) {
            case "男" -> 1;
            case "女" -> 2;
            default -> 0;
        };
    }

    /**
     * 解析整数值
     */
    private Integer parseInteger(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    /**
     * 根据学院名称获取学院ID
     */
    private Long getCollegeIdByName(String collegeName) {
        if (collegeName == null || collegeName.isEmpty()) {
            return null;
        }
        return studentManagementMapper.getCollegeIdByName(collegeName);
    }

    /**
     * 根据专业名称和学院id获取专业ID
     */
    private Long getMajorIdByName(Long collegeId, String majorName) {
        if (majorName.isEmpty() || collegeId == null) {
            return null;
        }
        return studentManagementMapper.getMajorIdByNameWithCollegeId(collegeId, majorName);
    }

    /**
     * 根据班级名称和专业id获取班级ID
     */
    private Long getClassIdByName(Long majorId, String className) {
        if (className.isEmpty() || majorId == null) {
            return null;
        }
        return studentManagementMapper.getClassIdByNameWithMajorId(majorId, className);
    }

    /**
     * 获取导入模板的表头
     *
     * @return Map
     */
    private Map<String, Object> getStringObjectMap() {
        Map<String, Object> headerMap = new LinkedHashMap<>();
        headerMap.put("用户名(不能重复)", "例如：zhangsan");
        headerMap.put("密码", "例如：123456");
        headerMap.put("真实姓名", "例如：张三");
        headerMap.put("手机号", "例如：13800138000");
        headerMap.put("邮箱", "例如：zhangsan@example.com");
        headerMap.put("性别", "例如：男");
        headerMap.put("学号", "例如：20220216001");
        headerMap.put("学院名称", "例如：信息工程学院");
        headerMap.put("专业名称", "例如：计算机科学与技术");
        headerMap.put("班级名称", "例如：2022级1班");
        headerMap.put("入学年份", "例如：2022");
        headerMap.put("学历层次(1:专科,2:本科,3:研究生)", "例如：2");
        headerMap.put("学籍状态(1:在读,2:休学,3:退学,4:毕业)", "例如：1");
        return headerMap;
    }
}
