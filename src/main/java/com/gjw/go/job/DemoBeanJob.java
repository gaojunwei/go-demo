package com.gjw.go.job;

import com.alibaba.fastjson2.JSON;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoBeanJob {

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("job01")
    public void job01() {
        String param = XxlJobHelper.getJobParam();
        log.info("DemoBeanJob job01 任务触发执行 参数：{}", JSON.toJSONString(param));
    }
}
