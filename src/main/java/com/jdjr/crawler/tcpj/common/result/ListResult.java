package com.jdjr.crawler.tcpj.common.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class ListResult<T> extends BasicResult {
    private List<T> data;
}
