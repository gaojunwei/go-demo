package com.gjw.common.innovation.service.impl;

import com.gjw.common.innovation.repository.TUserRepository;
import com.gjw.common.innovation.repository.domain.TUser;
import com.gjw.common.innovation.service.TUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author gaojunwei
 * @date 2019/10/14 20:02
 */
@Service
@Slf4j
public class TUserServiceImpl implements TUserService {
    @Resource
    private TUserRepository tUserRepository;

    @Override
    public TUser insert(TUser user) {
        return tUserRepository.save(user);
    }

    @Override
    public TUser getById(Long id) {
        Optional<TUser> optional = tUserRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        return null;
    }

    @Override
    public TUser getByAccount(String userAccount) {
        TUser tUser = new TUser();
        tUser.setUserAccount(userAccount);
        Example<TUser> example = Example.of(tUser);
        Optional<TUser> tUserOptional = tUserRepository.findOne(example);
        return tUserOptional.get();
    }
}