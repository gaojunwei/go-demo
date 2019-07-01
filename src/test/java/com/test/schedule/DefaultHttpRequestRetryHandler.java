package com.test.schedule;

import com.wangyin.schedule.httpclient.HttpRequestRetryHandler;
import com.wangyin.schedule.sdk.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author: gaojunwei
 * @Date: 2019/6/6 9:10
 * @Description:
 */
public class DefaultHttpRequestRetryHandler implements HttpRequestRetryHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultHttpRequestRetryHandler.class);

    @Override
    public boolean retryRequest(Request request, IOException exception, int executionCount) {
        return false;
    }
}