package com.cm.attendance.strategy;

import com.cm.attendance.mapper.AttendanceManagementMapper;
import com.cm.attendance.websocketServer.AttendanceWebSocketServer;
import com.cm.common.core.constant.ParametersQuestionConstant;
import com.cm.common.core.constant.RedisConstant;
import com.cm.common.core.constant.StatusConstant;
import com.cm.common.core.context.BaseContext;
import com.cm.common.core.exception.ParametersQuestionException;
import com.cm.dto.AttendanceSignInDTO;
import com.cm.entity.AttendanceInitiate;
import com.cm.entity.AttendanceRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 31373
 */
@Service
public class PasswordSignInStrategy implements SignInStrategy {

    private final RedisTemplate<String, String> redisTemplate;
    private final AttendanceManagementMapper attendanceManagementMapper;

    public PasswordSignInStrategy(RedisTemplate<String, String> redisTemplate, AttendanceManagementMapper attendanceManagementMapper) {
        this.redisTemplate = redisTemplate;
        this.attendanceManagementMapper = attendanceManagementMapper;
    }

    /**
     * 密码签到
     * @param attendanceSignInDTO 签到信息
     */
    @Override
    public void executeSignIn(AttendanceSignInDTO attendanceSignInDTO) {
        Long currentId = BaseContext.getCurrentId();
        // 检查学生是否已经签到，防止重复签到
        AttendanceRecord existingRecord = null;
        if (attendanceSignInDTO.getSignInStatus() == null || attendanceSignInDTO.getSignInStatus() == 3) {
            // 根据学生id,考勤id,课程id获取当前签到状态
            existingRecord = attendanceManagementMapper.getAttendanceRecord(attendanceSignInDTO, currentId);
        }
        // 如果已经签到（状态不为"未签到"），则拒绝重复签到
        if (existingRecord != null && !existingRecord.getStatus().equals(StatusConstant.ATTENDANCE_STATUS_NOT_SIGN_IN)) {
            throw new ParametersQuestionException(ParametersQuestionConstant.ATTENDANCE_ALREADY_SIGN_IN);
        }
        String password = redisTemplate.opsForValue().get(RedisConstant.ATTENDANCE_VERIFICATION_KEY + attendanceSignInDTO.getCourseId());
        if (password == null) {
            //签到已经过期
            throw new ParametersQuestionException(ParametersQuestionConstant.ATTENDANCE_EXPIRED);
        }
        if (!password.equals(attendanceSignInDTO.getSignInPassword())) {
            //密码错误
            throw new ParametersQuestionException(ParametersQuestionConstant.ATTENDANCE_PASSWORD_ERROR);
        }

        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setAttendanceInitiateId(attendanceSignInDTO.getAttendanceInitiateId());
        attendanceRecord.setCourseId(attendanceSignInDTO.getCourseId());
        attendanceRecord.setSignInPassword(attendanceSignInDTO.getSignInPassword());
        attendanceRecord.setStatus(StatusConstant.ATTENDANCE_STATUS_SIGN_IN);
        attendanceRecord.setUserId(currentId);
        attendanceRecord.setSignInTime(LocalDateTime.now());
        attendanceManagementMapper.updateAttendanceRecordWithStatus(attendanceRecord);
        AttendanceInitiate attendanceInitiateById = attendanceManagementMapper.getAttendanceInitiateById(attendanceSignInDTO.getAttendanceInitiateId());
        //通知教师端
        Map<String, Object> data = new HashMap<>();
        data.put("type", "attendance_sign_in");
        AttendanceWebSocketServer.sendAttendanceNotification(attendanceInitiateById.getTeacherId(), data);
    }
}
