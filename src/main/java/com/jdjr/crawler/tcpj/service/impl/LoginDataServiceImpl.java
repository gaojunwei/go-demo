package com.jdjr.crawler.tcpj.service.impl;

import com.jdjr.crawler.tcpj.repository.LoginDataRepository;
import com.jdjr.crawler.tcpj.repository.domain.LoginData;
import com.jdjr.crawler.tcpj.service.LoginDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 条件查询数据
     */
    public List<LoginData> selectByCon(LoginData loginData) {
        if (loginData == null)
            return loginDataRepository.findAll();

        //获取指定站点下的所有账号Token信息
        List<LoginData> loginDataList = loginDataRepository.findAll((Specification<LoginData>) (root, criteriaQuery, cb) -> {
            List<Predicate> list1 = new ArrayList<>();
            if (!StringUtils.isEmpty(loginData.getSite()))
                list1.add(cb.equal(root.get("site").as(String.class), loginData.getSite()));
            if (loginData.getType() != null)
                list1.add(cb.equal(root.get("type").as(Integer.class), loginData.getType()));
            if (!StringUtils.isEmpty(loginData.getAccount()))
                list1.add(cb.equal(root.get("account").as(String.class), loginData.getAccount()));
            if (loginData.getIsUsed() != null)
                list1.add(cb.equal(root.get("isUsed").as(Integer.class), loginData.getIsUsed()));
            if (loginData.getUseful() != null)
                list1.add(cb.equal(root.get("useful").as(Integer.class), loginData.getUseful()));
            Predicate[] predicates = new Predicate[list1.size()];
            return cb.and(list1.toArray(predicates));
        });
        return loginDataList;
    }
}
