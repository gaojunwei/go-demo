package com.gjw.innovation.repository.domain.key;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: gaojunwei
 * @Date: 2018/12/12 15:40
 * @Description: 指令执行任务表 联合主键
 */
@Data
public class RolePowerKey implements Serializable {
    private Long roleId;
    private Long powerId;
}