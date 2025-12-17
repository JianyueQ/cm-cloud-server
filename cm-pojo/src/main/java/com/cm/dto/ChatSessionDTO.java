package com.cm.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatSessionDTO implements Serializable {

    /**
     * 会话类型（1:私聊, 2:群聊）
     */
    @NotNull(message = "会话类型不能为空")
    private Integer sessionType;

    /**
     * 会话名称（群聊时使用）
     */
    private String sessionName;
    /**
     * 会话头像（群聊时使用）
     */
    private String avatar;
    /**
     * 发言状态(0:全体禁言,1:正常发言)
     */
    private Integer speakStatus;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 会话内昵称
     */
    private String nickname;

    /**
     * 用户类型
     */
    private Integer userType;

    @AssertTrue(message = "会话类型不正确")
    public boolean isSessionTypeValid() {
        return sessionType != null && (sessionType == 1 || sessionType == 2);
    }

    @AssertTrue(message = "会话名称不能为空")
    public boolean isSessionNameValid() {
        if (sessionType != null && sessionType == 2) {
            return sessionName != null && !sessionName.trim().isEmpty();
        }
        return true;
    }



    @AssertTrue(message = "会话头像不能为空")
    public boolean isAvatarValid() {
        if (sessionType != null && sessionType == 2) {
            return avatar != null && !avatar.trim().isEmpty();
        }
        return true;
    }

    @AssertTrue(message = "发言状态不能为空")
    public boolean isSpeakStatusValid() {
        if (sessionType != null && sessionType == 2) {
            return speakStatus != null;
        }
        return true;
    }

    @AssertTrue(message = "发言状态类型不正确")
    public boolean isSpeakStatusTypeValid() {
        if (sessionType != null && sessionType == 2) {
            return speakStatus != null && (speakStatus == 0 || speakStatus == 1);
        }
        return true;
    }

    @AssertTrue(message = "用户ID不能为空")
    public boolean isUserIdValid() {
        if (sessionType != null && sessionType == 1) {
            return userId != null;
        }
        return true;
    }

    @AssertTrue(message = "用户类型不能为空")
    public boolean isUserTypeValid() {
        if (sessionType != null && sessionType == 1) {
            return userType != null;
        }
        return true;
    }

    @AssertTrue(message = "用户类型不正确")
    public boolean isUserTypeCorrect() {
        return userType != null && (userType == 1 || userType == 2);
    }
}
