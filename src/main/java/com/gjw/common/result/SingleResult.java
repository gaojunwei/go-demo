package com.gjw.common.result;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SingleResult<T> extends BasicResult {
    T data;

    public SingleResult<T> success(T t) {
        this.setCode("0");
        this.setMsg("success");
        this.setData(t);
        return this;
    }
}
