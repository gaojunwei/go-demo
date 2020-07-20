package com.jdjr.crawler.tcpj.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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
     * 账号密码信息
     */
    @Value("${tcpj.account:'13910099494'}")
    private String tcpjCccount;
    @Value("${tcpj.account.pwd:'123QWEasd'}")
    private String tcpjAccountPwd;

    /**
     * 超级鹰的HTTP超时配置(单位毫秒)
     */
    @Value("${cjy.connectTimeout:40000}")
    private Integer cjyConnectTimeout;
    @Value("${cjy.socketTimeout:40000}")
    private Integer cjySocketTimeout;

    /**
     * 打开登录页面最大检测次数（每次/10s）
     */
    @Value("${tcpj.open.loginUrl.times:100}")
    private Integer tcpjOpenLoginUrlTimes;
}
