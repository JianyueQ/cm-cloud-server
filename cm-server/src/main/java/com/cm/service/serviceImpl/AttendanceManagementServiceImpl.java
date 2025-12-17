package com.cm.service.serviceImpl;

import com.cm.WebSocketServer.StudentWebSocketServer;
import com.cm.constant.ParametersQuestionConstant;
import com.cm.constant.RedisConstant;
import com.cm.constant.StatusConstant;
import com.cm.constant.TypeConstant;
import com.cm.context.BaseContext;
import com.cm.dto.AttendanceListPageDTO;
import com.cm.dto.AttendanceStudentListDTO;
import com.cm.dto.StartAttendanceDTO;
import com.cm.dto.UpdateAttendanceStatusDTO;
import com.cm.entity.*;
import com.cm.exception.ParametersQuestionException;
import com.cm.manager.AsyncManager;
import com.cm.manager.factory.AsyncFactory;
import com.cm.mapper.AttendanceManagementMapper;
import com.cm.result.PageResult;
import com.cm.service.AttendanceManagementService;
import com.cm.vo.AttendanceListPageVO;
import com.cm.vo.AttendanceStudentListVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 31373
 */
@Slf4j
@Service
public class AttendanceManagementServiceImpl implements AttendanceManagementService {

    @Autowired
    private AttendanceManagementMapper attendanceManagementMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final RabbitTemplate rabbitTemplate;

    public AttendanceManagementServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 开始签到
     *
     * @param startAttendanceDTO 开始签到参数
     */
    @Override
    @Transactional
    public void startAttendance(StartAttendanceDTO startAttendanceDTO) {
        String value = redisTemplate.opsForValue().get(RedisConstant.ATTENDANCE_VERIFICATION_KEY + startAttendanceDTO.getCourseId());
        if (value != null) {
            //已经有一个正在进行的签到
            throw new ParametersQuestionException(ParametersQuestionConstant.ATTENDANCE_EXIST);
        }
        Long currentId = BaseContext.getCurrentId();
        AttendanceInitiate attendanceInitiate = new AttendanceInitiate();
        //根据课程id获取课程信息
        CourseTeaching courseTeaching = attendanceManagementMapper.getCourseById(startAttendanceDTO.getCourseId());
        attendanceInitiate.setCourseTeachingId(courseTeaching.getId());
        attendanceInitiate.setCourseId(courseTeaching.getCourseId());
        attendanceInitiate.setCourseName(courseTeaching.getCourseName());
        attendanceInitiate.setTeacherId(courseTeaching.getTeacherId());
        //根据用户id获取用户信息
        User user = attendanceManagementMapper.getUserById(currentId);
        attendanceInitiate.setTeacherName(user.getRealName());
        //考勤开始时间
        LocalDateTime startTime = LocalDateTime.now();
        //考勤结束时间
        LocalDateTime endTime = startTime.plusMinutes(startAttendanceDTO.getDuration());
        attendanceInitiate.setStartTime(startTime);
        attendanceInitiate.setEndTime(endTime);
        String redisValue = "";
        if (TypeConstant.LOCATION_CHECK_IN.equals(startAttendanceDTO.getSignInType())) {
            attendanceInitiate.setSignInType(TypeConstant.LOCATION_CHECK_IN);
            attendanceInitiate.setLongitude(startAttendanceDTO.getLongitude());
            attendanceInitiate.setLatitude(startAttendanceDTO.getLatitude());
            redisValue = startAttendanceDTO.getLatitude() + "," + startAttendanceDTO.getLongitude();
        }
        if (TypeConstant.PASSWORD_CHECK_IN.equals(startAttendanceDTO.getSignInType())) {
            attendanceInitiate.setSignInType(TypeConstant.PASSWORD_CHECK_IN);
            attendanceInitiate.setSignInPassword(startAttendanceDTO.getSignInPassword());
            redisValue = startAttendanceDTO.getSignInPassword();
        }
        attendanceInitiate.setAttendanceType(startAttendanceDTO.getAttendanceType());
        attendanceInitiate.setStatus(StatusConstant.ATTENDANCE_STATUS_WAITING);
        attendanceManagementMapper.insertAttendanceInitiate(attendanceInitiate);
        //调用异步操作
        try {
            Long attendanceInitiateId = attendanceInitiate.getId();
            //计算延迟时间（毫秒）
            long delay = Duration.between(LocalDateTime.now(), endTime).toMillis();
            log.info("设置考勤结束任务，延迟时间: {} 毫秒", delay);
            if (delay > 0) {
//                AsyncManager.me().execute(AsyncFactory.updateAttendanceStatusTask(attendanceInitiate.getId()), delay);
                //rabbitMQ 延迟消息
                rabbitTemplate.convertAndSend("attendance.delay.exchange", "attendance.delay.message.update", attendanceInitiateId,message -> {
                    message.getMessageProperties().setDelayLong(delay);
                    return message;
                });
                //redis标记
                String delayMsgKey = RedisConstant.ATTENDANCE_DELAY_MESSAGE_KEY + attendanceInitiateId;
                redisTemplate.opsForValue().set(delayMsgKey, "1", startAttendanceDTO.getDuration() + 5 , TimeUnit.MINUTES);
            }
            attendanceInitiate.setCreateUser(currentId);
            //添加考勤记录信息
            AttendanceRecord attendanceRecord = getAttendanceRecord(attendanceInitiate, courseTeaching);
            AsyncManager.me().execute(AsyncFactory.addAttendanceRecordTask(attendanceRecord));
        } catch (Exception e) {
            log.error("考勤记录异常:{}", e.getMessage());
            throw e;
        }
        //将签到验证相关信息存入redis并设置过期时间
        String redisKey = RedisConstant.ATTENDANCE_VERIFICATION_KEY + startAttendanceDTO.getCourseId();
        redisTemplate.opsForValue().set(redisKey, redisValue, startAttendanceDTO.getDuration(), TimeUnit.MINUTES);
        String title = "考勤通知";
        String content = "教师:" + user.getRealName() + "发起了签到请求,请及时完成签到";
        String type = "attendance_start";
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("title", title);
        messageMap.put("content", content);
        messageMap.put("type", type);
        // 通知选课学生
        notifyStudentsForAttendance(startAttendanceDTO.getCourseId(), messageMap);
    }

    /**
     * 获取考勤记录
     *
     * @param attendanceInitiate 考勤信息
     * @param courseTeaching     课程信息
     * @return 考勤记录
     */
    private AttendanceRecord getAttendanceRecord(AttendanceInitiate attendanceInitiate, CourseTeaching courseTeaching) {
        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setAttendanceInitiateId(attendanceInitiate.getId());
        attendanceRecord.setCourseId(courseTeaching.getCourseId());
        attendanceRecord.setCourseName(courseTeaching.getCourseName());
        attendanceRecord.setCourseTeachingId(courseTeaching.getId());
        attendanceRecord.setTeacherId(courseTeaching.getTeacherId());
        //考勤状态（1:已签到, 2:迟到, 3:未签到, 4:请假）
        attendanceRecord.setStatus(StatusConstant.ATTENDANCE_STATUS_NOT_SIGN_IN);
        attendanceRecord.setCreateUser(attendanceInitiate.getCreateUser());
        return attendanceRecord;
    }

    /**
     * 通知选课学生
     *
     * @param courseId 课程id
     */
    private void notifyStudentsForAttendance(Long courseId, Map<String, Object> messageMap) {
        //获取选课学生列表
        List<CourseSelection> courseSelections = attendanceManagementMapper.listStudentIdsByCourseId(courseId);
        //通知选课学生
        for (CourseSelection selection : courseSelections) {
            if (selection.getStudentId() != null) {
                StudentWebSocketServer.sendAttendanceNotification(selection.getStudentId(), messageMap);
            }
        }
    }

    /**
     * 获取正在进行中的签到列表-分页查询
     *
     * @param attendanceListPageDTO 查询参数
     * @return 签到列表
     */
    @Override
    public PageResult listInitiate(AttendanceListPageDTO attendanceListPageDTO) {
        PageHelper.startPage(attendanceListPageDTO.getPageNum(), attendanceListPageDTO.getPageSize());
        Page<AttendanceListPageVO> page = attendanceManagementMapper.listInitiate(attendanceListPageDTO);
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    /**
     * 结束签到
     *
     * @param id 签到id
     */
    @Override
    public void endAttendance(Long id) {
        // 检查是否是手动结束（通过Redis标记）
        String delayMsgKey = RedisConstant.ATTENDANCE_DELAY_MESSAGE_KEY + id;
        Boolean hasDelayMessage = redisTemplate.hasKey(delayMsgKey);
        // 如果没有标记，说明可能是重复处理，直接返回避免数据库访问
        if (!hasDelayMessage) {
            log.info("考勤ID {} 无延迟消息标记，可能是重复处理，直接忽略", id);
            return;
        }
        try {
            AttendanceInitiate attendanceInitiate = new AttendanceInitiate();
            attendanceInitiate.setId(id);
            attendanceInitiate.setStatus(StatusConstant.ATTENDANCE_STATUS_END);
            attendanceManagementMapper.updateAttendanceInitiate(attendanceInitiate);
            attendanceInitiate = attendanceManagementMapper.getAttendanceInitiateById(id);
            redisTemplate.delete(RedisConstant.ATTENDANCE_VERIFICATION_KEY + attendanceInitiate.getCourseId());
            // 通知选课学生
            String title = "考勤通知";
            String content = "教师:" + attendanceInitiate.getTeacherName() + "结束了考勤";
            String type = "attendance_start";
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("title", title);
            messageMap.put("content", content);
            messageMap.put("type", type);
            notifyStudentsForAttendance(attendanceInitiate.getCourseId(), messageMap);

            // 处理成功后才清除标记
            redisTemplate.delete(delayMsgKey);
            log.info("成功处理考勤ID {} 并清除延迟消息标记", id);
        } catch (Exception e) {
            throw new RuntimeException("结束考勤失败",e);
        }
    }

    /**
     * 获取考勤的剩余时间
     *
     * @param id 考勤id
     * @return 剩余时间
     */
    @Override
    public Map<Object, Object> getRemainingTime(Long id) {
        //根据id获取redis缓存中的考勤信息的剩余时间
        String redisKey = RedisConstant.ATTENDANCE_VERIFICATION_KEY + id;
        // 获取剩余过期时间（秒）
        long expireTime = redisTemplate.getExpire(redisKey);
        Map<Object, Object> result = new HashMap<>();
        result.put("id", String.valueOf(id));
        if (expireTime > 0) {
            //将秒转换为时分秒格式
            int hours = (int) (expireTime / 3600);
            int minutes = (int) ((expireTime % 3600) / 60);
            int seconds = (int) (expireTime % 60);
            String timeFormat = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            result.put("remainingTime", timeFormat);
        } else {
            result.put("remainingTime", "00:00:00");
        }
        return result;
    }

    /**
     * 添加考勤记录
     *
     * @param attendanceRecord 考勤记录信息
     */
    @Override
    public void addAttendanceRecord(AttendanceRecord attendanceRecord) {
        //根据课程id获取选课列表中所有的学生id
        List<CourseSelection> courseSelections = attendanceManagementMapper.listStudentIdsByCourseId(attendanceRecord.getCourseId());
        if (courseSelections.isEmpty()) {
            return;
        }
        List<Long> studentIds = courseSelections.stream().map(CourseSelection::getStudentId).filter(Objects::nonNull).toList();
        if (studentIds.isEmpty()) {
            return;
        }
        List<User> users = attendanceManagementMapper.listStudentInfoByStudentIds(studentIds);
        // 批量创建考勤记录
        List<AttendanceRecord> recordsToInsert = new ArrayList<>();
        Long currentId = attendanceRecord.getCreateUser();
        LocalDateTime now = LocalDateTime.now();
        for (User user : users) {
            AttendanceRecord record = new AttendanceRecord();
            // 复制原始记录的属性
            BeanUtils.copyProperties(attendanceRecord, record);
            record.setId(null);
            record.setStudentId(user.getStudent().getId());
            record.setStudentName(user.getRealName());
            record.setStudentNo(user.getStudent().getStudentNo());
            record.setAvatar(user.getAvatar());
            //手动填充公共字段
            record.setCreateTime(now);
            record.setUpdateTime(now);
            record.setCreateUser(currentId);
            record.setUpdateUser(currentId);

            recordsToInsert.add(record);
        }
        // 批量插入所有记录
        attendanceManagementMapper.batchInsertAttendanceRecords(recordsToInsert);
    }

    /**
     * 获取考勤学生列表
     *
     * @param attendanceStudentListDTO 查询参数
     * @return 考勤学生列表
     */
    @Override
    public List<AttendanceStudentListVO> getStudentList(AttendanceStudentListDTO attendanceStudentListDTO) {
        return attendanceManagementMapper.getStudentList(attendanceStudentListDTO);
    }

    /**
     * 获取学生进行中的签到列表-分页查询
     *
     * @param attendanceListPageDTO 查询参数
     * @return 签到列表
     */
    @Override
    public PageResult studentListInitiate(AttendanceListPageDTO attendanceListPageDTO) {
        attendanceListPageDTO.setUserId(BaseContext.getCurrentId());
        PageHelper.startPage(attendanceListPageDTO.getPageNum(), attendanceListPageDTO.getPageSize());
        Page<AttendanceListPageVO> page = attendanceManagementMapper.studentListInitiate(attendanceListPageDTO);
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    /**
     * 修改学生考勤状态
     * @param updateAttendanceStatusDTO 修改参数
     */
    @Override
    public void updateStudentStatus(UpdateAttendanceStatusDTO updateAttendanceStatusDTO) {
        AttendanceRecord attendanceRecord = new AttendanceRecord();
        BeanUtils.copyProperties(updateAttendanceStatusDTO, attendanceRecord);
        attendanceRecord.setUserId(BaseContext.getCurrentId());
        if (attendanceRecord.getStatus() == null){
            attendanceRecord.setStatus(StatusConstant.ATTENDANCE_STATUS_NOT_SIGN_IN);
        }else {
            switch (attendanceRecord.getStatus()){
                case 1, 2:
                    attendanceRecord.setSignInTime(LocalDateTime.now());
                    break;
                case 4:
                default:
                    attendanceRecord.setSignOutTime(LocalDateTime.now());
            }
        }
        attendanceManagementMapper.teacherUpdateAttendanceRecordWithStatus(attendanceRecord);
    }
}
