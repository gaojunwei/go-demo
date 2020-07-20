package com.ecust.test.procy;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashSet;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/15 20:46
 **/
public class ProxyTest {

    @Test
    public void test001(){
        System.setProperty("javax.net.ssl.trustStore", "D:/proxy/cacerts");


        String url = "https://www.baidu.com";


        HttpGet httpGet = new HttpGet(url);

        //设置代理及超时配置
        setProxy(httpGet,true);

        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = HttpClientTool.getHttpClient();
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity());
                System.out.println("请求返回数据："+result.substring(0,100));
                return;
            }
            System.out.println("请求失败");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 设置代理信息
     *
     * @param httpRequestBase
     */
    private void setProxy(HttpRequestBase httpRequestBase,boolean ifUserProxy) {
        RequestConfig requestConfig;
        if (ifUserProxy) {
            HttpHost proxyHost = new HttpHost("10.13.220.17", 8888);

            Collection<String> targetPreferredAuthSchemes = new HashSet<>();
            targetPreferredAuthSchemes.add(null);
            targetPreferredAuthSchemes.add(null);

            requestConfig = RequestConfig.custom()
                    .setSocketTimeout(10000)
                    .setConnectTimeout(10000)
                    .setProxy(proxyHost)
                    //.setProxyPreferredAuthSchemes(targetPreferredAuthSchemes)
                    .build();

        } else {
            requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
        }
        httpRequestBase.setConfig(requestConfig);
    }
}
