package com.test.jpa;

import lombok.Data;

import java.util.Map;

/**
 * 存储返回结果对象
 */
@Data
public class ResultDto {
    private String returnCode;
    private String returnMessage;
    private String sn;
    private Map<String,Map<String,Object>> data;
}
