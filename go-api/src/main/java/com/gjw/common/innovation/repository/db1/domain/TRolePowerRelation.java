package com.gjw.common.innovation.repository.db1.domain;

import com.gjw.common.innovation.repository.db1.domain.key.RolePowerKey;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * 角色资源表
 */
@Data
@Entity
@IdClass(RolePowerKey.class)
public class TRolePowerRelation {
    @Id
    private Long id;
    private Long roleId;
    private Long powerId;
}