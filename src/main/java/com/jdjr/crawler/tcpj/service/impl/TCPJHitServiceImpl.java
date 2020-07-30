package com.jdjr.crawler.tcpj.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jdjr.crawler.tcpj.common.enums.BusinessEnums;
import com.jdjr.crawler.tcpj.common.result.BasicResult;
import com.jdjr.crawler.tcpj.config.SysConfig;
import com.jdjr.crawler.tcpj.repository.domain.LoginData;
import com.jdjr.crawler.tcpj.service.TCPJHitService;
import com.jdjr.crawler.tcpj.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 检测同城账号是否命中风控
 *
 * @Author gaojunwei
 * @Date 2020/7/30 10:14
 **/
@Service
@Slf4j
public class TCPJHitServiceImpl implements TCPJHitService {

    @Resource
    private UserAccountService userAccountService;
    @Resource
    private SysConfig sysConfig;

    @Override
    public void checkHit(String taskId) {
        List<LoginData> loginData = userAccountService.getAllToken(BusinessEnums.TCPJ);
        if (loginData == null || loginData.isEmpty()) {
            logger.warn("{} 警告：{} 下账号登录数据为空", taskId, BusinessEnums.TCPJ.getValue());
            return;
        }
        int total = loginData.size();
        int success = 0;
        for (LoginData data : loginData) {
            BasicResult result = checkFP(sysConfig.getTcpjCheckHitRiskUrl(), data.getToken());
            if (result == null) {
                logger.info("{} {},{} check FAIL ...", taskId, data.getAccount(), data.getSite());
                continue;
            }
            if (result.getCode().equals("0"))
                success++;
            logger.info("{} {},{} check {}/{} SUCCESS :{}", taskId, data.getAccount(), data.getSite(), success, total, JSON.toJSONString(result));
            //维护账号的状态码信息
            try {
                userAccountService.updateAccountCodeInfo(data.getSite(), data.getAccount(), result.getCode(), result.getMsg());
            } catch (Exception e) {
                logger.error("{} {}", taskId, e.getMessage(), e);
            }
        }
    }

    /**
     * 检测是否命中反爬措施
     * 返回接口响应的code和msg
     */
    @Override
    public BasicResult checkFP(String url, String token) {
        try {
            CredentialsProvider provider = new BasicCredentialsProvider();
            RequestConfig config;
            /**代理设置-超时时间设置*/
            if (sysConfig.getIfUseProxy().booleanValue()) {
                HttpHost proxy = new HttpHost(sysConfig.getProxyIp(), sysConfig.getProxyPort());
                provider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials(sysConfig.getProxyUsername(), sysConfig.getProxyPassword()));
                config = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).setProxy(proxy).build();
            } else {
                config = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
            }
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(provider).build();

            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(config);
            //请求头设置
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Host", "www.tcpjw.com");
            httpPost.setHeader("Authorization", "Bearer " + token);
            httpPost.setHeader("Origin", "https://www.tcpjw.com");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            httpPost.setHeader("OriginV", "3.9");
            httpPost.setHeader("Referer", "https://www.tcpjw.com/B2BHall/");
            httpPost.setHeader("Cookie", "_uab_collina=159427483449404126228267;_tacau=MCw2MWY1NzU5Yi1hODJhLWI1ZGMtZDhiYS04OTRlMjU5OTcxZjQs;access_token=" + token + ";");
            //设置请求参数
            StringEntity stringEntity = new StringEntity("{\"version\":\"3.5\",\"source\":\"HTML\",\"channel\":\"01\",\"pageNum\":1,\"pageSize\":50,\"payType\":null,\"bid\":null,\"bankName\":null,\"lastTime\":null,\"lastTimeStart\":null,\"lastTimeEnd\":null,\"startDate\":null,\"endDate\":null,\"flawStatus\":\"\",\"priceType\":null,\"priceSp\":null,\"priceEp\":null,\"yearQuote\":null,\"msw\":null,\"mswStart\":null,\"mswEnd\":null,\"orderColumn\":null,\"sortType\":\"\",\"depositPay\":null,\"blackBankName\":null,\"isCollected\":false,\"orderStatus\":null}", "UTF-8");
            httpPost.setEntity(stringEntity);

            CloseableHttpResponse resp = httpClient.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() == 200) {
                String jsonStr = EntityUtils.toString(resp.getEntity());
                JSONObject jsonObject = JSON.parseObject(jsonStr);
                BasicResult result = new BasicResult();
                result.setCode(jsonObject.getString("code"));
                result.setMsg(jsonObject.getString("msg"));
                return result;
            } else if (resp.getStatusLine().getStatusCode() == 401) {
                String jsonStr = EntityUtils.toString(resp.getEntity());
                BasicResult result = new BasicResult();
                result.setCode("-3");
                result.setMsg("httpCode:401," + jsonStr);
                return result;
            }
        } catch (IOException e) {
            logger.error("checkHit exception {}", e.getMessage(), e);
        }
        return null;
    }
}