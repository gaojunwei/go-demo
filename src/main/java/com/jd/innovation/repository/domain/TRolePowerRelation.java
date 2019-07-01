package com.jd.innovation.repository.domain;

import com.jd.innovation.repository.domain.key.RolePowerKey;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.IdClass;

/**
 * 角色资源表
 */
@Data
@Entity
@IdClass(RolePowerKey.class)
public class TRolePowerRelation {
    private Long roleId;
    private Long powerId;
}