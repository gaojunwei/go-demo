package com.gjw.common.utils.filedownload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataInfo {
    /** 下载地址 */
    private String url;

    /** Range 起始下标 */
    private Long startIndex;
    /** Range 终止下标 */
    private Long endIndex;
    /** 分片下载文件的排序值 */
    private Integer index;

    /** 下载的文件大小,单位字节 */
    private Long contentLength;
    /** 下载的文件类型 */
    private String contentType;
    /** 文件类型后缀 */
    private String suffix;

    /** 文件名称 */
    private String mainFileName;
    /** 分片文件存放位置 */
    private String filePath;

    /** 下载结果true：成功；false：失败 */
    private Boolean status;

    /** 下载失败后重试次数 */
    private Short retryTimes;
    /** 备注描述 */
    private String remark;

    public DataInfo() {
        this.retryTimes = 0;
    }
}
