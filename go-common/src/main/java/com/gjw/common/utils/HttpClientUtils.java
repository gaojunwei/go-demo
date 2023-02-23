package com.gjw.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.gjw.common.enums.SystemCodeEnums;
import com.gjw.common.exception.AppException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * HttpClient工具类
 *
 * @author gjw
 */
public class HttpClientUtils {

    /**
     * TRACE请求
     * 回显服务器收到的请求，主要用于测试或诊断
     */
    public static String trace(String url, Integer connectTimeOut) throws IOException {
        HttpTrace httpTrace = new HttpTrace(url);
        return sendRequest(httpTrace, connectTimeOut);
    }

    /**
     * GET请求
     */
    public static String get(String url, Integer connectTimeOut) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        return sendRequest(httpGet, connectTimeOut);
    }

    /**
     * PUT请求
     *
     * @param url            请求的URL
     * @param requestParam   请求对象
     * @param connectTimeOut 请求链接超时时间 单位毫秒
     * @return
     * @throws IOException
     */
    public static String put(String url, JSONObject requestParam, Integer connectTimeOut) throws IOException {
        HttpPut httpPut = new HttpPut(url);
        return sendRequest(requestParam, connectTimeOut, httpPut);
    }

    /**
     * POST请求
     *
     * @param url            请求的URL
     * @param requestParam   请求对象
     * @param connectTimeOut 请求链接超时时间 单位毫秒
     * @return
     * @throws IOException
     */
    public static String post(String url, JSONObject requestParam, Integer connectTimeOut) throws IOException {
        HttpPost httpPut = new HttpPost(url);
        return sendRequest(requestParam, connectTimeOut, httpPut);
    }

    private static String sendRequest(JSONObject jsonObject, Integer connectTimeOut, HttpEntityEnclosingRequestBase httpRequestBase) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        /**
         * 设置请求超时时间
         */
        RequestConfig requestConfig;
        if (connectTimeOut != null) {
            requestConfig = RequestConfig.custom().setSocketTimeout(connectTimeOut).setConnectTimeout(connectTimeOut).build();
            httpRequestBase.setConfig(requestConfig);
        }
        CloseableHttpResponse response = null;
        try {
            /**
             * 设置请求头信息及参数
             */
            httpRequestBase.setHeader("Content-type", "application/json");
            StringEntity putEntity = new StringEntity(jsonObject.toString(), "UTF-8");
            httpRequestBase.setEntity(putEntity);
            /**
             * 发送请求
             */
            response = httpClient.execute(httpRequestBase);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                return EntityUtils.toString(response.getEntity(), "utf-8");
            throw new AppException(SystemCodeEnums.ERROR.getCode(), String.format("HttpStatus:%s", response.getStatusLine().getStatusCode()));
        } finally {
            if (httpClient != null)
                httpClient.close();
            if (response != null)
                response.close();
        }
    }

    private static String sendRequest(HttpRequestBase httpRequestBase, Integer connectTimeOut) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        /**
         * 设置请求超时时间
         */
        RequestConfig requestConfig;
        if (connectTimeOut != null) {
            requestConfig = RequestConfig.custom().setSocketTimeout(connectTimeOut).setConnectTimeout(connectTimeOut).build();
            httpRequestBase.setConfig(requestConfig);
        }
        try {
            response = httpClient.execute(httpRequestBase);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, "utf-8");
            }
            throw new AppException(SystemCodeEnums.ERROR.getCode(), String.format("HttpStatus:%s", response.getStatusLine().getStatusCode()));
        } finally {
            httpClient.close();
            if (response != null)
                response.close();
        }
    }
}