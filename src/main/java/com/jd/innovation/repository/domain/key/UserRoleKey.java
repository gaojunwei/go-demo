package com.jd.innovation.repository.domain.key;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: gaojunwei
 * @Date: 2018/12/12 15:40
 * @Description: 指令执行任务表 联合主键
 */
@Data
public class UserRoleKey implements Serializable {
    private Long userId;
    private Long roleId;
}