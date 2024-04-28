package com.gjw.innovation.common.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PageResult<T> extends ListResult<T>{
    private int pageNo;
    private int pageSize;
    private long totalCount;
}
