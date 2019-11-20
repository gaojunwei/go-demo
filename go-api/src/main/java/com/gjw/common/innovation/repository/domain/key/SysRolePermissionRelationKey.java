package com.gjw.common.innovation.repository.domain.key;

import lombok.Data;

/**
 * @author gaojunwei
 * @date 2019/11/6 9:00
 */
@Data
public class SysRolePermissionRelationKey {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 资源ID
     */
    private Long permissionId;

}