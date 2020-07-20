package com.jdjr.crawler.tcpj.common.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PageResult<T> extends ListResult<T> {
    private int pageNo;
    private int pageSize;
    private long totalCount;
}
