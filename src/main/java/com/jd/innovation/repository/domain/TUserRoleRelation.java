package com.jd.innovation.repository.domain;

import com.jd.innovation.repository.domain.key.UserRoleKey;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.IdClass;

/**
 * @author: gaojunwei
 * @Date: 2019/7/1 18:39
 * @Description:
 */
@Data
@Entity
@IdClass(UserRoleKey.class)
public class TUserRoleRelation {
    private Long userId;
    private Long roleId;
}