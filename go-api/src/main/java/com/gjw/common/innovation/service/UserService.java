package com.gjw.common.innovation.service;

import com.gjw.common.innovation.domain.UserInfo;

import java.util.List;

public interface UserService {
    UserInfo getById(Long id);
    List<UserInfo> listByCom(Long id,String name);
}
