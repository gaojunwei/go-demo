package com.jdjr.crawler.tcpj.service.impl;

import com.alibaba.fastjson.JSON;
import com.jdjr.crawler.tcpj.common.enums.BusinessEnums;
import com.jdjr.crawler.tcpj.common.enums.SystemCodeEnums;
import com.jdjr.crawler.tcpj.common.exception.AppException;
import com.jdjr.crawler.tcpj.repository.LoginDataRepository;
import com.jdjr.crawler.tcpj.repository.TaskLogRepository;
import com.jdjr.crawler.tcpj.repository.UserAccountRepository;
import com.jdjr.crawler.tcpj.repository.domain.LoginData;
import com.jdjr.crawler.tcpj.repository.domain.TaskLog;
import com.jdjr.crawler.tcpj.repository.domain.UserAccount;
import com.jdjr.crawler.tcpj.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/29 10:57
 **/
@Service
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {
    @Resource
    private UserAccountRepository userAccountRepository;
    @Resource
    private LoginDataRepository loginDataRepository;
    @Resource
    private TaskLogRepository taskLogRepository;

    private Object lock = new Object();

    @Override
    public List<UserAccount> getAll(BusinessEnums siteEnum) {
        if (siteEnum == null)
            throw new AppException(SystemCodeEnums.ERROR.getCode(), "siteEnum 参数不能为空");

        UserAccount userAccount = new UserAccount();
        userAccount.setSite(siteEnum.getValue());
        return listUserByCon(userAccount);
    }

    @Override
    public void saveToken(LoginData param) {
        synchronized (lock) {
            if (param == null || StringUtils.isEmpty(param.getAccount()) || StringUtils.isEmpty(param.getSite()) || StringUtils.isEmpty(param.getToken()) || param.getType() == null) {
                logger.info("saveToken param:{}", JSON.toJSONString(param));
                throw new AppException(SystemCodeEnums.ERROR.getCode(), "必要参数不能为空");
            }
            List<LoginData> loginDataList = selectByCon(param.getSite(), null, param.getAccount(), null,null);
            //该账户token信息不存在则新增
            if (loginDataList == null || loginDataList.isEmpty()) {
                if (param.getTimeStamp() == null)
                    param.setTimeStamp(new Date());
                if (param.getIsUsed() == null)
                    param.setIsUsed(0);
                LoginData r = loginDataRepository.saveAndFlush(param);
                logger.info("新增TOKEN：{}", JSON.toJSONString(r));
            } else {//已存在Token数据，更新时间戳，继承老Token的使用状态
                LoginData oldData = loginDataList.get(0);
                param.setIsUsed(oldData.getIsUsed());
                param.setTimeStamp(new Date());
                LoginData r = loginDataRepository.saveAndFlush(param);
                logger.info("更新TOKEN：{}", JSON.toJSONString(r));
            }
        }
    }

    @Override
    public LoginData getToken(BusinessEnums siteEnum, Integer phoneType) {
        synchronized (lock) {
            if (siteEnum == null)
                throw new AppException(SystemCodeEnums.ERROR.getCode(), "siteEnum 参数不能为空");
            if (phoneType == null)
                throw new AppException(SystemCodeEnums.ERROR.getCode(), "phoneType 参数不能为空");
            //获取指定站点下的所有未使用账号Token信息
            List<LoginData> loginDataList = selectByCon(siteEnum.getValue(), phoneType, null, 0,0);
            if (loginDataList == null || loginDataList.isEmpty()) {
                //若果已全部使用过，则全部置为未使用状态0,并取第一个Token进行返回
                int count = loginDataRepository.unUsedSet(siteEnum.getValue(),phoneType);
                logger.info("按站点+账号类型设置全部账号为未使用状态 {},影响行数：{}", siteEnum.getValue(), count);

                //再次过滤出指定类型账户
                List<LoginData> list = selectByCon(siteEnum.getValue(), phoneType, null, 0,0);
                if (list == null || list.isEmpty())
                    return null;
                LoginData loginData = list.get(0);
                loginData.setIsUsed(1);
                loginDataRepository.saveAndFlush(loginData);
                return loginData;
            }
            //取第一个
            LoginData loginData = loginDataList.get(0);
            loginData.setIsUsed(1);
            loginDataRepository.saveAndFlush(loginData);
            return loginData;
        }
    }

    @Override
    public List<LoginData> getAllToken(BusinessEnums siteEnum) {
        return selectByCon(siteEnum.getValue(), null, null, null,0);
    }

    @Override
    public void saveTaskLog(String site, String logStr) {
        if (StringUtils.isEmpty(site) || StringUtils.isEmpty(logStr))
            return;
        try {
            TaskLog taskLog = new TaskLog();
            taskLog.setSite(site);
            taskLog.setLogStr(logStr);
            taskLogRepository.save(taskLog);
        } catch (Exception e) {
            logger.warn("任务执行日志记录失败");
        }
    }

    @Override
    public void clearExpiredData(BusinessEnums businessEnums, Date timePoint) {
        loginDataRepository.deleteExpiredData(businessEnums.getValue(), timePoint);
    }

    @Override
    public int updateAccountCodeInfo(String site, String account, String code, String msg) {
        return userAccountRepository.updateAccountCodeInfo(site,account,code,msg,new Date());
    }

    /**
     * 条件查询数据
     */
    private List<LoginData> selectByCon(String site, Integer type, String account, Integer isUsed,Integer useful) {
        //获取指定站点下的所有账号Token信息
        List<LoginData> loginDataList = loginDataRepository.findAll((Specification<LoginData>) (root, criteriaQuery, cb) -> {
            List<Predicate> list1 = new ArrayList<>();
            if (!StringUtils.isEmpty(site))
                list1.add(cb.equal(root.get("site").as(String.class), site));
            if (type != null)
                list1.add(cb.equal(root.get("type").as(Integer.class), type.intValue()));
            if (!StringUtils.isEmpty(account))
                list1.add(cb.equal(root.get("account").as(String.class), account));
            if (isUsed != null)
                list1.add(cb.equal(root.get("isUsed").as(Integer.class), isUsed));
            if (useful != null)
                list1.add(cb.equal(root.get("useful").as(Integer.class), useful));
            Predicate[] predicates = new Predicate[list1.size()];
            return cb.and(list1.toArray(predicates));
        });
        return loginDataList;
    }

    /**
     * 根据条件查询用户列表信息
     */
    @Override
    public List<UserAccount> listUserByCon(UserAccount userAccount){
        if(userAccount == null)
            return userAccountRepository.findAll();

        List<UserAccount> list = userAccountRepository.findAll((Specification<UserAccount>) (root, criteriaQuery, cb) -> {
            List<Predicate> list1 = new ArrayList<>();
            if(!StringUtils.isEmpty(userAccount.getSite()))
                list1.add(cb.equal(root.get("site").as(String.class), userAccount.getSite()));
            if(!StringUtils.isEmpty(userAccount.getCode()))
                list1.add(cb.equal(root.get("code").as(String.class), userAccount.getCode()));
            list1.add(cb.equal(root.get("delete_flag").as(Integer.class), 0));//未删除状态
            Predicate[] predicates = new Predicate[list1.size()];
            return cb.and(list1.toArray(predicates));
        });
        return list;
    }

}
