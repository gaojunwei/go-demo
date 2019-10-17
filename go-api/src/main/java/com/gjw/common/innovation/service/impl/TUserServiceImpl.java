package com.gjw.common.innovation.service.impl;

import com.gjw.common.innovation.repository.db1.dao.entity.TUser;
import com.gjw.common.innovation.service.TUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author gaojunwei
 * @date 2019/10/14 20:02
 */
@Service
@Slf4j
public class TUserServiceImpl implements TUserService {
    @Autowired
    private com.gjw.common.innovation.repository.db1.dao.TUserRepository primaryTUserRepository;

    @Autowired
    private com.gjw.common.innovation.repository.db2.dao.TUserRepository secondaryTUserRepository;
    @Override
    public TUser insert(TUser user) {
        com.gjw.common.innovation.repository.db2.dao.entity.TUser user2 = new com.gjw.common.innovation.repository.db2.dao.entity.TUser();
        BeanUtils.copyProperties(user,user2);
        secondaryTUserRepository.save(user2);
        return primaryTUserRepository.save(user);
    }

    @Override
    public TUser getById(Long id) {
        Optional<TUser> optional = primaryTUserRepository.findById(id);
        if(optional.isPresent())
            return optional.get();
        return null;
    }
}