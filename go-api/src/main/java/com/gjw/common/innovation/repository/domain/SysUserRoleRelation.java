package com.gjw.common.innovation.repository.domain;

import com.gjw.common.innovation.repository.domain.key.SysUserRoleRelationKey;
import lombok.Data;

import javax.persistence.*;
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

    @Id
    @Column(name = "role_id")
    private Long roleId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "c_time")
    private Date cTime;
}