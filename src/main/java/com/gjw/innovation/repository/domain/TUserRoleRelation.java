package com.gjw.innovation.repository.domain;

import com.gjw.innovation.repository.domain.key.UserRoleKey;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
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
    @Id
    private Long id;
    private Long userId;
    private Long roleId;
}