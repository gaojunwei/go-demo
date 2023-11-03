package com.gjw.innovation.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@TableName("wx_user")
@Getter
@Setter
public class WxUser {
    /**
     * 用户ID
     */
    @TableId(value = "user_id")
    private Long userId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 微信openId
     */
    private String openId;
    /**
     * 微信sessionKey
     */
    private String sessionKey;
    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
    /**
     * 默认值0，0未删除，1已删除
     */
    private Integer delFlag;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
