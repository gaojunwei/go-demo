package com.gjw.common.result;

import lombok.Data;

import java.util.List;

@Data
public class ListResult<T> extends BasicResult {
    private List<T> data;
}
