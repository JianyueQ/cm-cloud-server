package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatSessionMember implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 会话ID
     */
    private Long sessionId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户类型（1:学生, 2:教师, 3:管理员）
     */
    private Integer userType;

    /**
     * 会话内昵称
     */
    private String nickname;

    /**
     * 角色（1:普通成员, 2:管理员, 3:群主）
     */
    private Integer role;

    /**
     * 未读消息数
     */
    private Integer unreadCount;

    /**
     * 加入时间
     */
    private LocalDateTime joinTime;

    /**
     * 成员状态（0:已退出, 1:正常）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     */
    private Long createUser;

    /**
     * 更新人ID
     */
    private Long updateUser;

    /**
     * 删除标识（0:未删除, 1:已删除）
     */
    private Integer isDeleted;
}
