package com.test.mybatisplus;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjw.common.innovation.TApplication;
import com.gjw.common.innovation.entity.GoUser;
import com.gjw.common.innovation.service.IGoUserService;
import com.gjw.common.utils.UuidUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TApplication.class)
public class SpringTest {

    @Autowired
    IGoUserService goUserService;

    @Test
    public void test001() {
        //insert();
        //update();
        //delete();
        //getSingle();
        //listAll();
        //listByCon();
        listByConPage();
    }

    public void getSingle() {
        QueryWrapper<GoUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", "root");

        GoUser goUser = goUserService.getOne(queryWrapper);
        System.out.println("单个查询：" + JSON.toJSONString(goUser));
    }

    public void listAll() {
        List<GoUser> users = goUserService.list();
        System.out.println("查询全部数据：" + JSON.toJSONString(users));
    }

    public void listByCon() {
        QueryWrapper<GoUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("account", "root");
        List<GoUser> users = goUserService.list(queryWrapper);
        System.out.println("条件查询：" + JSON.toJSONString(users));
    }

    public void listByConPage() {
        //pageNo为0和1都是首页的意思
        Page<GoUser> page = new Page<>(0, 2);

        QueryWrapper<GoUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("account", "root");

        IPage<GoUser> userIPage = goUserService.page(page, queryWrapper);
        System.out.println("分页查询：" + JSON.toJSONString(userIPage));
    }

    public void delete() {
        String code = "a8cc9ca674804810a22c6cf5df838556";
        //删除条件
        QueryWrapper<GoUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);

        boolean result = goUserService.remove(queryWrapper);
        System.out.println("删除结果：" + result);
    }

    public void update() {
        String code = "a8cc9ca674804810a22c6cf5df838556";
        //修改值
        GoUser goUser = new GoUser();
        goUser.setModifiedDate(new Date());
        goUser.setAccount("admin");
        //修改条件
        UpdateWrapper<GoUser> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.set("code", code);

        boolean result = goUserService.update(goUser, userUpdateWrapper);
        System.out.println("修改结果：" + result);
    }

    public void insert() {
        GoUser goUser = new GoUser();
        goUser.setCode(UuidUtils.getUUID());
        goUser.setAccount("root2");
        goUser.setPassword("123456");
        goUser.setModifiedDate(new Date());
        goUser.setCreatedDate(new Date());

        boolean result = goUserService.save(goUser);
        System.out.println("保存结果：" + result);
    }
}