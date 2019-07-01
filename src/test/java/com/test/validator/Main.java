package com.test.validator;

import com.alibaba.fastjson.JSON;
import com.jd.innovation.common.utils.ValidatorHelper;

import java.util.Date;
import java.util.List;

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
        param.setVisaDate(new Date(new Date().getTime()+1234234));
        param.setEmail("gjw.5@qqcom");
        param.setEmailBack("gjw.5@qq..com");
        param.setMarryYears("2");
        param.setUrl("http://www.jd.com/ss.action");
        param.setUserName("上三");
        test(param);
    }
    public static void test(Param param){
        // 参数验证
        List<String> errors = ValidatorHelper.validate(param);
        System.out.println(JSON.toJSONString(errors));
    }
}