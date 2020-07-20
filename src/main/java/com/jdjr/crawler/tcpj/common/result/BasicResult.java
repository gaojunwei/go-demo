package com.jdjr.crawler.tcpj.common.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper=false)
public class BasicResult implements Serializable {

    private static final long serialVersionUID = -5809782578272943999L;
    
    private String code;
    private String msg;
    private String sn;
}
