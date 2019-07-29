package com.gjw.common.result;

import lombok.Data;

@Data
public class SingleResult<T> extends BasicResult {
    T data;
}
