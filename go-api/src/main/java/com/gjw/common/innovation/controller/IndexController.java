package com.gjw.common.innovation.controller;

import com.gjw.common.enums.SystemCodeEnums;
import com.gjw.common.result.BasicResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: gaojunwei
 * @Date: 2019/7/2 19:53
 * @Description:
 */
@Controller
@RequestMapping("/")
@Slf4j
public class IndexController {

    @RequestMapping("")
    public String index(Model model){
        model.addAttribute("msg", "Hello World!");
        Map<String,String> data1 = new HashMap<>();
        data1.put("name","小明");
        data1.put("age","12");

        Map<String,String> data2 = new HashMap<>();
        data2.put("name","钢蛋");
        data2.put("age","10");

        model.addAttribute("list", Arrays.asList(data1,data2));
        logger.info("返回页面");
        return "index";
    }

    @RequestMapping("/index")
    @ResponseBody
    public BasicResult test(){
        BasicResult result = BasicResult.instance(SystemCodeEnums.SUCCESS.getCode(),SystemCodeEnums.SUCCESS.getMsg());
        logger.info("返回JSON格式数据");
        return result;
    }
}