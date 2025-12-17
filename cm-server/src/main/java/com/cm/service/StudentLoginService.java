package com.cm.service;

import com.cm.dto.StudentLoginDTO;
import com.cm.vo.StudentLoginVO;

/**
 * @author 31373
 */
public interface StudentLoginService {

    /**
     * 管理员登录
     *
     * @param studentLoginDTO 管理员登录参数
     * @return 管理员登录返回的token
     */
    StudentLoginVO login(StudentLoginDTO studentLoginDTO);

    /**
     * 管理员登出
     * @param currentId 当前登录管理员id
     */
    void logout(Long currentId);

}
