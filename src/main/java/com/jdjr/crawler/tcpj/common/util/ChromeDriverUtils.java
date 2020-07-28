package com.jdjr.crawler.tcpj.common.util;

import com.jdjr.crawler.tcpj.config.SysConfig;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.HashMap;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/15 10:29
 **/
@Slf4j
public class ChromeDriverUtils {

    private static SysConfig sysConfig = SpringUtils.getBean("sysConfig", SysConfig.class);

    public static WebDriver getInstance() {
        return getChromeDriver();
    }

    /**
     * 打开谷歌浏览器，返回一个WebDriver，对浏览器的操作通过webDriver来执行
     */
    private static WebDriver getChromeDriver() {
        //设置谷歌浏览器驱动，我放在项目的路径下，这个驱动可以帮你打开本地的谷歌浏览器
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        /**设置对谷歌浏览器的初始配置****开始*/
        HashMap<String, Object> prefs = new HashMap<String, Object>();
        //设置禁止图片
        //prefs.put("profile.managed_default_content_settings.images", 2);
        //设置禁止cookies
        //prefs.put("profile.default_content_settings.cookies", 2);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        /** 代理设置 */
        if (sysConfig.getIfUseProxy().booleanValue()) {
            logger.info("selenium use proxy ********** IP:{},Port:{},ExtFile:{}", sysConfig.getProxyIp(), sysConfig.getProxyPort(), sysConfig.getSeleniumProxyFilePath());
            //Proxy proxy = new Proxy().setHttpProxy(String.format("%s:%s", sysConfig.getProxyIp(), sysConfig.getProxyPort())).setProxyType(Proxy.ProxyType.MANUAL);
            //options.setProxy(proxy);
            options.addExtensions(new File(sysConfig.getSeleniumProxyFilePath()));
        }

        DesiredCapabilities chromeCaps = DesiredCapabilities.chrome();
        chromeCaps.setCapability(ChromeOptions.CAPABILITY, options);
        /**设置对谷歌浏览器的初始配置****结束*/
        //新建一个谷歌浏览器对象（driver）
        return new ChromeDriver(ChromeDriverService.createDefaultService(), options);
    }
}