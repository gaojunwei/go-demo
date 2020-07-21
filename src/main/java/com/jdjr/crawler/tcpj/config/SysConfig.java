package com.jdjr.crawler.tcpj.config;

import com.jdjr.crawler.tcpj.config.data.UserInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
@Getter
@Setter
public class SysConfig {
    /**
     * 图形验证实际显示宽度（用于等比缩放）
     */
    @Value("${tcpj.tcaptcha.showWidth:340}")
    private Integer showWidth;

    /**
     * 是否使用超级鹰接口,默认使用
     */
    @Value("${tcpj.tcaptcha.isUseCJY:true}")
    private Boolean useCJY;

    /**
     * 验证码点击失败最大重试次数
     */
    @Value("${tcpj.tcaptcha.reTryTimes:3}")
    private Integer tcpjTcaptchaReTryTimes;

    /**
     * 是否使用代理
     */
    @Value("${tcpj.tcaptcha.ifUseProxy:false}")
    private Boolean ifUseProxy;

    /**
     * 链接超时时间
     */
    @Value("${tcpj.connectTimeout:10000}")
    private Integer connectTimeout;

    /**
     * socket超时时间
     */
    @Value("${tcpj.socketTimeout:10000}")
    private Integer socketTimeout;

    /**
     * 代理IP
     */
    @Value("${proxy.ip:'172.25.135.187'}")
    private String proxyIp;

    /**
     * 代理端口
     */
    @Value("${proxy.port:61888}")
    private Integer proxyPort;

    /**
     * 代理账户ID
     */
    @Value("${proxy.username:'jdjr_crawler'}")
    private String proxyUsername;

    /**
     * 代理账户密码
     */
    @Value("${proxy.password:'jdjr_crawler007'}")
    private String proxyPassword;

    /**
     * Selenium 代理配置插件位置
     */
    @Value("${selenium.proxyFilePath}")
    private String seleniumProxyFilePath;

    /**
     * 验证码图片下载重试次数
     */
    @Value("${tcpj.downloadImg.retryTimes:3}")
    private Integer tcpjDownloadImgRetryTimes;

    /**
     * 超级鹰账户信息配置
     */
    @Value("${cjy.user}")
    private String cjyUser;
    @Value("${cjy.password}")
    private String cjyPassword;
    @Value("${cjy.softid}")
    private String cjySoftid;
    @Value("${cjy.codetype}")
    private String cjyCodetype;

    @Value("${tcpj.tcaptcha.loginPageUrl:'https://www.tcpjw.com/passport/login'}")
    private String tcpjLoginPageUrl;

    /**
     * 超级鹰的HTTP超时配置(单位毫秒)
     */
    @Value("${cjy.connectTimeout:40000}")
    private Integer cjyConnectTimeout;
    @Value("${cjy.socketTimeout:40000}")
    private Integer cjySocketTimeout;

    /**
     * 打开登录页面最大检测次数（每次/1s）
     */
    @Value("${tcpj.open.loginUrl.times:1000}")
    private Integer tcpjOpenLoginUrlTimes;

    /**
     * 每个账号登录重试最大次数
     */
    @Value("${tcpj.account.login.retryTime:3}")
    private Integer tcpjAccountLoginRetryTime;

    /**
     * 账号密码信息
     */
    @Value("${tcpj.account.info}")
    private String tcpjCccountInfo;

    /**
     * 获取同城票据的账户信息列表
     *
     * @return
     */
    public List<UserInfo> getTcpjAccounts() {
        List<UserInfo> list = new ArrayList<>();

        String[] items = tcpjCccountInfo.split("\\|");
        for (String item : items) {
            String[] itemArr = item.split(",");

            UserInfo userInfo = new UserInfo();
            userInfo.setAccount(itemArr[0].trim());
            userInfo.setPassword(itemArr[1].trim());
            userInfo.setType(Integer.parseInt(itemArr[2]));

            list.add(userInfo);
        }
        return list;
    }
}
