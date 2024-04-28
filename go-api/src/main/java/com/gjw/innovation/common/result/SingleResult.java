package com.gjw.innovation.common.result;

import lombok.Data;

@Data
public class SingleResult<T> extends BasicResult {
    T data;
}
