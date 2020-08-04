package com.jdjr.crawler.tcpj.service.impl;

import com.jdjr.crawler.tcpj.repository.LoginDataRepository;
import com.jdjr.crawler.tcpj.service.LoginDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/8/4 13:46
 **/
@Service
@Slf4j
public class LoginDataServiceImpl implements LoginDataService {

    @Resource
    private LoginDataRepository loginDataRepository;

    @Override
    public void invalidToken(String account, String site, String remark) {
        updateUseful(9, remark, account, site);
    }

    @Override
    public void validToken(String account, String site, String remark) {
        updateUseful(0, remark, account, site);
    }

    private void updateUseful(Integer useful, String remark, String account, String site) {
        try {
            loginDataRepository.updateUseful(useful, remark, account, site);
        } catch (Exception e) {
            logger.error("{}", e.getMessage(), e);
        }
    }
}
