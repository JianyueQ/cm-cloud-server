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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 31373
 */
@Service
public class LocationSignInStrategy implements SignInStrategy {

    // 地球半径（米）
    private static final double EARTH_RADIUS = 6371000;
    // 允许的位置误差（米）
    private static final double ALLOWED_ERROR_METERS = 25.0;
    private final RedisTemplate<String, String> redisTemplate;
    private final AttendanceManagementMapper attendanceManagementMapper;

    public LocationSignInStrategy(RedisTemplate<String, String> redisTemplate, AttendanceManagementMapper attendanceManagementMapper) {
        this.redisTemplate = redisTemplate;
        this.attendanceManagementMapper = attendanceManagementMapper;
    }

    /**
     * 位置签到
     *
     * @param attendanceSignInDTO 位置签到参数
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
        //从缓存中获取经纬度
        String redisValue = redisTemplate.opsForValue().get(RedisConstant.ATTENDANCE_VERIFICATION_KEY + attendanceSignInDTO.getCourseId());
        if (redisValue == null) {
            //签到已经过期
            throw new ParametersQuestionException(ParametersQuestionConstant.ATTENDANCE_EXPIRED);
        }
        String[] split = redisValue.split(",");
        String teacherLatitude = split[0];
        String teacherLongitude = split[1];
        //类型转换
        BigDecimal teacherLat = new BigDecimal(teacherLatitude);
        BigDecimal teacherLon = new BigDecimal(teacherLongitude);
        BigDecimal studentLatitude = attendanceSignInDTO.getLatitude();
        BigDecimal studentLongitude = attendanceSignInDTO.getLongitude();

        //计算距离
        double distance = calculateDistance(
                studentLatitude.doubleValue(),
                studentLongitude.doubleValue(),
                teacherLat.doubleValue(),
                teacherLon.doubleValue()
        );
        //判断是否在允许范围内
        if (distance > ALLOWED_ERROR_METERS) {
            throw new ParametersQuestionException("位置超出允许范围，当前位置距离签到点" + Math.round(distance) + "米");
        }

        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setAttendanceInitiateId(attendanceSignInDTO.getAttendanceInitiateId());
        attendanceRecord.setCourseId(attendanceSignInDTO.getCourseId());
        attendanceRecord.setLatitude(attendanceSignInDTO.getLatitude());
        attendanceRecord.setLongitude(attendanceSignInDTO.getLongitude());
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

    /**
     * 使用Haversine公式计算两个经纬度点之间的距离（米）
     *
     * @param lat1 第一个点的纬度
     * @param lon1 第一个点的经度
     * @param lat2 第二个点的纬度
     * @param lon2 第二个点的经度
     * @return 距离（米）
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // 将角度转换为弧度
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // 计算差值
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // Haversine公式
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 计算距离
        return EARTH_RADIUS * c;
    }
}

