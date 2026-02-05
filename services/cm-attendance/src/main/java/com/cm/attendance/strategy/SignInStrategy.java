package com.cm.attendance.strategy;

import com.cm.dto.AttendanceSignInDTO;

/**
 * @author 31373
 */
public interface SignInStrategy {

    void executeSignIn(AttendanceSignInDTO attendanceSignInDTO);

}
