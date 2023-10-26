package com.gjw.innovation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class WxLoginOpenIdVo {
    private String openid;
    private String sessionKey;
    private String token;
}
