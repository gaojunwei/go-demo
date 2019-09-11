package com.test;

import lombok.Data;

import java.util.Date;

/**
 * @author: gaojunwei
 * @Date: 2018/11/20 16:47
 * @Description: 发布设备软件版本信息
 */
@Data
public class DeviceVersionHistoryParam {
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页显示数量
     */
    private Integer pageSize;
    /**
     * 开始时间
     */
    private Date startDate;
    /**
     * 结束时间
     */
    private Date endDate;
    /**
     * 天午：ca、ap、epl
     */
    private String deviceType;
}