package com.gjw.go.common.result;

import com.gjw.go.common.enums.SysCodeEnums;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResponseDto<T> extends BasicResponseDto {
    T data;

    public SingleResponseDto(String code, String msg, T data) {
        super(code, msg);
        this.data = data;
    }

    public static <T> SingleResponseDto<T> success(T data) {
        return new SingleResponseDto(SysCodeEnums.SUCCESS.getCode(), SysCodeEnums.SUCCESS.getMsg(), data);
    }
}
