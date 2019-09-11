package com.test;

import lombok.Data;

/**
 * @author: gaojunwei
 * @Date: 2018/11/20 16:47
 */
@Data
public class DeviceVersionPublishParam {
    private String deviceType;
    private String version;
    private String fileUrl;
    private String fileKey;
    private String fileMd5;

    private String remark;
    private Byte deviceGroup;
    private Byte ifForce;

    private String operator;
    private Long operatorId;
}