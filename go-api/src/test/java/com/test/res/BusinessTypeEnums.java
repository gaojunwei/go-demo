package com.test.res;

/**
 * 任务类型枚举值
 */
public enum BusinessTypeEnums {
    /**
     * 临期任务
     */
    SoonExpire("SoonExpireTask","com.jd.jr.innovation.epl.web.task.SoonExpireTask"),
    /**
     * 安全库存任务
     */
    StockWarning("StockWarningTask","com.jd.jr.innovation.epl.web.task.StockWarningTask");

    private String shortName;
    private String fullName;
    BusinessTypeEnums(String shortName, String fullName){
        this.shortName = shortName;
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }
}
