package com.jdjr.crawler.tcpj.common.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SingleResult<T> extends BasicResult {
    T data;
}
