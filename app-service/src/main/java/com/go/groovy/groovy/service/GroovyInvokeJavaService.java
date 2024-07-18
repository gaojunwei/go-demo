package com.go.groovy.groovy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Groovy脚本 调用spring bean 案例
 */
@Service
@Slf4j
public class GroovyInvokeJavaService {
    public String groovyInvokeJava() {
        return "Groovy脚本 调用spring bean 无参数数据成功";
    }

    public String groovyInvokeJavaParam(int a, int b) {
        return "Groovy脚本 调用spring bean 有参数数据成功 a=" + a + " b=" + b;
    }
}
