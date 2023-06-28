package com.gjw.go.job;

import com.alibaba.fastjson2.JSON;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoJobHandler extends IJobHandler {

    @Override
    public void execute() throws Exception {
        String param = XxlJobHelper.getJobParam();
        log.info("DemoJobHandler 任务触发执行 参数：{}", JSON.toJSONString(param));
    }
}
