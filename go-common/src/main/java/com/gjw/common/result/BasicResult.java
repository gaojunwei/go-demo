package com.gjw.common.result;

import lombok.Data;

import java.io.Serializable;


@Data
public class BasicResult implements Serializable {
    private static final long serialVersionUID = -5809782578272943999L;
    private String code;
    private String msg;

    public static BasicResult instance(String code,String msg){
        BasicResult result = new BasicResult();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
