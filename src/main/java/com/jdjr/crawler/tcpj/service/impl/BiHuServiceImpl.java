package com.jdjr.crawler.tcpj.service.impl;

import com.alibaba.fastjson.JSON;
import com.jdjr.crawler.tcpj.common.enums.SystemCodeEnums;
import com.jdjr.crawler.tcpj.common.exception.AppException;
import com.jdjr.crawler.tcpj.common.util.ChromeDriverUtils;
import com.jdjr.crawler.tcpj.common.util.ThreadSleepUtils;
import com.jdjr.crawler.tcpj.service.BiHuService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Set;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/28 9:10
 **/
@Service
@Slf4j
public class BiHuServiceImpl implements BiHuService {

    private final String accessToken = "token";

    @Override
    public String getLoginToken(String url, String account, String password) {
        logger.info("start request param url:{},account:{},password:{}", url, account, password);
        //获取浏览器对象
        WebDriver driver = ChromeDriverUtils.getInstance();
        try {
            //通过driver控制浏览器打开链接（url）
            openUrl(driver, url);
            logger.info("driver open url success {}", url);
            //获取手机号输入框
            WebElement phoneInput = driver.findElement(By.cssSelector("input[placeholder='请填写手机号码']"));
            phoneInput.sendKeys(account);
            logger.info("phoneInput success {}", account);
            ThreadSleepUtils.sleepMS(500);
            //获取手机号输入框
            WebElement pwdInput = driver.findElement(By.cssSelector("input[placeholder='至少 8 位 (包含字母和数字)']"));
            pwdInput.sendKeys(password);
            logger.info("pwdInput success {}", password);
            ThreadSleepUtils.sleep(5);
            WebElement logInBtnBig = driver.findElement(By.cssSelector("button[class='el-button bh-button bh-button__huge operation-btn login-btn bh-button__primary el-button--default']"));
            logInBtnBig.click();
            logger.info("logInBtnBig was clicked success");
            //判断是否登录成功
            if (!checkIsSuccess(driver)) {
                return null;
            }
            //获取登录后的cookie信息
            String access_token = getToken(driver);
            if (StringUtils.isEmpty(access_token)) {
                return null;
            }
            try {
                String jsonStr = URLDecoder.decode(access_token, "utf-8");
                String jwt = JSON.parseObject(jsonStr).getString("jwt");
                logger.info("bihu jwt:{}", jwt);
                return jwt;
            } catch (UnsupportedEncodingException e) {
                logger.error("token decode Exception:{}", e.getMessage(), e);
                return null;
            }
        } finally {
            //关闭浏览器
            if (driver != null) {
                driver.quit();
            }
        }
    }

    /**
     * 检测是否登录成功
     *
     * @param driver
     * @return
     */
    private Boolean checkIsSuccess(WebDriver driver) {
        ThreadSleepUtils.sleep(5);
        int count = 30;
        int retryTimes = 1;
        boolean flag = false;
        while (true) {
            ThreadSleepUtils.sleep(1);
            try {
                List<WebElement> items = driver.findElements(By.cssSelector("div[class='row']"));
                logger.info("BIHU checkIsSuccess:{}", items.size());
                if (items != null && items.size() > 5) {
                    flag = true;
                    break;
                }
            } catch (Exception e) {
                logger.info("BIHU checkIsSuccess {} times", retryTimes);
            }
            retryTimes++;
            if (retryTimes >= count) {
                break;
            }
        }
        logger.info("BIHU_checkIsSuccess_checkResult:{}", flag);
        return flag;
    }

    /**
     * 打开URL地址
     */
    private void openUrl(WebDriver driver, String url) {
        driver.get(url);
        int count = 0;
        while (true) {
            ThreadSleepUtils.sleep(1);
            try {
                //找到登录按钮元素
                WebElement logInBtn = driver.findElement(By.cssSelector("button[class='el-button bh-button login-btn el-button--default']"));
                if (logInBtn != null) {
                    logger.info("open url complete success...GO GO GO");
                    logInBtn.click();
                    logger.info("toLogInBtn success click");
                    ThreadSleepUtils.sleep(5);
                    WebElement logInBtnSmall = driver.findElement(By.cssSelector("button[class='el-button bh-button bh-button__huge operation-btn captcha-login-btn bh-button__green el-button--default']"));
                    logInBtnSmall.click();
                    logger.info("use_pwd_toLogInBtn success click");
                    ThreadSleepUtils.sleep(3);
                    WebElement logInBtnBig = driver.findElement(By.cssSelector("button[class='el-button bh-button bh-button__huge operation-btn login-btn bh-button__primary el-button--default']"));
                    logger.info("logInBtnBig was find ... success !!!");
                    break;
                }
            } catch (Exception e) {
                logger.info("bihu driver_open url checking... {}", count);
            }
            count++;
            //每达到100s刷新该tab
            if ((count % 100) == 0) {
                driver.navigate().refresh();
            }
            //达到最大检测次数，
            if (count > 1000) {
                throw new AppException(SystemCodeEnums.ERROR.getCode(), "open url fail, reason reach_max_retry_times:" + 1000);
            }
        }
    }

    /**
     * 获取Token值
     *
     * @param driver
     * @return
     */
    private String getToken(WebDriver driver) {
        int checkTimes = 20;
        for (int i = 1; i <= checkTimes; i++) {
            ThreadSleepUtils.sleep(1);
            String token = getCookie(driver, accessToken);
            logger.info("bihu attempt get token {}/{} token:{}", i, checkTimes, token);
            if (!StringUtils.isEmpty(token)) {
                return token;
            }
        }
        return null;
    }

    /**
     * 从cookies中获取指定key的值
     */
    private String getCookie(WebDriver driver, String cookieKey) {
        Set<Cookie> cookies = driver.manage().getCookies();
        if (cookies == null || cookies.isEmpty()) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieKey)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
