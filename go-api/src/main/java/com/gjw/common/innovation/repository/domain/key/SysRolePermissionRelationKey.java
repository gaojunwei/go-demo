package com.gjw.common.innovation.repository.domain.key;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaojunwei
 * @date 2019/11/6 9:00
 */
@Data
public class SysRolePermissionRelationKey implements Serializable {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 资源ID
     */
    private Long permissionId;

}