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
import com.cm.dto.CollegeMajorClassPageDTO;
import com.cm.dto.TeacherAccountStatusDTO;
import com.cm.dto.TeacherDTO;
import com.cm.dto.UserWithTeacherInfoPageDTO;
import com.cm.entity.Teacher;
import com.cm.entity.User;
import com.cm.user.domain.TeacherExcelVO;
import com.cm.user.mapper.TeacherManagementMapper;
import com.cm.user.service.TeacherManagementService;
import com.cm.vo.CollegeMajorClassVO;
import com.cm.vo.MajorVO;
import com.cm.vo.UserWithTeacherInfoPageVO;
import com.cm.vo.UserWithTeacherInfoVO;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class TeacherManagementServiceImpl implements TeacherManagementService {

    private final TeacherManagementMapper teacherManagementMapper;
    private final RedisTemplate<String, String> redisTemplate;

    public TeacherManagementServiceImpl(TeacherManagementMapper teacherManagementMapper, RedisTemplate<String, String> redisTemplate) {
        this.teacherManagementMapper = teacherManagementMapper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取学院-专业信息
     *
     * @param collegeMajorClassPageDTO 筛选条件
     * @return 学院-专业信息
     */
    @RedisCache(keyPrefix = "collegeMajorClass:",
            keyParts = {
                    "#collegeMajorClassPageDTO.collegeName", "#collegeMajorClassPageDTO.collegeCode",
                    "#collegeMajorClassPageDTO.majorName", "#collegeMajorClassPageDTO.majorCode",
                    "#collegeMajorClassPageDTO.className", "#collegeMajorClassPageDTO.classCode"},
            expireTime = 1, timeUnit = TimeUnit.HOURS)
    @Override
    public PageResult getCollegeMajor(CollegeMajorClassPageDTO collegeMajorClassPageDTO) {
        // 开始分页
        PageHelper.startPage(collegeMajorClassPageDTO.getPageNum(), collegeMajorClassPageDTO.getPageSize());

        // 查询学院列表（根据条件过滤）
        Page<CollegeMajorClassVO> collegePage = teacherManagementMapper.getCollegeMajor(collegeMajorClassPageDTO);

        // 为每个学院获取专业
        List<CollegeMajorClassVO> result = collegePage.getResult().stream().map(college -> {
            // 获取该学院下的所有专业（根据查询条件过滤）
            List<MajorVO> majors = teacherManagementMapper.getMajorsByCollegeId(
                    college.getId(),
                    collegeMajorClassPageDTO.getMajorName(),
                    collegeMajorClassPageDTO.getMajorCode());
            college.setMajorVO(majors);
            return college;
        }).collect(Collectors.toList());

        return PageResult.builder()
                .total(collegePage.getTotal())
                .records(result)
                .build();
    }

    /**
     * 添加教师账号
     *
     * @param teacherDTO 教师信息
     */
    @RedisCacheEvict(value = {@RedisCacheEvict.CacheKeyConfig(keyPrefix = "teacherAccount:list:", isPattern = true)})
    @Transactional
    @Override
    public void addTeacherAccount(TeacherDTO teacherDTO) {
        //查看是否已存在该用户
        User username = teacherManagementMapper.selectByUsername(teacherDTO.getUsername());
        if (username != null) {
            throw new ParametersQuestionException(ParametersQuestionConstant.USERNAME_EXIST);
        }
        User user = new User();
        BeanUtils.copyProperties(teacherDTO, user);
        user.setUserType(TypeConstant.USER_TYPE_TEACHER);
        user.setStatus(StatusConstant.STATUS_NORMAL);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        teacherManagementMapper.insertUser(user);

        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherDTO, teacher);

        teacher.setUserId(user.getId());
        teacherManagementMapper.insertTeacher(teacher);
    }

    /**
     * 获取教师账号列表
     *
     * @param userWithTeacherInfoPageDTO 筛选条件
     * @return 教师账号列表
     */
    @RedisCache(keyPrefix = "teacherAccount:list:",keyParts = {
            "#userWithTeacherInfoPageDTO.username", "#userWithTeacherInfoPageDTO.realName",
            "#userWithTeacherInfoPageDTO.status", "#userWithTeacherInfoPageDTO.teacherNo",
            "#userWithTeacherInfoPageDTO.pageNum", "#userWithTeacherInfoPageDTO.pageSize"
    },expireTime = 1, timeUnit = TimeUnit.HOURS)
    @Override
    public PageResult listTeacherAccount(UserWithTeacherInfoPageDTO userWithTeacherInfoPageDTO) {
        PageHelper.startPage(userWithTeacherInfoPageDTO.getPageNum(), userWithTeacherInfoPageDTO.getPageSize());
        Page<UserWithTeacherInfoPageVO> page = teacherManagementMapper.listTeacherAccount(userWithTeacherInfoPageDTO);
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    /**
     * 下载模板
     *
     * @return 模板
     */
    @Override
    public ResponseEntity<byte[]> downloadTemplate() {
        //创建excel写入器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 创建表头和示例行
        List<Map<String, Object>> rows = new ArrayList<>();
        Map<String, Object> headerMap = getStringObjectMap();
        rows.add(headerMap);
        // 写入数据
        writer.write(rows, true);
        //设置列宽自适应
        for (int i = 0; i < headerMap.size(); i++) {
            writer.setColumnWidth(i, 30);
        }
        // 输出到字节数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writer.flush(outputStream);
        writer.close();
        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "teacher_template.xlsx");
        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

    /**
     * 批量导入教师账号
     *
     * @param file 文件
     * @return 错误信息
     */
    @RedisCacheEvict(value = {@RedisCacheEvict.CacheKeyConfig(keyPrefix = "teacherAccount:list:", isPattern = true)})
    @Override
    public String batchImportTeacher(MultipartFile file) {
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
                if (rowData.size() < 12) {
                    failCount++;
                    errorMessage.append("第").append(i + 3).append("行数据列数不足; ");
                    continue;
                }

                try {
                    User user = new User();
                    Teacher teacher = new Teacher();

                    // 解析数据并填充到User实体类中（前6列）
                    // 用户名
                    user.setUsername(getStringValue(rowData.get(0)));
                    // 加密密码
                    user.setPassword(DigestUtils.md5DigestAsHex(getStringValue(rowData.get(1)).getBytes()));
                    // 真实姓名
                    user.setRealName(getStringValue(rowData.get(2)));
                    // 手机号
                    user.setPhone(getStringValue(rowData.get(3)));
                    // 邮箱
                    user.setEmail(getStringValue(rowData.get(4)));
                    // 性别
                    user.setGender(parseGender(getStringValue(rowData.get(5))));
                    // 设置为教师类型
                    user.setUserType(TypeConstant.USER_TYPE_TEACHER);
                    // 默认启用状态
                    user.setStatus(StatusConstant.STATUS_NORMAL);

                    // 解析数据并填充到Teacher实体类中
                    Long collegeId = getCollegeIdByName(getStringValue(rowData.get(6)));
                    if (collegeId == null) {
                        failCount++;
                        errorMessage.append("第").append(i + 3).append("行学院名称不存在; ");
                        continue;
                    }
                    teacher.setCollegeId(collegeId);
                    Long majorId = getMajorIdByName(collegeId, getStringValue(rowData.get(7)));
                    if (majorId == null) {
                        failCount++;
                        errorMessage.append("第").append(i + 3).append("行专业名称不存在或与学院不匹配; ");
                        continue;
                    }
                    teacher.setMajorId(majorId);
                    teacher.setTeacherNo(getStringValue(rowData.get(8)));
                    teacher.setTitle(getStringValue(rowData.get(9)));
                    teacher.setHireDate(parseLocalDate(getStringValue(rowData.get(10))));
                    // 在职状态
                    teacher.setStatus(parseInteger(getStringValue(rowData.get(11))));

                    // 检查用户名是否已存在
                    User existingUser = teacherManagementMapper.selectByUsername(user.getUsername());
                    if (existingUser != null) {
                        failCount++;
                        errorMessage.append("第").append(i + 3).append("行用户名已存在; ");
                        continue;
                    }

                    // 插入用户信息
                    teacherManagementMapper.insertUser(user);
                    // 设置教师表的用户ID
                    teacher.setUserId(user.getId());
                    // 插入教师信息
                    teacherManagementMapper.insertTeacher(teacher);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    errorMessage.append("第").append(i + 3).append("行数据错误: ").append(e.getMessage()).append("; ");
                }
            }
            reader.close();
            // 返回结果
            String resultMsg = "导入完成，成功:" + successCount + "条，失败:" + failCount + "条。";
            if (failCount > 0) {
                resultMsg += " 错误信息：" + errorMessage.toString();
                throw new ParametersQuestionException(ParametersQuestionConstant.EXCEL_ERROR + resultMsg);
            }
            return resultMsg;
        } catch (Exception e) {
            throw new ParametersQuestionException(ParametersQuestionConstant.EXCEL_ERROR + e.getMessage());
        }
    }

    /**
     * 获取教师账号详情
     *
     * @param id id
     * @return UserWithStudentInfoVO
     */
    @RedisCache(keyPrefix = "teacherAccount:detail:", keyParts = {"#id"}, expireTime = 1, timeUnit = TimeUnit.HOURS)
    @Override
    public UserWithTeacherInfoVO getTeacherAccountDetail(String id) {
        return teacherManagementMapper.getTeacherAccountDetail(id);
    }

    /**
     * 修改教师账号
     *
     * @param teacherDTO 教师信息
     */
    @RedisCacheEvict(value = {
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "teacherAccount:list:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "teacherAccount:detail:", keyParts = "#teacherDTO.id")
    })
    @Transactional
    @Override
    public void updateTeacherAccount(TeacherDTO teacherDTO) {
        User user = new User();
        user.setId(teacherDTO.getId());
        user.setRealName(teacherDTO.getRealName());
        user.setPhone(teacherDTO.getPhone());
        user.setEmail(teacherDTO.getEmail());
        user.setGender(teacherDTO.getGender());
        teacherManagementMapper.updateUser(user);
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherDTO, teacher);
        teacher.setUserId(user.getId());
        teacherManagementMapper.updateTeacher(teacher);
    }

    /**
     * 删除教师账号
     *
     * @param ids id集合
     */
    @RedisCacheEvict(value = {
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "teacherAccount:list:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "teacherAccount:detail:", keyParts = "#ids")
    })
    @Transactional
    @Override
    public void deleteTeacherAccount(List<Long> ids) {
        teacherManagementMapper.deleteUser(ids);
        teacherManagementMapper.deleteTeacher(ids);
    }

    /**
     * 重置教师密码
     *
     * @param id id
     * @return 密码
     */
    @Override
    public String resetTeacherPassword(String id) {
        //根据id查询用户
        User user = teacherManagementMapper.selectById(id);
        if (user == null) {
            throw new ParametersQuestionException(ParametersQuestionConstant.USER_NOT_EXIST);
        }
        String password = RandomStringUtils.randomAlphanumeric(11);
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        teacherManagementMapper.updateUser(user);
        return password;
    }

    /**
     * 修改教师账号状态
     *
     * @param teacherAccountStatusDTO 修改信息
     */
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "teacherAccount:list:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "teacherAccount:detail:", keyParts = "#teacherAccountStatusDTO.id")
    })
    @Override
    public void updateTeacherAccountStatus(TeacherAccountStatusDTO teacherAccountStatusDTO) {
        User user = new User();
        BeanUtils.copyProperties(teacherAccountStatusDTO, user);
        teacherManagementMapper.updateUser(user);
        //删除redis中存储的用户信息
        Map<Object, Object> existingUserSession = redisTemplate.opsForHash().entries(RedisConstant.JWT_ID_KEY + teacherAccountStatusDTO.getId());
        if (!existingUserSession.isEmpty()) {
            Object existingToken = existingUserSession.get(MapConstant.USER_TOKEN);
            if (existingToken != null) {
                // 删除旧的token
                redisTemplate.delete(RedisConstant.JWT_TOKEN_KEY + existingToken);
            }
        }
    }

    /**
     * 导出教师账号
     *
     * @param userWithStudentInfoPageDTO dto
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<byte[]> exportTeacherAccount(UserWithTeacherInfoPageDTO userWithStudentInfoPageDTO) {
        try {
            PageHelper.startPage(userWithStudentInfoPageDTO.getPageNum(), userWithStudentInfoPageDTO.getPageSize());
            Page<TeacherExcelVO> page = teacherManagementMapper.exportTeacherAccount(userWithStudentInfoPageDTO);
            List<TeacherExcelVO> excelDataList = new ArrayList<>();
            for (TeacherExcelVO vo : page.getResult()) {
                TeacherExcelVO excelVO = new TeacherExcelVO();
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
                //处理在职状态（1:在职, 2:离职, 3:退休）
                if (vo.getStatus() != null) {
                    switch (parseInteger(vo.getStatus())) {
                        case 1:
                            excelVO.setStatus("在职");
                            break;
                        case 2:
                            excelVO.setStatus("离职");
                            break;
                        case 3:
                            excelVO.setStatus("退休");
                            break;
                        default:
                            excelVO.setStatus("未知");
                            break;
                    }
                }

                excelDataList.add(excelVO);
            }
            // 导出Excel
            byte[] excelBytes = ExcelExportUtil.exportExcel(excelDataList, TeacherExcelVO.class);

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "teacher_data.xlsx");
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new ParametersQuestionException("导出失败: " + e.getMessage());
        }
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
     * 解析日期值
     */
    private LocalDate parseLocalDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        // 尝试 yyyy-MM-dd HH:mm:ss 格式（LocalDateTime 格式）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
            return localDateTime.toLocalDate();
        } catch (Exception e) {
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
        return teacherManagementMapper.getCollegeIdByName(collegeName);
    }

    /**
     * 根据专业名称和学院id获取专业ID
     */
    private Long getMajorIdByName(Long collegeId, String majorName) {
        if (majorName.isEmpty() || collegeId == null) {
            return null;
        }
        return teacherManagementMapper.getMajorIdByNameWithCollegeId(collegeId, majorName);
    }

    /**
     * 获取示例行
     *
     * @return 示例行
     */
    private Map<String, Object> getStringObjectMap() {
        Map<String, Object> headerMap = new LinkedHashMap<>();
        headerMap.put("用户名(不能重复)", "例如：zhangsan");
        headerMap.put("密码", "例如：123456");
        headerMap.put("真实姓名", "例如：张三");
        headerMap.put("手机号", "例如：13800138000");
        headerMap.put("邮箱", "例如：zhangsan@example.com");
        headerMap.put("性别", "例如：男");
        headerMap.put("学院名称", "例如：信息工程学院");
        headerMap.put("专业名称", "例如：计算机科学与技术");
        headerMap.put("工号", "例如：202200010001");
        headerMap.put("职称", "例如：助教");
        headerMap.put("入职时间", "例如：2022-01-01");
        headerMap.put("在职状态（1:在职, 2:离职, 3:退休）", "例如:1");
        return headerMap;
    }
}
