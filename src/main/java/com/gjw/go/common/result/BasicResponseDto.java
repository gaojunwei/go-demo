package com.gjw.go.common.result;

import com.gjw.go.common.enums.SysCodeEnums;
import lombok.Getter;
import lombok.Setter;

/**
 * 同意响应数据模型
 */
@Setter
@Getter
public class BasicResponseDto extends AbstractDto {
    private String code;
    private String msg;


    public static BasicResponseDto success() {
        return new BasicResponseDto(SysCodeEnums.SUCCESS.getCode(), SysCodeEnums.SUCCESS.getMsg());
    }

    public static BasicResponseDto error() {
        return new BasicResponseDto(SysCodeEnums.ERROR.getCode(), SysCodeEnums.ERROR.getMsg());
    }

    public static BasicResponseDto error(String msg) {
        return new BasicResponseDto(SysCodeEnums.ERROR.getCode(), msg);
    }

    public BasicResponseDto(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
