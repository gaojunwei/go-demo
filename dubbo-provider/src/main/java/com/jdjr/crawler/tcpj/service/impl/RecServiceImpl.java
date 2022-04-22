package com.jdjr.crawler.tcpj.service.impl;

import com.jdjr.crawler.tcpj.service.RecServiceApi;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecServiceImpl implements RecServiceApi {

    @Override
    public String doWork(String param) {
        return "hi," + param;
    }
}
