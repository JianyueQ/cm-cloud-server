package com.cm.dto;

import com.cm.entity.Page;
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
public class ChatSessionListDTO extends Page implements Serializable {

    /**
     * 会话名称
     */
    private String sessionName;

    /**
     * 会话内昵称
     */
    private String nickname;

    /**
     * 用户id
     */
    private Long userId;

}
