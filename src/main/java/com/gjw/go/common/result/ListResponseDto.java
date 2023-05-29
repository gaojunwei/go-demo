package com.gjw.go.common.result;

import com.gjw.go.common.enums.SysCodeEnums;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListResponseDto<T> extends BasicResponseDto {
    private List<T> data;

    public ListResponseDto(String code, String msg, List<T> data) {
        super(code, msg);
        this.data = data;
    }

    public static <T> ListResponseDto<T> success(List<T> data) {
        return new ListResponseDto(SysCodeEnums.SUCCESS.getCode(), SysCodeEnums.SUCCESS.getMsg(), data);
    }
}
