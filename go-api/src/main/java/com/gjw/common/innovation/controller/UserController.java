package com.gjw.common.innovation.controller;

import com.gjw.common.enums.SystemCodeEnums;
import com.gjw.common.innovation.repository.domain.TUser;
import com.gjw.common.innovation.service.TUserService;
import com.gjw.common.result.SingleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author gaojunwei
 * @date 2019/10/14 20:04
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private TUserService tUserService;

    @RequestMapping("add")
    public SingleResult<TUser> addUser() {
        SingleResult<TUser> result = new SingleResult<>();
        TUser user = new TUser();
        user.setUserName("张三");
        user.setUserAccount("admin");
        user.setUserPwd("123456");

        user = tUserService.insert(user);
        SystemCodeEnums.SUCCESS.applyValue(result);
        result.setData(user);
        return result;
    }

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping("get")
    public SingleResult<TUser> getuser(@RequestParam Long id) {
        SingleResult<TUser> result = new SingleResult<>();
        TUser user = tUserService.getById(id);
        SystemCodeEnums.SUCCESS.applyValue(result);
        result.setData(user);
        return result;
    }
}