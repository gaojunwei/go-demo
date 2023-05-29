package com.gjw.go.common.result;

import com.alibaba.fastjson2.JSON;

import java.io.Serializable;

/**
 * @author ext.gaojunwei1
 * @date 2023/5/29
 */
public abstract class AbstractDto implements Serializable {
    private static final long serialVersionUID = 1915714417292764241L;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
