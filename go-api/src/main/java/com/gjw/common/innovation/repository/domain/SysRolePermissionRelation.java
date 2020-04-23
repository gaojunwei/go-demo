package com.gjw.common.innovation.repository.domain;

import com.gjw.common.innovation.repository.domain.key.SysRolePermissionRelationKey;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色与权限中间关系表
 */
@Data
@Entity
@Table(name = "sys_user_role_relation")
@IdClass(SysRolePermissionRelationKey.class)
public class SysRolePermissionRelation implements Serializable {
    /**
     * 角色ID
     */
    @Id
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 资源ID
     */
    @Id
    @Column(name = "permission_id")
    private Long permissionId;

    /**
     * 创建时间
     */
    @Column(name = "c_time")
    private Date cTime;
}