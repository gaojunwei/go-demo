package com.gjw.common.innovation.controller;

import com.alibaba.fastjson.JSON;
import com.gjw.common.enums.SystemCodeEnums;
import com.gjw.common.result.BasicResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wf.captcha.ChineseGifCaptcha;
import com.wf.captcha.utils.CaptchaUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("public")
@Slf4j
public class CommonController {
    @RequestMapping("mdc")
    public BasicResult one() {
        logger.info("MDC 链路方法测试Demo");
        BasicResult result = SystemCodeEnums.SUCCESS.applyValue();
        logger.info(JSON.toJSONString(result));
        return result;
    }

    @RequestMapping("exception")
    public BasicResult two() {
        logger.info("MDC 链路方法测试Demo-异常测试");
        BasicResult result = SystemCodeEnums.SUCCESS.applyValue();
        logger.info(JSON.toJSONString(result));
        int c = 10/0;
        return result;
    }

    /**
     * 图形验证码
     *
     * @param request
     * @param response
     */
    @RequestMapping("imgcode")
    public void imgCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //GifCaptcha captcha = new GifCaptcha();
        //ArithmeticCaptcha captcha = new ArithmeticCaptcha();
        ChineseGifCaptcha captcha = new ChineseGifCaptcha();
        System.out.println("生产系统验证码：" + captcha.text());
        System.out.println("生产系统验证码Base64：" + captcha.toBase64());
        CaptchaUtil.out(captcha, request, response);
    }
}
