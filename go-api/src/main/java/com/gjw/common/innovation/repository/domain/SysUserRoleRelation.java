package com.gjw.common.innovation.repository.domain;

import com.gjw.common.innovation.repository.domain.key.SysUserRoleRelationKey;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author gaojunwei
 */
@Data
@Entity
@Table(name = "sys_user_role_relation")
@IdClass(SysUserRoleRelationKey.class)
public class SysUserRoleRelation implements Serializable {

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "c_time")
    private Date cTime;
}