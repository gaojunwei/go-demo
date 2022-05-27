package com.gjw.deme.service.rpc;

import com.jdjr.crawler.tcpj.service.RecServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("recService")
public class RecServiceApiIml implements RecServiceApi {

    @Override
    public String doWork(String param) {
        logger.info("收到的请求参数：{}",param);
        return "hi,"+param;
    }
}
