package com.gjw.common.utils.filedownload;

import lombok.Data;

import java.util.List;

@Data
public class FileDownLoadResult {
    private List<DataInfo> dataInfos;
    private String filePath;
}
