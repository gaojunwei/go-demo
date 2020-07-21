package com.jdjr.crawler.tcpj.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jdjr.crawler.tcpj.common.enums.SystemCodeEnums;
import com.jdjr.crawler.tcpj.common.exception.AppException;
import com.jdjr.crawler.tcpj.common.util.Arith;
import com.jdjr.crawler.tcpj.common.util.ChromeDriverUtils;
import com.jdjr.crawler.tcpj.common.util.SpringUtils;
import com.jdjr.crawler.tcpj.common.util.UuidUtils;
import com.jdjr.crawler.tcpj.config.SysConfig;
import com.jdjr.crawler.tcpj.service.TCPJService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/15 14:20
 **/
@Service
@Slf4j
public class TCPJServiceImpl implements TCPJService {

    private SysConfig sysConfig = (SysConfig) SpringUtils.getBean("sysConfig");
    private final String accessToken = "access_token";

    @Override
    public String getLoginToken(String url, String account, String password) {
        long startTime = System.currentTimeMillis();
        logger.info("start request param url:{},account:{},password:{}", url, account, password);
        //获取浏览器对象
        WebDriver driver = ChromeDriverUtils.getInstance();
        try {
            //通过driver控制浏览器打开链接（url）
            openUrl(driver, url);
            logger.info("driver_open step");
            removeCookie(driver, accessToken);

            System.out.println("测试cookie A获取acw_tc:" + getCookie(driver, "acw_tc"));
            System.out.println("测试cookie 删除acw_tc");
            removeCookie(driver, "acw_tc");
            System.out.println("测试cookie B获取acw_tc:" + getCookie(driver, "acw_tc"));


            //输入手机号
            WebElement phoneInput = driver.findElement(By.cssSelector("input[class='ant-input login-input']"));
            phoneInput.sendKeys(account);
            logger.info("driver_key_phone step");
            sleep(1);
            //输入密码
            WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));
            passwordInput.sendKeys(password);
            logger.info("driver_key_password step");
            sleep(1);
            //找到滑块元素
            WebElement slider = driver.findElement(By.cssSelector("span#nc_1_n1z"));
            logger.info("driver_find slider step");
            //滑动滑块
            doSlider(driver, slider);
            logger.info("driver_find doSlider step");
            //点击登录按钮
            WebElement logInBtn = driver.findElement(By.cssSelector("button[class='ant-btn login-button ant-btn-primary']"));
            logger.info("driver_find logInBtn step");
            logInBtn.click();
            logger.info("driver_click logInBtn step");
            sleep(5);
            /** 点击图形验证码处理 */
            int count = 1;
            boolean isSuccess = false;
            //切换到iframe窗口
            try {
                if (!witchToFrame(driver)) {
                    logger.info("driver_switchTo frame step fail!!!");
                    throw new AppException(SystemCodeEnums.ERROR.getCode(), "witchToFrame fail");
                }
            } catch (AppException e) {
                if (e.getCode() == SystemCodeEnums.SUCCESS.getCode()) {
                    logger.info("after_login_success,no imgCode check end：access_token-{}", e.getMsg());
                    return e.getMsg();
                }
                throw e;
            }

            logger.info("driver_switchTo frame success step");
            while (count <= sysConfig.getTcpjTcaptchaReTryTimes()) {
                try {
                    isSuccess = doTcaptcha(driver);
                } catch (Exception e) {
                    logger.error("driver_imgCode exception {} times,errorMsg:{},", count, e.getMessage(), e);
                }
                logger.info("driver_imgCode operate result：{},{} times", isSuccess, count);
                //点击失败刷新验证码
                if (!isSuccess) {
                    logger.info("driver_imgCode operate fail - do retry, {} times.", count);
                    WebElement reloadEle = driver.findElement(By.id("reload"));
                    reloadEle.click();
                    sleep(2);
                } else {
                    break;
                }
                count++;
            }
            if (!isSuccess) {
                String msg = "验证码点击失败达到最大重试次数" + sysConfig.getTcpjTcaptchaReTryTimes();
                logger.info("driver_imgCode end get max retryTimes {},需要人工检测网址是否改版或反爬", sysConfig.getTcpjTcaptchaReTryTimes());
                throw new AppException(SystemCodeEnums.ERROR.getCode(), msg);
            }
            logger.info("driver_imgCode click SUCCESS!!!!!!!");
            //获取登录后的cookie信息
            String access_token = getToken(driver);
            ;
            logger.info("after_login_success,get Cookie info step：access_token-{}", access_token);
            return access_token;
        } finally {
            //关闭浏览器
            if (driver != null) {
                driver.quit();
                long endTime = System.currentTimeMillis();
                logger.info("driver_close step... cost:{}m", ((endTime - startTime) / 1000));
            }
        }
    }

    /**
     * 切换Frame
     *
     * @return
     */
    private Boolean witchToFrame(WebDriver driver) {
        Boolean result = false;
        for (int i = 1; i <= 20; i++) {
            try {
                driver.switchTo().frame("tcaptcha_iframe");
                result = true;
                break;
            } catch (Exception e) {
                logger.info("driver_frame witchTo checking... {} times", i);
                sleep(1);
            }
            //检测是否已经登录验证
            String token = getCookie(driver, accessToken);
            if (!StringUtils.isEmpty(token)) {
                logger.info("after_login_success,no imgCode check end：access_token-{}", token);
                throw new AppException(SystemCodeEnums.SUCCESS.getCode(), token);
            }
        }
        return result;
    }

    /**
     * 打开URL地址
     */
    private void openUrl(WebDriver driver, String url) {
        driver.get(url);
        int count = 0;
        while (true) {
            sleep(1);
            try {
                //找到登录按钮元素
                WebElement logInBtn = driver.findElement(By.cssSelector("button[class='ant-btn login-button ant-btn-primary']"));
                //找到手机号框元素
                WebElement phoneInput = driver.findElement(By.cssSelector("input[class='ant-input login-input']"));
                //找到密码框元素
                WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));
                //找到滑块元素
                WebElement slider = driver.findElement(By.cssSelector("span#nc_1_n1z"));

                if (logInBtn != null && phoneInput != null && passwordInput != null && slider != null) {
                    logger.info("open url complete success...GO GO GO");
                    break;
                }
            } catch (Exception e) {
                logger.info("driver_open url checking...");
            }
            count++;
            //每达到100s刷新该tab
            if ((count % 100) == 0) {
                driver.navigate().refresh();
            }
            //达到最大检测次数，
            if (count > sysConfig.getTcpjOpenLoginUrlTimes()) {
                throw new AppException(SystemCodeEnums.ERROR.getCode(), "open url fail, reason reach_max_retry_times:" + sysConfig.getTcpjOpenLoginUrlTimes().toString());
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
        int checkTimes = 10;
        for (int i = 1; i <= checkTimes; i++) {
            sleep(1);
            String token = getCookie(driver, accessToken);
            logger.info("attempt get token {}/{} token:{}", i, checkTimes, token);
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

    /**
     * 删除指定的cookie
     */
    private void removeCookie(WebDriver driver, String cookieKey) {
        driver.manage().deleteCookieNamed(cookieKey);
    }

    /**
     * 图形验证码，点击处理，含重试逻辑
     *
     * @return
     */
    private boolean doTcaptcha(WebDriver driver) throws IOException {
        //获取图形验证码的点击坐标值
        Integer x, y;
        //获取图片元素及提示信息元素
        if (!checkingImgCodeExist(driver)) {
            throw new AppException(SystemCodeEnums.ERROR.getCode(), "图形验证码加载失败");
        }
        WebElement imgEle = driver.findElement(By.id("tcaptcha-img"));
        WebElement imgDescEle = driver.findElement(By.cssSelector("p[class='tcaptcha-title']"));
        String imgSrc = imgEle.getAttribute("src");
        String imgDescStr = imgDescEle.getText();

        String coordinate = getCoordinate(imgSrc, imgDescStr);
        if (StringUtils.isEmpty(coordinate)) {
            logger.info("get coordinate fail step");
            return false;
        }
        String[] coordinates = coordinate.split(",");
        x = Integer.parseInt(coordinates[0]);
        y = Integer.parseInt(coordinates[1]);
        //根据坐标点击图形验证码
        Actions action = new Actions(driver);
        action.moveToElement(imgEle, x, y).click();
        action.release().perform();
        logger.info("imgCode click step");
        sleepMS(800);
        //判断是否点击图形验证码成功过
        try {
            WebElement tcaptchaNoteEle = driver.findElement(By.id("tcaptcha_note"));
            if (!tcaptchaNoteEle.getText().trim().equals("")) {
                logger.info("imgCode click fail step reason：{}", tcaptchaNoteEle.getText());
                return false;
            } else {
                logger.info("imgCode errorMsg check pass step");
            }
            //如果点击完20秒后图片元素还在认为点击失败
            int maxtimes = 20;
            for (int i = 1; i <= maxtimes; i++) {
                WebElement tcaptchaEle;
                try {
                    tcaptchaEle = driver.findElement(By.id("tcaptcha-img"));
                } catch (Exception e) {
                    tcaptchaEle = null;
                }
                if (tcaptchaEle == null) {
                    logger.info("imgCode after click,not exist step true");
                    return true;
                }
                logger.info("imgCode after click checking {} times", i);
                sleep(1);
            }
            logger.info("imgCode after click,after maxChecking {} times click fail", maxtimes);
            return false;
        } catch (Exception e) {
            logger.info("获取点击描述异常 {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 检测图片验证码是否加载成功
     *
     * @param driver
     * @return
     */
    public Boolean checkingImgCodeExist(WebDriver driver) {
        boolean result = false;
        int checkTimes = 100;
        for (int i = 1; i <= checkTimes; i++) {
            try {
                WebElement imgEle = driver.findElement(By.id("tcaptcha-img"));
                if (imgEle != null) {
                    logger.info("imgCode loading success!!! {} times", i);
                    result = true;
                    break;
                } else {
                    throw new AppException("", "");
                }
            } catch (Exception e) {
                logger.info("imgCode loading checking {} times", i);
                sleep(1);
            }
        }
        if (!result)
            logger.info("imgCode loading checking fail--!!! {} times", checkTimes);
        return result;
    }


    /**
     * 获取点击坐标值
     */
    private String getCoordinate(String imgSrc, String imgDescStr) throws IOException {
        try {
            logger.info("imgUrl、imgDesc step imgDesc：{},imgUrl：{}", imgDescStr, imgSrc);
            //拼接生成新图片的base64字符
            String base64Str = getImgBase64(imgSrc, imgDescStr);
            if (StringUtils.isEmpty(base64Str)) {
                throw new NullPointerException("base64Str can not be null or empty");
            }
            //调用打码平台获取点击坐标
            String resultStr = chaoJiYing(base64Str);
            JSONObject jsonObject = JSON.parseObject(resultStr);
            if (jsonObject.getIntValue("err_no") != 0 || StringUtils.isEmpty(jsonObject.getString("pic_str"))) {
                logger.error("call cjy service fail step ,responseStr:{}", resultStr);
                return null;
            }
            return jsonObject.getString("pic_str");
        } catch (Exception e) {
            logger.error("tcpj execute exception step {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 图片下载，拼接文字，返回base64字符
     *
     * @param imgSrc
     * @param imgDescStr
     * @return
     */
    private String getImgBase64(String imgSrc, String imgDescStr) {
        //图片下载
        String base64Str = null;
        try {
            byte[] imgBts = downLoadImgData(imgSrc);
            if (imgBts == null || imgBts.length == 0) {
                return base64Str;
            }
            InputStream inputStream = new ByteArrayInputStream(imgBts);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            //图片进行缩放
            BufferedImage scaleImg = getScaleImg(bufferedImage);

            //获取图片的高度和宽度
            int width = scaleImg.getWidth();
            int height = scaleImg.getHeight();

            //设置文字图片信息
            int textHeight = 30;
            BufferedImage textImage = new BufferedImage(width, textHeight, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics graphics = textImage.getGraphics();
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.black);
            Font font = new Font("宋体", Font.BOLD, 18);
            graphics.setFont(font);
            graphics.drawString(imgDescStr, 1, 20);

            int[] textImageArray = new int[width * textHeight];
            textImageArray = textImage.getRGB(0, 0, width, textHeight, textImageArray, 0, width);

            /**生成新图片*/
            int[] bufferedImageArray = new int[width * height];
            bufferedImageArray = scaleImg.getRGB(0, 0, width, height, bufferedImageArray, 0, width);


            /**合并成新图片*/
            BufferedImage imageNew = new BufferedImage(width, height + textHeight, BufferedImage.TYPE_INT_RGB);
            //设置上半部分的RGB
            imageNew.setRGB(0, 0, width, height, bufferedImageArray, 0, width);
            //设置下半部分的RGB
            imageNew.setRGB(0, height, width, textHeight, textImageArray, 0, width);


            //获取图片base64字符串
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(imageNew, "jpg", stream);
            base64Str = new String(Base64.encodeBase64(stream.toByteArray()), "utf-8");

            try {
                File outDe = new File("D:/proxy/imgCode/");
                if (!outDe.exists())
                    outDe.mkdirs();
                //保存到本地
                File outFile = new File("D:/proxy/imgCode/" + UuidUtils.getUUID() + ".jpg");
                ImageIO.write(imageNew, "jpg", outFile);// 写图片
            } catch (Exception e) {
                logger.warn("警告：验证码保存到本地失败，不影响流程可忽略");
            }

        } catch (Exception e) {
            logger.info("img_operate step exception {}", e.getMessage(), e);
        }
        logger.info("img_operate_end step base64Str:{}", StringUtils.isEmpty(base64Str) ? "img operate fail" : "img operate success");
        return base64Str;
    }

    /**
     * 获取图片数据
     */
    private byte[] downLoadImgData(String url) {
        byte[] result = null;
        int reTryCount = 0;
        while (reTryCount < sysConfig.getTcpjDownloadImgRetryTimes().intValue()) {
            logger.info("downLoad_start step img {} times", reTryCount);
            HttpGet httpGet = new HttpGet(url);
            //设置代理及超时配置
            CloseableHttpClient httpClient = getHttpClient(httpGet, sysConfig.getConnectTimeout(), sysConfig.getSocketTimeout());

            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    break;
                }
            } catch (ClientProtocolException e) {
                logger.error("downLoad_exception step get img data exception {}", e.getMessage(), e);
            } catch (IOException e) {
                logger.error("downLoad_exception step get img data exception {}", e.getMessage(), e);
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                    }
                }
            }
            reTryCount++;
        }
        if (result == null || result.length == 0) {
            logger.info("downLoad_end img fail step reTryCount:{},url:{}", reTryCount, url);
        } else {
            logger.info("downLoad_end img success step reTryCount:{},url:{}", reTryCount, url);
        }
        return result;
    }

    /**
     * 代理设置处理
     *
     * @return
     */
    private CloseableHttpClient getHttpClient(HttpRequestBase httpRequestBase, Integer conTime, Integer socktTime) {
        if (!sysConfig.getIfUseProxy().booleanValue()) {
            return HttpClients.createDefault();
        }
        // 设置代理HttpHost
        HttpHost proxy = new HttpHost(sysConfig.getProxyIp(), sysConfig.getProxyPort());
        RequestConfig config = RequestConfig.custom().setProxy(proxy)
                .setConnectTimeout(conTime)
                .setSocketTimeout(socktTime)
                .build();
        httpRequestBase.setConfig(config);

        // 设置认证
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials(sysConfig.getProxyUsername(), sysConfig.getProxyPassword()));

        return HttpClients.custom().setDefaultCredentialsProvider(provider).build();
    }

    /**
     * 请求超级鹰接口服务
     */
    private String chaoJiYing(String imgBase64Str) throws IOException {
        if (!sysConfig.getUseCJY().booleanValue()) {
            logger.info("cjy_api step TEST***********return test data");
            return "{\"err_no\":0,\"err_str\":\"OK\",\"pic_id\":\"3110614003740400427\",\"pic_str\":\"10,10\",\"md5\":\"bdbf60be536302c4670fe988b2a6adda\"}";
        }
        HttpPost httpPost = new HttpPost("http://upload.chaojiying.net/Upload/Processing.php");

        JSONObject param = new JSONObject();
        param.put("user", sysConfig.getCjyUser());
        param.put("pass", sysConfig.getCjyPassword());
        param.put("softid", sysConfig.getCjySoftid());
        param.put("codetype", sysConfig.getCjyCodetype());
        //param.put("len_min","");
        param.put("file_base64", imgBase64Str);

        //设置代理及超时配置
        CloseableHttpClient httpClient = getHttpClient(httpPost, sysConfig.getCjyConnectTimeout(), sysConfig.getCjySocketTimeout());

        CloseableHttpResponse response = null;
        try {
            /**
             * 设置请求头信息及参数
             */
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            StringEntity putEntity = new StringEntity(param.toString(), "UTF-8");
            httpPost.setEntity(putEntity);
            /**
             * 发送请求
             */
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String reponseStr = EntityUtils.toString(response.getEntity(), "utf-8");
                logger.info("cjy_api success step：{}", reponseStr);
                return reponseStr;
            }
            logger.info("cjy_api fail step");
            throw new AppException(SystemCodeEnums.ERROR.getCode(), String.format("cjy step  HttpStatus:%s", response.getStatusLine().getStatusCode()));
        } finally {
            if (httpClient != null)
                httpClient.close();
            if (response != null)
                response.close();
        }
    }

    /**
     * 按比例对图片进行缩放.
     */
    private BufferedImage getScaleImg(BufferedImage img) {
        //获取缩放后的长和宽
        int width = img.getWidth();
        int height = img.getHeight();
        double rate = Arith.div(Integer.toString(sysConfig.getShowWidth()), Integer.toString(width));
        logger.info("img_compress_rate：{}", rate);
        int showHeight = (int) (height * rate);

        //获取缩放后的Image对象
        Image imgSrcScaled = img.getScaledInstance(sysConfig.getShowWidth(), showHeight, Image.SCALE_DEFAULT);
        //新建一个和Image对象相同大小的画布
        BufferedImage scaledImg = new BufferedImage(sysConfig.getShowWidth(), showHeight, BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics2D graphics = scaledImg.createGraphics();
        //将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
        graphics.drawImage(imgSrcScaled, 0, 0, null);
        //释放资源
        graphics.dispose();
        return scaledImg;
    }

    /**
     * 滑动滑块
     */
    private void doSlider(WebDriver driver, WebElement element) {
        int maxRetryTimes = 3;
        for (int i = 1; i <= maxRetryTimes; i++) {
            Actions action = new Actions(driver);
            action.moveToElement(element).clickAndHold(element);

            List<Integer> stepList = random(360);
            for (Integer a : stepList) {
                action.moveByOffset(a, 0).pause(400).perform();
            }
            action.release(element).perform();
            logger.info("do slide times:{}",i);
            //检测是否滑动成功
            for (int j = 1; j <= 8; j++) {
                try {
                    sleep(1);
                    WebElement slideEle = driver.findElement(By.id("nc_1_n1z"));
                    logger.info("do check slide result time:{}", j);
                    if (slideEle.getAttribute("class").equals("nc_iconfont btn_ok")) {
                        logger.info("do slide success class:nc_iconfont btn_ok");
                        return;
                    }
                } catch (Exception e) {
                    throw new AppException(SystemCodeEnums.ERROR.getCode(), "滑块验证码元素找不到，网址可能改版了，slideEle.id='nc_1_n1z'");
                }
            }
        }
        throw new AppException(SystemCodeEnums.ERROR.getCode(), "滑动验证码，操作失败，达到最大重试次数，maxRetryTimes:" + maxRetryTimes);
    }

    /**
     * 随机返回1到10之间的数字
     */
    private List<Integer> random(Integer maxNum) {
        List<Integer> resultList = new ArrayList<>();
        Random random = new Random();
        int count = 0;
        while (count < maxNum) {
            int result = random.nextInt(80) + 1;
            resultList.add(result);
            count += result;
        }
        return resultList;
    }

    private void sleep(Integer second) {
        try {
            logger.info("do sleep {}s", second);
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleepMS(Integer millisecond) {
        try {
            logger.info("do sleep {}ms", millisecond);
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
