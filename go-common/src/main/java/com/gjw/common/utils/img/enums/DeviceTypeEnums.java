package com.gjw.common.utils.img.enums;

/**
 * 设备屏幕类型
 */
public enum DeviceTypeEnums {
    /**
     * 黑白屏
     */
    DEVICE_BW(0),
    /**
     * 黑白红屏
     */
    DEVICE_BWR(1),
    /**
     * 黑白黄屏
     */
    DEVICE_BWY(2);

    private int value;

    DeviceTypeEnums(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
