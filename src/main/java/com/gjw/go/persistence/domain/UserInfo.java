package com.gjw.go.persistence.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ext.gaojunwei1
 * @date 2023/5/29
 */
@Setter
@Getter
public class UserInfo {
    //用户ID
    @TableId
    private Long userId;
    //用户名称
    private String userName;
    //用户年龄
    private Integer userAge;
    //数据来源
    private String fromSource;
}
