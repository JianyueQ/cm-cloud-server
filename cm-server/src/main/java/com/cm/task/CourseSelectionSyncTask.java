package com.cm.task;


import com.cm.constant.RedisConstant;
import com.cm.entity.CourseTeaching;
import com.cm.mapper.CourseMapper;
import com.cm.utils.RedisAtomicUtil;
import com.cm.vo.CoursesNumberVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 定时任务：同步课程选课人数
 * 每5分钟执行一次
 * @author 31373
 */
@Component
@Slf4j
public class CourseSelectionSyncTask {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private RedisAtomicUtil redisAtomicUtil;

    /**
     * 定时任务：将Redis中的课程选课人数同步到数据库
     * 每5分钟执行一次
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void syncCourseSelectionToDatabase() {
        log.info("开始同步Redis中的课程选课人数到数据库");
        String redisKey = RedisConstant.COURSE_SELECTION_COUNT_HASH;

        try {
            // 获取Redis中所有的课程选课人数
            Map<Object, Object> redisCourseCounts = redisTemplate.opsForHash().entries(redisKey);

            if (redisCourseCounts.isEmpty()) {
                log.info("Redis中没有课程选课人数数据");

                // 如果Redis中没有数据，从数据库加载数据到Redis，防止缓存丢失
                List<CoursesNumberVO> dbCourses = courseMapper.getAllActiveCourseSelectionCount(LocalDateTime.now());
                if (dbCourses != null && !dbCourses.isEmpty()) {
                    Map<String, String> hashData = dbCourses.stream()
                            .collect(Collectors.toMap(
                                    course -> String.valueOf(course.getId()),
                                    course -> {
                                        int currentCount = course.getCurrentStudentCount() != null ? course.getCurrentStudentCount() : 0;
                                        int maxCount = course.getMaxStudentCount() != null ? course.getMaxStudentCount() : 0;
                                        return currentCount + "," + maxCount;
                                    }
                            ));

                    redisTemplate.opsForHash().putAll(redisKey, hashData);
                    redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);
                    log.info("已从数据库加载 {} 门课程的选课人数到Redis", dbCourses.size());
                }
                return;
            }

            // 从数据库获取所有相关课程的当前选课人数
            List<CoursesNumberVO> dbCourses = courseMapper.getAllActiveCourseSelectionCount(LocalDateTime.now());
            Map<Long, CoursesNumberVO> dbCourseMap = dbCourses.stream()
                    .collect(Collectors.toMap(CoursesNumberVO::getId, course -> course));

            int syncCount = 0;
            // 遍历Redis中的数据，同步到数据库
            for (Map.Entry<Object, Object> entry : redisCourseCounts.entrySet()) {
                try {
                    Long courseId = Long.valueOf(entry.getKey().toString());
                    String[] redisCounts = entry.getValue().toString().split(",");
                    int redisCurrentCount = Integer.parseInt(redisCounts[0]);
                    // int redisMaxCount = Integer.parseInt(redisCounts[1]); // 最大人数通常不变更，不需要同步

                    // 获取数据库中的选课人数
                    CoursesNumberVO dbCourse = dbCourseMap.get(courseId);
                    if (dbCourse != null) {
                        int dbCurrentCount = dbCourse.getCurrentStudentCount() != null ? dbCourse.getCurrentStudentCount() : 0;

                        // 如果Redis中的当前选课人数与数据库不一致，以Redis为准更新数据库
                        if (redisCurrentCount != dbCurrentCount) {
                            CourseTeaching courseTeaching = new CourseTeaching();
                            courseTeaching.setCourseId(courseId);
                            courseTeaching.setCurrentStudentCount(redisCurrentCount);

                            courseMapper.updateCourseTeaching(courseTeaching);
                            syncCount++;
                            log.info("课程ID {} 的选课人数已从数据库({})同步为Redis中的值({})",
                                    courseId, dbCurrentCount, redisCurrentCount);
                        }
                    } else {
                        log.warn("数据库中未找到课程ID {} 的信息", courseId);
                    }
                } catch (Exception e) {
                    log.error("同步课程选课人数时发生异常，课程ID: {}", entry.getKey(), e);
                }
            }
            log.info("成功同步 {} 门课程的选课人数到数据库", syncCount);
            //清除教师端和管理端的分页缓存
            redisAtomicUtil.deleteKeysByPatternWithLuaScan(RedisConstant.TEACHER_COURSE_PAGE_KEY + "*");
            redisAtomicUtil.deleteKeysByPatternWithLuaScan(RedisConstant.ADMIN_COURSE_PAGE_KEY + "*");
        } catch (Exception e) {
            log.error("同步课程选课人数时发生异常", e);
        }
        log.info("课程选课人数同步完成");
    }

}
