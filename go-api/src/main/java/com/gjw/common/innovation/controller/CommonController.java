package com.gjw.common.innovation.controller;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.ChineseGifCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/public")
public class CommonController {
    /**
     * 图形验证码
     * @param request
     * @param response
     */
    @RequestMapping("imgcode")
    public void imgCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //GifCaptcha captcha = new GifCaptcha();
        //ArithmeticCaptcha captcha = new ArithmeticCaptcha();
        ChineseGifCaptcha captcha = new ChineseGifCaptcha();
        System.out.println("生产系统验证码："+captcha.text());
        CaptchaUtil.out(captcha,request,response);
    }
}
