package com.test.validator;

import com.alibaba.fastjson.JSON;
import com.gjw.common.utils.ValidationUtil;

import java.util.Date;

/**
 * @author: gaojunwei
 * @Date: 2018/7/25 15:36
 * @Description:
 */
public class Main {
    public static void main(String[] args) {
        Param param = new Param();
        param.setAge(6);
        param.setLove(true);
        param.setBloodSugar(50.8);
        param.setCreatedDate(new Date());
        param.setVisaDate(new Date(new Date().getTime() + 1234234));
        param.setEmail("sfsdf");
        param.setEmailBack("gjw.5@qq.com");
        param.setMarryYears("2");
        param.setUrl("http://www.jd.com/ss.action");
        param.setUserName("上1");
        test002(param);
    }

    public static void test002(Param param) {
        // 参数验证
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(param);
        System.out.println(JSON.toJSONString(validResult));
    }
}