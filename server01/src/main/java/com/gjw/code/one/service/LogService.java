package com.gjw.code.one.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class LogService {

    public Flux<Object> grepWithLinuxCommand(String filePath, String keyword) {
        AtomicBoolean cancelled = new AtomicBoolean(false);
        return Flux.create(sink -> {
            try {
                // 构造命令：grep "keyword" /path/to/file.log
                ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "grep -a '" + keyword + "' " + filePath);
                processBuilder.redirectErrorStream(true); // 合并标准输出和错误输出
                Process process = processBuilder.start();

                // 读取命令输出
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null && !cancelled.get()) {
                    sink.next(line); // 直接使用 line，因为 BufferedReader.readLine() 返回 String
                }

                int exitCode = process.waitFor();
                if (exitCode == 0 || cancelled.get()) {
                    sink.complete();
                } else {
                    sink.error(new RuntimeException("命令执行失败，退出码：" + exitCode));
                }

            } catch (Exception e) {
                sink.error(e);
            }
        }).doOnCancel(() -> cancelled.set(true));
    }
}