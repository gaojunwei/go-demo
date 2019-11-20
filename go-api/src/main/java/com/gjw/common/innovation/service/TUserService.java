package com.gjw.common.innovation.service;

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
     * 通过用户ID获取用户
     */
    TUser getById(Long id);

    /**
     * 通过用户账号或该用户
     */
    TUser getByAccount(String userAccount);
}