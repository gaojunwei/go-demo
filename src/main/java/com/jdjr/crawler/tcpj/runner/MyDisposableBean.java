package com.jdjr.crawler.tcpj.runner;

import com.alibaba.fastjson.JSON;
import com.jdjr.crawler.tcpj.common.enums.BusinessEnums;
import com.jdjr.crawler.tcpj.schedule.BIHUCatch;
import com.jdjr.crawler.tcpj.schedule.TCPJCatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述 结束的时候执行
 *
 * @Author gaojunwei
 * @Date 2020/7/17 10:30
 **/
@Component
@Slf4j
public class MyDisposableBean implements DisposableBean {
    @Override
    public void destroy() {
        logger.info("程序定时任务执行日志记录：{}", JSON.toJSONString(TCPJCatch.getLog()));
        brushToFile();
    }

    /**
     * 将缓存数据刷新到文件中
     */
    private void brushToFile() {
        if (TCPJCatch.getCatch().isEmpty() && BIHUCatch.getCatch().isEmpty())
            return;
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File("catch" + File.separator);
            if (!file.exists())
                file.mkdirs();

            File catchFile = new File("catch" + File.separator + System.currentTimeMillis() + ".json");
            Map<String, List> dataMap = new HashMap<>();
            dataMap.put(BusinessEnums.TCPJ.getValue(), TCPJCatch.getCatch());
            dataMap.put(BusinessEnums.BIHU.getValue(), BIHUCatch.getCatch());
            String jsonStr = JSON.toJSONString(dataMap);
            fileOutputStream = new FileOutputStream(catchFile);
            fileOutputStream.write(jsonStr.getBytes());
            logger.warn("catch data flush to file SUCCESS {}", catchFile.getAbsolutePath());
        } catch (IOException e) {
            logger.warn("catch data flush to file exception {}", e.getMessage(), e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
