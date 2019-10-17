package com.gjw.common.innovation.service;

import com.gjw.common.innovation.repository.db1.domain.TUser;

/**
 * @author gaojunwei
 * @date 2019/10/14 20:00
 */
public interface TUserService {
    /**
     * 保存用户
     */
    TUser insert(TUser user);

    /**
     * 保存用户
     */
    TUser getById(Long id);
}