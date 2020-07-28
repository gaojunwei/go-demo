package com.jdjr.crawler.tcpj.controller;

import com.alibaba.fastjson.JSON;
import com.jdjr.crawler.tcpj.common.enums.SystemCodeEnums;
import com.jdjr.crawler.tcpj.common.result.SingleResult;
import com.jdjr.crawler.tcpj.common.util.DateFormatUtils;
import com.jdjr.crawler.tcpj.config.SysConfig;
import com.jdjr.crawler.tcpj.schedule.BIHUCatch;
import com.jdjr.crawler.tcpj.schedule.TCPJCatch;
import com.jdjr.crawler.tcpj.schedule.data.BaseData;
import com.jdjr.crawler.tcpj.schedule.data.TcpjData;
import com.jdjr.crawler.tcpj.service.TCPJService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private TCPJService tcpjService;

    @Value("${tcpj.tcaptcha.loginPageUrl:'https://www.tcpjw.com/passport/login'}")
    private String url;

    @Resource
    private SysConfig sysConfig;

    /**
     * 测试获取同城票据的登录Token
     *
     * @return
     */
    @RequestMapping(value = "token", method = RequestMethod.GET)
    public SingleResult<String> tcpjGetToken(@RequestParam(required = false) String account, @RequestParam(required = false) String password, @RequestParam(required = false) Boolean istest) {
        logger.info("tcpjGetToken_start account:{},password:{},istest:{}", account, password, istest);
        if (istest != null && istest.booleanValue()) {
            account = "13910099494";
            password = "123QWEasd";
            logger.info("测试请求 account:{},password:{}", account, password);
        }
        SingleResult<String> result = new SingleResult<>();
        //参数校验
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            result.setCode(SystemCodeEnums.SUCCESS.getCode());
            result.setMsg(SystemCodeEnums.SUCCESS.getMsg());
            logger.info("tcpjGetToken_end result:{}", JSON.toJSONString(result));
            return result;
        }
        account = account.trim();
        password = password.trim();
        //获取登录态
        String token = tcpjService.getLoginToken(url, account, password);
        if (StringUtils.isEmpty(token)) {
            result.setCode(SystemCodeEnums.ERROR.getCode());
            result.setMsg("获取登录TOKEN失败，请检查该网站是否改版或者反爬");
            logger.info("tcpjGetToken_end result:{}", JSON.toJSONString(result));
            return result;
        }
        result.setCode(SystemCodeEnums.SUCCESS.getCode());
        result.setMsg(SystemCodeEnums.SUCCESS.getMsg());
        result.setData("Bearer " + token);
        logger.info("tcpjGetToken_end result:{}", JSON.toJSONString(result));
        return result;
    }

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
        return getLogInToken(0);
    }

    /**
     * 获取普通账户登录TOKEN
     */
    @RequestMapping(value = "getPmToken", method = RequestMethod.GET)
    public SingleResult<String> getTokenType1() {
        return getLogInToken(1);
    }

    /**
     * 获取普通账户登录TOKEN
     */
    @RequestMapping(value = "getBiHuToken", method = RequestMethod.GET)
    public SingleResult<String> getBiHuToken() {
        BaseData baseData = BIHUCatch.getToken();
        SingleResult<String> result = new SingleResult<>();
        result.setCode(SystemCodeEnums.SUCCESS.getCode());
        if (baseData == null) {
            result.setMsg("无可用Token");
            logger.info("BIHU request_token result:{}", JSON.toJSONString(result));
            return result;
        }
        result.setMsg(String.format("手机号：%s-创建日期：%s-是否已经使用：%s", baseData.getPhone(), DateFormatUtils.dateFormat(baseData.getCreatTime(), DateFormatUtils.FormatEnums.yyyy_MM_dd_HH_mm_ss), baseData.getIsUsed()));
        result.setData(baseData.getToken());
        logger.info("BIHU request_token result:{}", JSON.toJSONString(result));
        return result;


    }

    /**
     * 获取最近登录任务执行日志
     *
     * @return
     */
    @RequestMapping(value = "log", method = RequestMethod.GET)
    public SingleResult<Map<String, Object[]>> getLog() {
        SingleResult<Map<String, Object[]>> result = new SingleResult<>();
        result.setCode(SystemCodeEnums.SUCCESS.getCode());
        result.setMsg(SystemCodeEnums.SUCCESS.getMsg());

        Map<String, Object[]> dataMap = new HashMap<>();
        dataMap.put("tcpj", TCPJCatch.getLog());
        dataMap.put("bihu", BIHUCatch.getLog());

        result.setData(dataMap);
        return result;
    }

    /**
     * 获取缓存中记录的登录态信息
     *
     * @return
     */
    @RequestMapping(value = "catch", method = RequestMethod.GET)
    public SingleResult<Map<String, List>> getCatch() {
        SingleResult<Map<String, List>> result = new SingleResult<>();
        result.setCode(SystemCodeEnums.SUCCESS.getCode());
        result.setMsg(SystemCodeEnums.SUCCESS.getMsg());

        Map<String, List> dataMap = new HashMap<>();
        dataMap.put("tcpj", TCPJCatch.getCatch());
        dataMap.put("bihu", BIHUCatch.getCatch());

        result.setData(dataMap);
        return result;
    }

    /**
     * 获取Token核心方法
     */
    private SingleResult<String> getLogInToken(Integer phoneType) {
        TcpjData tcpjData;
        if (phoneType.intValue() == 0) {
            tcpjData = TCPJCatch.getTokenType0();
        } else {
            tcpjData = TCPJCatch.getTokenType1();
        }
        SingleResult<String> result = new SingleResult<>();
        result.setCode(SystemCodeEnums.SUCCESS.getCode());
        if (tcpjData == null) {
            result.setMsg("无卡用Token,类型:" + phoneType);
            logger.info("request_token type:{},result:{}", phoneType, JSON.toJSONString(result));
            return result;
        }
        result.setMsg(String.format("手机号：%s-类型：%s-创建日期：%s-是否已经使用：%s", tcpjData.getPhone(), tcpjData.getPhoneType(), DateFormatUtils.dateFormat(tcpjData.getCreatTime(), DateFormatUtils.FormatEnums.yyyy_MM_dd_HH_mm_ss), tcpjData.getIsUsed()));
        result.setData(tcpjData.getToken());
        logger.info("request_token type:{},result:{}", phoneType, JSON.toJSONString(result));
        return result;
    }
}
