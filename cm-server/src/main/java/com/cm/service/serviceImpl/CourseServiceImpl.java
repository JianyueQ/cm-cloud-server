package com.cm.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.cm.annotations.RedisCache;
import com.cm.annotations.RedisCacheEvict;
import com.cm.constant.*;
import com.cm.context.BaseContext;
import com.cm.dto.*;
import com.cm.entity.*;
import com.cm.exception.ParametersQuestionException;
import com.cm.exception.SystemException;
import com.cm.mapper.CourseMapper;
import com.cm.result.PageResult;
import com.cm.service.CourseService;
import com.cm.utils.RedisAtomicUtil;
import com.cm.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 31373
 */
@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RedisAtomicUtil redisAtomicUtil;

    /**
     * 分页查询课程
     *
     * @param coursePageDTO 课程分页参数
     * @return 课程分页结果
     */
    @Override
    @RedisCache(keyPrefix = "course:page:admin:", keyParts = {
            "#coursePageDTO.pageNum", "#coursePageDTO.pageSize",
            "#coursePageDTO.courseCode", "#coursePageDTO.name",
            "#coursePageDTO.courseType", "#coursePageDTO.status"
    }, expireTime = 1, timeUnit = TimeUnit.HOURS)
    public PageResult pageQuery(CoursePageDTO coursePageDTO) {
        PageHelper.startPage(coursePageDTO.getPageNum(), coursePageDTO.getPageSize());
        Page<CoursePageVO> page = courseMapper.pageQuery(coursePageDTO);
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    /**
     * 课程教师分页查询
     *
     * @param userWithTeacherInfoPageDTO 课程教师分页参数
     * @return 课程教师分页结果
     */
    @Override
    @RedisCache(keyPrefix = "course:teacher:page:", keyParts = {
            "#userWithTeacherInfoPageDTO.pageNum", "#userWithTeacherInfoPageDTO.pageSize",
            "#userWithTeacherInfoPageDTO.username", "#userWithTeacherInfoPageDTO.realName",
            "#userWithTeacherInfoPageDTO.teacherNo", "#userWithTeacherInfoPageDTO.status"
    }, expireTime = 1, timeUnit = TimeUnit.HOURS)
    public PageResult listTeacherAccount(UserWithTeacherInfoPageDTO userWithTeacherInfoPageDTO) {
        PageHelper.startPage(userWithTeacherInfoPageDTO.getPageNum(), userWithTeacherInfoPageDTO.getPageSize());
        Page<UserWithTeacherInfoPageVO> page = courseMapper.listTeacherAccount(userWithTeacherInfoPageDTO);
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    /**
     * 课程分配教师
     *
     * @param courseAssignTeacherDTO 课程分配教师参数
     */
    @Override
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:page:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:detail", keyParts = "#courseAssignTeacherDTO.courseId"),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:page:teacher:", isPattern = true)
    })
    public void assignTeacher(CourseAssignTeacherDTO courseAssignTeacherDTO) {
        CourseTeaching courseTeaching = new CourseTeaching();
        courseTeaching.setCourseId(courseAssignTeacherDTO.getCourseId());
        courseTeaching.setTeacherId(courseAssignTeacherDTO.getTeacherId());
        courseMapper.updateCourseTeaching(courseTeaching);
    }

    /**
     * 获取课程树结构
     *
     * @return 课程树结构数据
     */
    @RedisCache(keyPrefix = "course:tree:")
    @Override
    public List<CollegeMajorTreeVO> collegeMajorTree() {
        return courseMapper.collegeMajorTree();
    }

    /**
     * 课程班级分页查询
     *
     * @param classListPageDTO 课程班级分页参数
     * @return 课程班级分页结果
     */
    @Override
    public PageResult listClasses(ClassListPageDTO classListPageDTO) {
        PageHelper.startPage(classListPageDTO.getPageNum(), classListPageDTO.getPageSize());
        Page<ClassListPageVO> page = courseMapper.listClasses(classListPageDTO);
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    /**
     * 添加课程班级
     *
     * @param assignClassToCourse 添加课程班级参数
     */
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:page:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:detail:", keyParts = "#assignClassToCourse.courseId"),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:page:teacher:", isPattern = true)
    })
    @Override
    @Transactional
    public void addClass(AssignClassToCourseDTO assignClassToCourse) {
        //根据课程id查询课程管理表获取课程管理表id,教师id,学期
        CourseTeaching courseTeaching = courseMapper.getCourseTeachingById(assignClassToCourse.getCourseId());
        //判断是否为在职的教师并且真实的教师
        Teacher teacher = courseMapper.getTeacherById(courseTeaching.getTeacherId());
        if (teacher == null || teacher.getTeacherNo() == null) {
            throw new ParametersQuestionException(ParametersQuestionConstant.TEACHER_NOT_EXIST);
        } else if (StatusConstant.STATUS_INCUMBENCY.equals(teacher.getStatus())) {
            throw new ParametersQuestionException(ParametersQuestionConstant.TEACHER_NOT_INCUMBENCY);
        }

        CourseSelection courseSelection = new CourseSelection();
        courseSelection.setCourseId(assignClassToCourse.getCourseId());
        courseSelection.setCourseName(assignClassToCourse.getCourseName());
        courseSelection.setTeacherId(courseTeaching.getTeacherId());
        courseSelection.setTeacherName(teacher.getRealName());
        courseSelection.setCourseTeachingId(courseTeaching.getId());
        courseSelection.setSemester(courseTeaching.getSemester());
        courseSelection.setSelectionTime(LocalDateTime.now());
        courseSelection.setStatus(StatusConstant.STATUS_ELECTED);
        //根据课程id将所有之前该课程的选课记录删除
        courseMapper.deleteCourseSelectionByCourseId(assignClassToCourse.getCourseId());
        //根据classIds获取学生id
        List<Long> studentIds = courseMapper.getStudentIdsByClassIds(assignClassToCourse.getClassIds());
        for (Long studentId : studentIds) {
            courseSelection.setStudentId(studentId);
            courseMapper.saveCourseSelection(courseSelection);
        }

        if (assignClassToCourse.getTotalStudentCount() > courseTeaching.getMaxStudentCount()) {
            courseTeaching.setMaxStudentCount(assignClassToCourse.getTotalStudentCount());
        }
        courseTeaching.setCurrentStudentCount(studentIds.size());
        String classIds = assignClassToCourse.getClassIds().stream().map(String::valueOf).collect(Collectors.joining(","));
        courseTeaching.setClassIds(classIds);
        courseMapper.updateCourseTeaching(courseTeaching);
    }

    /**
     * 课程详情
     *
     * @param id 课程id
     * @return 课程详情
     */
    @RedisCache(keyPrefix = "course:detail:", keyParts = "#id")
    @Override
    public CourseDetailVO detail(String id) {
        return courseMapper.detail(id);
    }

    /**
     * 修改课程
     *
     * @param courseDTO 修改的课程信息
     */
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:page:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:detail:", keyParts = "#courseDTO.id"),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:page:teacher:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:selection:count:", isPattern = true)
    })
    @Transactional
    @Override
    public void updateCourse(CourseDTO courseDTO) {
        Course course = new Course();
        BeanUtils.copyProperties(courseDTO, course);
        courseMapper.updateCourse(course);

        //根据课程id查询课程教学信息
        CourseTeaching courseTeaching = courseMapper.getCourseTeachingById(courseDTO.getId());
        //判断最大人数是否小于当前人数
        if (courseDTO.getMaxStudentCount() < courseTeaching.getCurrentStudentCount()) {
            throw new ParametersQuestionException(ParametersQuestionConstant.MAX_STUDENT_COUNT_LESS_THAN_CURRENT_STUDENT_COUNT);
        }
        courseTeaching = new CourseTeaching();
        BeanUtils.copyProperties(courseDTO, courseTeaching);
        courseTeaching.setCourseId(courseDTO.getId());

        courseMapper.updateCourseTeaching(courseTeaching);
    }

    /**
     * 修改课程状态
     *
     * @param courseChangeStatusDTO 课程状态信息
     */
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:page:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:detail:", keyParts = "#courseChangeStatusDTO.id"),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:selection:count:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:selected:course:", isPattern = true),
    })
    @Override
    @Transactional
    public void changeStatus(CourseChangeStatusDTO courseChangeStatusDTO) {
        //判断是否有任课教师
        CourseTeaching courseTeaching = courseMapper.getCourseTeachingById(courseChangeStatusDTO.getId());
        //根据教师id查询教师
        Teacher teacher = courseMapper.getTeacherById(courseTeaching.getTeacherId());
        if (teacher == null) {
            throw new ParametersQuestionException(ParametersQuestionConstant.TEACHER_NOT_EXIST);
        }
        //判断课程类型是否为必修
        if (courseChangeStatusDTO.getCourseType().equals(TypeConstant.COURSE_TYPE_REQUIRED)) {
            //判断classIds是否为空
            if (StringUtils.isEmpty(courseTeaching.getClassIds())) {
                throw new ParametersQuestionException(ParametersQuestionConstant.CLASS_NOT_EXIST);
            }
        }
        courseTeaching = new CourseTeaching();
        courseTeaching.setCourseId(courseChangeStatusDTO.getId());
        courseTeaching.setStatus(courseChangeStatusDTO.getStatus());
        courseMapper.updateCourseTeaching(courseTeaching);
        Course course = new Course();
        course.setId(courseChangeStatusDTO.getId());
        course.setStatus(courseChangeStatusDTO.getStatus());
        courseMapper.updateCourse(course);
    }

    /**
     * 删除课程
     *
     * @param ids 课程id
     */
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:page:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:detail:", keyParts = "#ids"),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:page:teacher:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:selection:count:", isPattern = true)
    })
    @Override
    @Transactional
    public void deleteCourse(List<Long> ids) {
        courseMapper.deleteCourse(ids);
        courseMapper.deleteCourseTeaching(ids);
        courseMapper.deleteCourseSelection(ids);
    }

    @RedisCache(keyPrefix = "course:page:teacher:", keyParts = {
            "#coursePageDTO.pageNum", "#coursePageDTO.pageSize",
            "#coursePageDTO.courseCode", "#coursePageDTO.name",
            "#coursePageDTO.courseType", "#coursePageDTO.status",
            "#coursePageDTO.userId"
    }, expireTime = 1, timeUnit = TimeUnit.HOURS)
    @Override
    public PageResult pageQueryByTeacher(CoursePageDTO coursePageDTO) {
        PageHelper.startPage(coursePageDTO.getPageNum(), coursePageDTO.getPageSize());
        Page<CoursePageVO> page = courseMapper.pageQueryByTeacher(coursePageDTO);
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    @RedisCache(keyPrefix = "course:page:student:", keyParts = {
            "#coursePageDTO.pageNum", "#coursePageDTO.pageSize",
            "#coursePageDTO.name", "#coursePageDTO.courseType", "#coursePageDTO.semester"
    }, expireTime = 1, timeUnit = TimeUnit.HOURS)
    @Override
    public PageResult pageQueryByStudent(CoursePageDTO coursePageDTO) {
        coursePageDTO.setNow(LocalDateTime.now());
        PageHelper.startPage(coursePageDTO.getPageNum(), coursePageDTO.getPageSize());
        Page<StudentCoursePageVO> page = courseMapper.pageQueryByStudent(coursePageDTO);
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    /**
     * 获取当前和最大人数
     *
     * @param courseIds 课程分页参数
     * @return 当前和最大人数
     */
    @Override
    public List<CoursesNumberVO> getCurrentAndMaxStudentCount(List<Long> courseIds) {
        if (courseIds == null || courseIds.isEmpty()) {
            return new ArrayList<>();
        }
        // 检查Redis中是否有缓存数据
        Map<Object, Object> cachedData = redisTemplate.opsForHash().entries(RedisConstant.COURSE_SELECTION_COUNT_HASH);

        List<CoursesNumberVO> result;

        if (!cachedData.isEmpty()) {
            // Redis中有缓存数据，根据课程ID列表从缓存中获取
            result = getCourseSelectionCountFromCache(courseIds, cachedData);
        } else {
            LocalDateTime now = LocalDateTime.now();
            // Redis中没有缓存数据，从数据库获取全部启用且未删除的课程信息
            List<CoursesNumberVO> allCourses = courseMapper.getAllActiveCourseSelectionCount(now);

            // 将全部数据存入Redis缓存
            cacheAllCourseSelectionCount(allCourses);

            // 根据课程ID列表筛选数据
            result = filterCourseSelectionCountByCourseIds(courseIds, allCourses);
        }
        return result;
    }


    /**
     * 选课
     *
     * @param studentSelectCourseDTO 课程id
     */
    @Override
    public void select(StudentSelectCourseDTO studentSelectCourseDTO) {
        List<Long> courseIds = studentSelectCourseDTO.getCourseIds();
        LocalDateTime now = LocalDateTime.now();
        //使用redis分布式锁解决并发问题
        String lockKey = RedisConstant.COURSE_SELECTION_LOCK_KEY + courseIds.toString();
        String lockValue = UUID.randomUUID().toString();
        //尝试获取锁
        while (true) {
            Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 10, TimeUnit.SECONDS);
            if (lockAcquired != null && lockAcquired) {
                break;
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            //选课操作被中断
            throw new SystemException(MessageConstant.SELECT_COURSE_INTERRUPTED);
        }
        try {
            //获取当前选课人数和最大选课人数
            List<String> courseIdsString = courseIds.stream().map(String::valueOf).toList();
            List<Object> courseIdsObject = new ArrayList<>(courseIdsString);
            List<Object> selectionCounts = redisTemplate.opsForHash().multiGet(RedisConstant.COURSE_SELECTION_COUNT_HASH, courseIdsObject);
            //检查是否有足够的位置供学生选择
            for (int i = 0; i < courseIds.size(); i++) {
                Long courseId = courseIds.get(i);
                Object countData = selectionCounts.get(i);
                if (countData == null) {
                    // 如果Redis中没有缓存数据，则从数据库获取
                    List<CoursesNumberVO> courseNumbers = courseMapper.getAllActiveCourseSelectionCount(now);
                    cacheAllCourseSelectionCount(courseNumbers);
                    // 重新获取该课程的信息
                    countData = redisTemplate.opsForHash().get(RedisConstant.COURSE_SELECTION_COUNT_HASH, courseId.toString());
                }
                if (countData != null) {
                    String[] counts = countData.toString().split(",");
                    int currentCount = Integer.parseInt(counts[0]);
                    int maxCount = Integer.parseInt(counts[1]);

                    // 检查是否超过最大人数限制
                    if (currentCount >= maxCount) {
                        throw new ParametersQuestionException(ParametersQuestionConstant.SELECT_COURSE_EXCEED_MAX_COUNT);
                    }
                } else {
                    throw new ParametersQuestionException(ParametersQuestionConstant.SELECT_COURSE_NOT_EXIST);
                }
            }
            Long currentId = BaseContext.getCurrentId();
            //获取学生id
            Long studentId = courseMapper.getStudentIdByUserId(currentId);
            //获取全部规定选课时间中的选课时间
            List<Course> courses = courseMapper.getAllActiveCourseBySelectionTime(now);
            Map<Long, Course> courseMap = courses.stream().collect(Collectors.toMap(Course::getId, course -> course));
            for (Long courseId : courseIds) {
                // 检查学生是否已经选择了该课程
                CourseSelection existingSelection = courseMapper.getCourseSelectionByStudentAndCourse(studentId, courseId);
                if (existingSelection != null) {
                    // 如果已经选过该课程
                    throw new ParametersQuestionException(existingSelection.getCourseName() + ParametersQuestionConstant.SELECT_COURSE_ALREADY_SELECTED);
                }
                // 检查该课程是否在规定选课时间中
                Course course = courseMap.get(courseId);
                if (course == null || !now.isAfter(course.getSelectionStartTime()) || now.isAfter(course.getSelectionEndTime())) {
                    throw new ParametersQuestionException(ParametersQuestionConstant.SELECT_COURSE_NOT_IN_TIME);
                }
                // 获取填充选课记录的相关信息
                CourseSelection courseSelection = courseMapper.getFillCourseSelection(courseId);
                if (courseSelection == null) {
                    throw new ParametersQuestionException(ParametersQuestionConstant.SELECT_COURSE_NOT_EXIST);
                }
                //创建选课记录
                courseSelection.setStudentId(studentId);
                courseSelection.setCourseId(courseId);
                courseSelection.setSelectionTime(LocalDateTime.now());
                courseSelection.setStatus(StatusConstant.STATUS_ELECTED);
                courseMapper.saveCourseSelection(courseSelection);

                // 更新Redis中的选课人数
                String courseData = (String) redisTemplate.opsForHash().get(RedisConstant.COURSE_SELECTION_COUNT_HASH, courseId.toString());
                if (courseData != null) {
                    String[] counts = courseData.split(",");
                    int currentCount = Integer.parseInt(counts[0]);
                    int maxCount = Integer.parseInt(counts[1]);
                    String updatedCourseData = (currentCount + 1) + "," + maxCount;
                    redisTemplate.opsForHash().put(RedisConstant.COURSE_SELECTION_COUNT_HASH, courseId.toString(), updatedCourseData);
                    // 设置过期时间，确保缓存不会永久存在
                    redisTemplate.expire(RedisConstant.COURSE_SELECTION_COUNT_HASH, 1, TimeUnit.HOURS);
                }
                //刷新选课缓存
                redisTemplate.delete(RedisConstant.COURSE_SELECTED_COURSE + currentId);
            }
        } finally {
            //释放锁
            String currentValue = redisTemplate.opsForValue().get(lockKey);
            if (lockValue.equals(currentValue)) {
                redisTemplate.delete(lockKey);
            }
        }
    }

    /**
     * 获取当前用户已选课程的详细信息
     *
     * @return 课程分页结果
     */
    @Override
    public List<CourseSelectionVO> listSelectedCourse() {
        Long currentId = BaseContext.getCurrentId();
        //判断是否有缓存
        String cacheKey = RedisConstant.COURSE_SELECTED_COURSE + currentId;
        String cacheData = redisTemplate.opsForValue().get(cacheKey);
        if (cacheData != null) {
            return JSON.parseArray(cacheData, CourseSelectionVO.class);
        }
        //拼接时间,只能获取当前时间地前两天和后两条内的已经选择的课程
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusDays(2);
        LocalDateTime endTime = now.plusDays(2);
        List<CourseSelectionVO> courseSelectionVO = courseMapper.listSelectedCourse(currentId, startTime, endTime);
        //防止缓存雪崩,固定时间加上一个随机的时间,时间单位为分钟
        long expireTime = 600 + new Random().nextInt(120);
        //存入redis
        redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(courseSelectionVO), expireTime, TimeUnit.SECONDS);
        return courseSelectionVO;
    }

    /**
     * 取消选课
     *
     * @param cancelCourseSelectionDTO 课程id
     */
    @Override
    public void deleteCourseSelection(CancelCourseSelectionDTO cancelCourseSelectionDTO) {
        Long currentId = BaseContext.getCurrentId();
        String redisKey = RedisConstant.COURSE_SELECTION_COUNT_HASH;
        LocalDateTime now = LocalDateTime.now();
        //获取全部规定时间中的选课
        List<Course> courses = courseMapper.getAllActiveCourseBySelectionTime(now);
        Map<Long, Course> courseMap = courses.stream().collect(Collectors.toMap(Course::getId, course -> course));
        for (Long courseId : cancelCourseSelectionDTO.getCourseIds()) {
            // 检查该课程是否在规定时间中
            Course course = courseMap.get(courseId);
            if (course == null || !now.isAfter(course.getSelectionStartTime()) || now.isAfter(course.getSelectionEndTime())) {
                throw new ParametersQuestionException(ParametersQuestionConstant.SELECTION_NOT_IN_TIME);
            }
            // 检查Redis缓存是否存在，如果不存在则从数据库加载
            if (!redisAtomicUtil.hasHashField(redisKey, courseId.toString())) {
                // Redis中没有缓存数据，从数据库获取全部启用且未删除的课程信息
                List<CoursesNumberVO> allCourses = courseMapper.getAllActiveCourseSelectionCount(now);
                // 将全部数据存入Redis缓存
                cacheAllCourseSelectionCount(allCourses);
            }
            // 使用原子操作减少选课人数，只需传入redisKey和courseId
            boolean success = redisAtomicUtil.decrementHashField(redisKey, courseId.toString());
            if (!success) {
                // 如果操作失败，可能课程数据有问题，记录日志或抛出异常
                log.warn("无法减少课程ID:{}:的课程选择计数", courseId);
            }

        }
        //取消课程选课
        cancelCourseSelectionDTO.setUserId(currentId);
        cancelCourseSelectionDTO.setCancelTime(now);
        //取消课程
        courseMapper.cancelCourseSelection(cancelCourseSelectionDTO);
        //刷新缓存
        redisTemplate.delete(RedisConstant.COURSE_SELECTED_COURSE + currentId);
    }

    @Override
    public List<CourseListVO> listCourse() {
        Long currentId = BaseContext.getCurrentId();
        return courseMapper.listCourse(currentId);
    }

    @Override
    public List<CourseListVO> listCourseByStudent() {
        Long currentId = BaseContext.getCurrentId();
        return courseMapper.listCourseByStudent(currentId);
    }

    @Override
    public List<CourseListVO> adminListCourse() {
        return courseMapper.listCourseByAdmin();
    }

    /**
     * 根据课程ID列表筛选数据
     *
     * @param courseIds  课程ID列表
     * @param allCourses 全部课程数据
     * @return 筛选后的数据
     */
    private List<CoursesNumberVO> filterCourseSelectionCountByCourseIds(List<Long> courseIds, List<CoursesNumberVO> allCourses) {
        if (allCourses == null || allCourses.isEmpty()) {
            return new ArrayList<>();
        }

        // 将全部课程信息转换为Map，便于查找
        Map<Long, CoursesNumberVO> allCoursesMap = allCourses.stream()
                .collect(Collectors.toMap(CoursesNumberVO::getId, coursesNumberVO -> coursesNumberVO));

        List<CoursesNumberVO> result = new ArrayList<>();
        for (Long courseId : courseIds) {
            if (allCoursesMap.containsKey(courseId)) {
                result.add(allCoursesMap.get(courseId));
            }
        }

        return result;
    }

    /**
     * 缓存全部课程选择人数
     *
     * @param allCourses 全部课程数据
     */
    private void cacheAllCourseSelectionCount(List<CoursesNumberVO> allCourses) {
        if (allCourses == null || allCourses.isEmpty()) {
            return;
        }

        Map<String, String> hashData = new HashMap<>();
        for (CoursesNumberVO course : allCourses) {
            String courseData = course.getCurrentStudentCount() + "," + course.getMaxStudentCount();
            hashData.put(String.valueOf(course.getId()), courseData);
        }

        redisTemplate.opsForHash().putAll(RedisConstant.COURSE_SELECTION_COUNT_HASH, hashData);
        // 设置过期时间，例如1小时
        redisTemplate.expire(RedisConstant.COURSE_SELECTION_COUNT_HASH, 1, TimeUnit.HOURS);
    }

    /**
     * 从缓存中获取课程选择人数
     *
     * @param courseIds  课程ID列表
     * @param cachedData 缓存数据
     * @return 课程选择人数
     */
    private List<CoursesNumberVO> getCourseSelectionCountFromCache(List<Long> courseIds, Map<Object, Object> cachedData) {
        List<CoursesNumberVO> result = new ArrayList<>();

        for (Long courseId : courseIds) {
            String field = String.valueOf(courseId);
            if (cachedData.containsKey(field)) {
                String value = (String) cachedData.get(field);
                String[] counts = value.split(",");
                CoursesNumberVO vo = CoursesNumberVO.builder()
                        .id(courseId)
                        .currentStudentCount(Integer.parseInt(counts[0]))
                        .maxStudentCount(Integer.parseInt(counts[1]))
                        .build();
                result.add(vo);
            }
        }

        return result;
    }

    /**
     * 添加课程
     *
     * @param courseDTO 添加的课程信息
     */
    @Transactional
    @Override
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:page:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:page:teacher:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "course:selection:count:", isPattern = true)
    })
    public void addCourse(CourseDTO courseDTO) {
        Course course = new Course();
        if (courseDTO.getCourseType() != 1) {
            // 不是必修的非空校验
            if (courseDTO.getSelectionStartTime() == null) {
                throw new ParametersQuestionException(ParametersQuestionConstant.SELECTION_START_TIME_NOT_NULL);
            }
            if (courseDTO.getSelectionEndTime() == null) {
                throw new ParametersQuestionException(ParametersQuestionConstant.SELECTION_END_TIME_NOT_NULL);
            }
        }
        BeanUtils.copyProperties(courseDTO, course);
        course.setStatus(StatusConstant.STATUS_DISABLE);
        courseMapper.addCourse(course);
        CourseTeaching courseTeaching = new CourseTeaching();
        courseTeaching.setCourseId(course.getId());
        courseTeaching.setStatus(StatusConstant.STATUS_DISABLE);

        Long currentId = BaseContext.getCurrentId();
        // 判断当前用户是否是老师
        User user = courseMapper.getUserById(currentId);
        // 不是教师
        if (!user.getUserType().equals(TypeConstant.USER_TYPE_TEACHER)) {
            //获取当前时间戳转换成毫秒,临时的教师id
            courseTeaching.setTeacherId(System.currentTimeMillis() + UUID.randomUUID().hashCode());
        } else {
            courseTeaching.setTeacherId(user.getTeacher().getId());
        }
        courseTeaching.setSemester(courseDTO.getSemester());
        courseTeaching.setMaxStudentCount(courseDTO.getMaxStudentCount());
        courseTeaching.setScheduleInfo(courseDTO.getScheduleInfo());
        courseMapper.addCourseTeaching(courseTeaching);
    }
}
