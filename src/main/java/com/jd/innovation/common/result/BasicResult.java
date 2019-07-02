package com.jd.innovation.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


@Data
@AllArgsConstructor
public class BasicResult implements Serializable {
    private static final long serialVersionUID = -5809782578272943999L;
    private String code;
    private String msg;

    public static BasicResult instance(String code,String msg){
        return new BasicResult(code,msg);
    }
}
