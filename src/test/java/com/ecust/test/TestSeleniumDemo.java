/*
package com.ecust.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jdjr.crawler.tcpj.common.enums.SystemCodeEnums;
import com.jdjr.crawler.tcpj.common.exception.AppException;
import com.jdjr.crawler.tcpj.common.util.Arith;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.*;

*/
/**
 * 用来测试 Selenium
 * 超级鹰官网：http://www.chaojiying.com/price.html
 *//*

public class TestSeleniumDemo {
    private WebDriver driver;
    //图形验证实际显示宽度（用于等比缩放）
    private Integer showWidth = 340;
    //是否使用超级鹰接口
    private Boolean useCJY = true;
    //验证码点击失败最大重试次数
    private Integer reTryTimes = 3;

    @Test
    public void tongChengPiaoJu() {
        //开启个浏览器并且输入链接
        driver = getChromeDriver();
        //通过driver控制浏览器打开链接（url）
        driver.get("https://www.tcpjw.com/passport/login");

        sleep(2);
        //输入手机号
        WebElement phoneInput = driver.findElement(By.cssSelector("input[class='ant-input login-input']"));
        phoneInput.sendKeys("13910099494");
        sleep(1);
        //输入密码
        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));
        passwordInput.sendKeys("123QWEasd");
        //找到滑块元素
        WebElement slider = driver.findElement(By.cssSelector("span#nc_1_n1z"));
        //滑动滑块
        doSlider(slider);
        sleep(2);
        //点击登录按钮
        WebElement logInBtn = driver.findElement(By.cssSelector("button[class='ant-btn login-button ant-btn-primary']"));
        logInBtn.click();
        sleep(3);
        */
/** 点击图形验证码处理 *//*

        int count = 1;
        boolean isSuccess = false;
        //切换到iframe窗口
        driver.switchTo().frame("tcaptcha_iframe");
        while (count <= reTryTimes) {
            try {
                isSuccess = doTcaptcha();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("点击图形验证码处理结果：" + isSuccess);
            //点击失败刷新验证码
            if (!isSuccess) {
                System.out.println("点击验证码失败-进行重试,第" + count + "次！！！！");
                WebElement reloadEle = driver.findElement(By.id("reload"));
                reloadEle.click();
                sleep(3);
            } else {
                break;
            }
            count++;
        }
        if (isSuccess) {
            System.out.println("恭喜，图形验证码点击成功。");
            sleep(3);
        } else {
            System.out.println("图形验证码点击失败...");
        }

        //获取登录后的cookie信息
        Set<Cookie> cookies = driver.manage().getCookies();
        System.out.println("获取登录成功的Cookie信息：" + JSON.toJSONString(cookies));
        sleep(50);
    }

    */
/**
     * 图形验证码，点击处理，含重试逻辑
     *
     * @return
     *//*

    public boolean doTcaptcha() throws IOException {
        //获取图形验证码的点击坐标值
        Integer x, y;
        String coordinate = getCoordinate();
        if (StringUtils.isEmpty(coordinate)) {
            System.out.println("获取图形验证码点击坐标失败");
            return false;
        }
        String[] coordinates = coordinate.split(",");
        x = Integer.parseInt(coordinates[0]);
        y = Integer.parseInt(coordinates[1]);
        //根据坐标点击图形验证码
        WebElement imgEle = driver.findElement(By.id("tcaptcha-img"));
        System.out.println("--->>" + imgEle.getTagName() + " x=" + x + " y=" + y);
        Actions action = new Actions(driver);
        action.moveToElement(imgEle, x, y).click();
        action.release().perform();
        sleepMS(200);
        //判断是否点击图形验证码成功过
        try {
            WebElement tcaptchaNoteEle = driver.findElement(By.id("tcaptcha_note"));
            if (!tcaptchaNoteEle.getText().trim().equals("")) {
                System.out.println("验证点击描述：" + tcaptchaNoteEle.getText());
                return false;
            }
        } catch (Exception e) {
            System.out.println("获取点击描述异常");
        }
        return true;
    }

    */
/**
     * 获取点击坐标值
     *//*

    private String getCoordinate() throws IOException {
        try {
            WebElement imgEle = driver.findElement(By.id("tcaptcha-img"));
            String imgSrc = imgEle.getAttribute("src");
            System.out.println("获取图片地址：" + imgSrc);

            WebElement imgDescEle = driver.findElement(By.cssSelector("p[class='tcaptcha-title']"));
            String imgDescStr = imgDescEle.getText();
            System.out.println("获取图片描述：" + imgDescStr);
            //拼接生成新图片的base64字符
            String base64Str = getImgBase64(imgSrc, imgDescStr);
            //调用打码平台获取点击坐标
            String resultStr = chaoJiYing(base64Str);
            JSONObject jsonObject = JSON.parseObject(resultStr);
            if (jsonObject.getIntValue("err_no") != 0 || StringUtils.isEmpty(jsonObject.getString("pic_str"))) {
                System.out.println("调用超级鹰服务失败");
                return null;
            }
            return jsonObject.getString("pic_str");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    */
/**
     * 图片下载，拼接文字，返回base64字符
     *
     * @param imgSrc
     * @param imgDescStr
     * @return
     *//*

    public String getImgBase64(String imgSrc, String imgDescStr) {
        //图片下载
        String base64Str = null;
        try {
            byte[] imgBts = getImgData(imgSrc);
            InputStream inputStream = new ByteArrayInputStream(imgBts);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            //获取图片的高度和宽度
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            //设置文字图片信息
            int textHeight = 40;
            BufferedImage textImage = new BufferedImage(width, textHeight, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics graphics = textImage.getGraphics();
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.black);
            Font font = new Font("宋体", Font.BOLD, 20);
            graphics.setFont(font);
            graphics.drawString(imgDescStr, 10, 30);

            int[] textImageArray = new int[width * textHeight];
            textImageArray = textImage.getRGB(0, 0, width, textHeight, textImageArray, 0, width);

            */
/**生成新图片*//*

            int[] bufferedImageArray = new int[width * height];
            bufferedImageArray = bufferedImage.getRGB(0, 0, width, height, bufferedImageArray, 0, width);


            */
/**合并成新图片*//*

            BufferedImage imageNew = new BufferedImage(width, height + textHeight, BufferedImage.TYPE_INT_RGB);
            //设置上半部分的RGB
            imageNew.setRGB(0, 0, width, height, bufferedImageArray, 0, width);
            //设置下半部分的RGB
            imageNew.setRGB(0, height, width, textHeight, textImageArray, 0, width);
            //保存到本地
            File outFile = new File("D:/cd/qq2.jpg");
            ImageIO.write(imageNew, "jpg", outFile);// 写图片
            //图片进行缩放
            BufferedImage scaleImg = getScaleImg(imageNew);
            //获取图片base64字符串
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(scaleImg, "jpg", stream);
            base64Str = new String(Base64.encodeBase64(stream.toByteArray()), "utf-8");
            System.out.println("拼接完后的图片的Base64字符串：data:image/jpg;base64," + base64Str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64Str;
    }


    */
/**
     * 按比例对图片进行缩放.
     *//*

    public BufferedImage getScaleImg(BufferedImage img) {
        //获取缩放后的长和宽
        int width = img.getWidth();
        int height = img.getHeight();
        double rate = Arith.div(Integer.toString(showWidth), Integer.toString(width));
        System.out.println("图片缩放比例为：" + rate);
        int showHeight = (int) (height * rate);

        //获取缩放后的Image对象
        Image imgSrcScaled = img.getScaledInstance(showWidth, showHeight, Image.SCALE_DEFAULT);
        //新建一个和Image对象相同大小的画布
        BufferedImage scaledImg = new BufferedImage(showWidth, showHeight, BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics2D graphics = scaledImg.createGraphics();
        //将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
        graphics.drawImage(imgSrcScaled, 0, 0, null);
        //释放资源
        graphics.dispose();
        return scaledImg;
    }

    */
/**
     * 滑动滑块
     *//*

    public void doSlider(WebElement element) {
        Actions action = new Actions(driver);
        action.moveToElement(element).clickAndHold(element);

        List<Integer> stepList = random(360);
        for (Integer a : stepList) {
            action.moveByOffset(a, 0).pause(300).perform();
        }
        action.release().perform();
    }

    */
/**
     * 随机返回1到10之间的数字
     *//*

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

    @After
    public void after() {
        if (driver != null) {
            //退出此驱动程序，关闭所有相关窗口。
            driver.quit();
            //关闭当前窗口，如果是当前打开的最后一个窗口，则退出浏览器。
            //driver.close();
            System.out.println("成功关闭浏览器");
        }
    }

    */
/**
     * 打开谷歌浏览器，返回一个WebDriver，对浏览器的操作通过webDriver来执行
     *//*

    public static WebDriver getChromeDriver() {
        //设置谷歌浏览器驱动，我放在项目的路径下，这个驱动可以帮你打开本地的谷歌浏览器
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        // 设置对谷歌浏览器的初始配置           开始
        HashMap<String, Object> prefs = new HashMap<String, Object>();
        //设置禁止图片
        //prefs.put("profile.managed_default_content_settings.images", 2);
        //设置禁止cookies
        //prefs.put("profile.default_content_settings.cookies", 2);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        DesiredCapabilities chromeCaps = DesiredCapabilities.chrome();
        chromeCaps.setCapability(ChromeOptions.CAPABILITY, options);
        // 设置对谷歌浏览器的初始配置           结束

        ChromeDriverService.Builder builder = new ChromeDriverService.Builder();
        ChromeDriverService chromeService = builder.usingDriverExecutable(new File("chromedriver.exe")).usingPort(3333).build();

        //新建一个谷歌浏览器对象（driver）
        WebDriver driver = new EventFiringWebDriver(new ChromeDriver(chromeService, options)).register(new MyWebDriverEventListener());
        return driver;
    }

    */
/**
     * 获取图片数据
     *//*

    public byte[] getImgData(String url) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = HttpClientTool.getHttpClient().execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toByteArray(response.getEntity());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    */
/**
     * 请求超级鹰接口服务
     *//*

    public String chaoJiYing(String imgBase64Str) throws IOException {
        if (!useCJY.booleanValue()) {
            System.out.println("返回测试数据，未请求超级鹰服务");
            return "{\"err_no\":0,\"err_str\":\"OK\",\"pic_id\":\"3110614003740400427\",\"pic_str\":\"10,10\",\"md5\":\"bdbf60be536302c4670fe988b2a6adda\"}";
        }
        HttpPost httpPost = new HttpPost("http://upload.chaojiying.net/Upload/Processing.php");
        Integer connectTimeOut = 30000;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        JSONObject param = new JSONObject();
        param.put("user", "cjy2jy");
        param.put("pass", "diyanfei910629");
        param.put("softid", "903238");
        param.put("codetype", "9101");
        //param.put("len_min","");
        param.put("file_base64", imgBase64Str);

        */
/**
         * 设置请求超时时间
         *//*

        RequestConfig requestConfig;
        if (connectTimeOut != null) {
            requestConfig = RequestConfig.custom().setSocketTimeout(connectTimeOut).setConnectTimeout(connectTimeOut).build();
            httpPost.setConfig(requestConfig);
        }
        CloseableHttpResponse response = null;
        try {
            */
/**
             * 设置请求头信息及参数
             *//*

            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            StringEntity putEntity = new StringEntity(param.toString(), "UTF-8");
            httpPost.setEntity(putEntity);
            */
/**
             * 发送请求
             *//*

            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String reponseStr = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println("超级鹰返回数据：" + reponseStr);
                return reponseStr;
            }
            throw new AppException(SystemCodeEnums.ERROR.getCode(), String.format("HttpStatus:%s", response.getStatusLine().getStatusCode()));
        } finally {
            if (httpClient != null)
                httpClient.close();
            if (response != null)
                response.close();
        }
    }

    private void sleep(Integer second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleepMS(Integer second) {
        try {
            Thread.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}*/
