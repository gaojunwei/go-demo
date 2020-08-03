package com.jdjr.crawler.tcpj.controller;

import com.alibaba.fastjson.JSON;
import com.jdjr.crawler.tcpj.common.enums.BusinessEnums;
import com.jdjr.crawler.tcpj.common.enums.SystemCodeEnums;
import com.jdjr.crawler.tcpj.common.result.SingleResult;
import com.jdjr.crawler.tcpj.common.util.DateFormatUtils;
import com.jdjr.crawler.tcpj.config.SysConfig;
import com.jdjr.crawler.tcpj.repository.domain.LoginData;
import com.jdjr.crawler.tcpj.service.BiHuService;
import com.jdjr.crawler.tcpj.service.TCPJService;
import com.jdjr.crawler.tcpj.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/14 17:21
 **/
@RestController
@RequestMapping("/")
@Slf4j
public class IndexController {

    @Resource
    private UserAccountService userAccountService;
    @Resource
    private BiHuService biHuService;
    @Resource
    private TCPJService tcpjService;

    @Resource
    private SysConfig sysConfig;

    /**
     * 测试代理是否可用
     *
     * @return
     */
    @RequestMapping(value = "proxy", method = RequestMethod.GET)
    public SingleResult<String> testProxy() {
        SingleResult<String> stringSingleResult = new SingleResult<>();

        String url = "https://www.baidu.com";


        String data = downLoad(url);

        stringSingleResult.setData(data);

        stringSingleResult.setCode(SystemCodeEnums.SUCCESS.getCode());
        stringSingleResult.setMsg(SystemCodeEnums.SUCCESS.getMsg());
        return stringSingleResult;
    }

    public String downLoad(String url) {
        try {
            // 设置代理HttpHost
            HttpHost proxy = new HttpHost(sysConfig.getProxyIp(), sysConfig.getProxyPort());
            // 设置认证
            CredentialsProvider provider = new BasicCredentialsProvider();
            provider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials(sysConfig.getProxyUsername(), sysConfig.getProxyPassword()));

            CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(provider).build();

            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();

            HttpGet httpGet = new HttpGet(url);

            httpGet.setConfig(config);

            CloseableHttpResponse resp = httpClient.execute(httpGet);

            if (resp.getStatusLine().getStatusCode() == 200) {
                String str = EntityUtils.toString(resp.getEntity());
                logger.info("返回数据：{}", str);
                return str;
            }
        } catch (IOException e) {
            logger.error("exception **** {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取普通账户登录TOKEN
     */
    @RequestMapping(value = "getToken", method = RequestMethod.GET)
    public SingleResult<String> getTokenType0() {
        return getLogInToken(BusinessEnums.TCPJ, 0);
    }

    /**
     * 获取普通账户登录TOKEN
     */
    @RequestMapping(value = "getPmToken", method = RequestMethod.GET)
    public SingleResult<String> getTokenType1() {
        return getLogInToken(BusinessEnums.TCPJ, 1);
    }

    /**
     * 获取普通账户登录TOKEN
     */
    @RequestMapping(value = "getBiHuToken", method = RequestMethod.GET)
    public SingleResult<String> getBiHuToken() {
        return getLogInToken(BusinessEnums.BIHU, 0);
    }

    /**
     * 获取Token核心方法
     */
    private SingleResult<String> getLogInToken(BusinessEnums businessEnums, Integer phoneType) {
        SingleResult<String> result = new SingleResult<>();
        LoginData loginData = userAccountService.getToken(businessEnums, phoneType);
        if (loginData == null) {
            result.setMsg("无卡用Token,类型:" + phoneType);
            logger.info("request_token site:{},type:{},result:{}", businessEnums.getValue(), phoneType, JSON.toJSONString(result));
            return result;
        }

        result.setMsg(String.format("手机号：%s-类型：%s-创建日期：%s-是否已经使用：%s", loginData.getAccount(), loginData.getType(), DateFormatUtils.dateFormat(loginData.getTimeStamp(), DateFormatUtils.FormatEnums.yyyy_MM_dd_HH_mm_ss), loginData.getIsUsed()));
        result.setData(loginData.getToken());
        logger.info("request_token site:{},type:{},result:{}", businessEnums.getValue(), phoneType, JSON.toJSONString(result));
        return result;
    }

    /**
     * 获取缓存中记录的登录态信息
     *
     * @return
     */
    @RequestMapping(value = "flushToken", method = RequestMethod.GET)
    public SingleResult<String> flushBtoken(@RequestParam String type, @RequestParam String account, @RequestParam String pwd, @RequestParam String code) {
        SingleResult<String> result = new SingleResult<>();
        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(account) || StringUtils.isEmpty(pwd)) {
            result.setCode(SystemCodeEnums.ERROR.getCode());
            result.setMsg(SystemCodeEnums.ERROR.getMsg());
            return result;
        }
        if (type.trim().toLowerCase().equals("bihu")) {
            String token = biHuService.getLoginToken(sysConfig.getBihuLoginPageUrl(), account, pwd);
            result.setCode(SystemCodeEnums.SUCCESS.getCode());
            result.setMsg(SystemCodeEnums.SUCCESS.getMsg());
            result.setData(token);
            return result;
        }
        if (type.trim().toLowerCase().equals("tcpj")) {
            code = StringUtils.isEmpty(code) ? "0" : code;
            String token = tcpjService.getLoginToken(sysConfig.getTcpjLoginPageUrl(), account, pwd, code);
            result.setCode(SystemCodeEnums.SUCCESS.getCode());
            result.setMsg(SystemCodeEnums.SUCCESS.getMsg());
            result.setData(token);
            return result;
        }
        result.setCode(SystemCodeEnums.ERROR.getCode());
        result.setMsg("type only is bihu or tcpj");
        return result;
    }
}
