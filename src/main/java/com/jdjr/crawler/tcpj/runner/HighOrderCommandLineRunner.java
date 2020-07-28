package com.jdjr.crawler.tcpj.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jdjr.crawler.tcpj.common.enums.BusinessEnums;
import com.jdjr.crawler.tcpj.schedule.BIHUCatch;
import com.jdjr.crawler.tcpj.schedule.TCPJCatch;
import com.jdjr.crawler.tcpj.schedule.data.BaseData;
import com.jdjr.crawler.tcpj.schedule.data.TcpjData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 自定义启动运行逻辑
 * <p>
 * 方法可以看出来参数都不一样，额外科普一下 Spring Boot 如何传递额外参数通过命令行
 * 执行 java -jar 传递给 main 方法，规则如下键值对 格式为 --K=V
 * 多个使用空格隔开值 多个空格隔开
 *
 * @author gaojunwei
 * @date 2019/11/4 10:52
 */
@Slf4j
@Component
public class HighOrderCommandLineRunner implements CommandLineRunner, Ordered {

    @Value("${app.version}")
    private String appVersion;

    @Override
    public void run(String... args) {
        /**设置系统变量指定证书位置*/
        System.setProperty("javax.net.ssl.trustStore", "D:/proxy/cacerts");
        /**刷新本地缓存文件数据到内存中*/
        initCatch();
        logger.info("系统启动成功，当前系统版本号：{}", appVersion);
        logger.info("System start success,version:{}", appVersion);

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * 将缓存文件中的数据读到缓存中
     */
    private void initCatch() {
        FileReader reader = null;
        BufferedReader bReader = null;
        try {
            File folder = new File("catch" + File.separator);
            if (!folder.exists()) {
                return;
            }
            File[] files = folder.listFiles();
            if (files.length == 0) {
                return;
            }
            Optional<File> fileObj = Arrays.stream(files).filter(file -> file.getName().endsWith(".json")).sorted(Comparator.comparing(File::lastModified).reversed()).findFirst();
            if (!fileObj.isPresent())
                return;
            File catchFile = fileObj.get();
            reader = new FileReader(catchFile);
            bReader = new BufferedReader(reader);
            String catchStr = bReader.readLine();

            JSONObject jsonObject = JSON.parseObject(catchStr);
            JSONArray tcpjArray = jsonObject.getJSONArray(BusinessEnums.TCPJ.getValue());
            if (tcpjArray != null && !tcpjArray.isEmpty()) {
                List<TcpjData> dataList = JSON.parseArray(tcpjArray.toJSONString(), TcpjData.class);
                //添加到缓存中
                for (TcpjData data : dataList) {
                    TCPJCatch.applyValue("system_srat_init", data.getPhone(), data.getPhoneType(), data.getToken(),data.getIsUsed(), data.getCreatTime());
                }
                //清除过期数据
                TCPJCatch.checkExpire();
            }
            JSONArray bihuArray = jsonObject.getJSONArray(BusinessEnums.BIHU.getValue());
            if (bihuArray != null && !bihuArray.isEmpty()) {
                List<BaseData> dataList = JSON.parseArray(bihuArray.toJSONString(), BaseData.class);
                //添加到缓存中
                for (BaseData data : dataList) {
                    BIHUCatch.applyValue("system_srat_init", data.getPhone(), data.getToken(), data.getIsUsed(),data.getCreatTime());
                }
                //清除过期数据
                BIHUCatch.checkExpire();
            }
            logger.info("read catch file:{} success!!!!", catchFile.getName());
            //释放文件句柄
            reader.close();
            bReader.close();
            //清空缓存文件
            for (File file : files) {
                logger.info("delete file:{}", file.getName());
                file.delete();
            }
        } catch (IOException e) {
            logger.warn("读取缓存文件中数据，刷新到缓存中失败，{}", e.getMessage(), e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (bReader != null)
                    bReader.close();
            } catch (IOException e) {
            }
        }
    }
}