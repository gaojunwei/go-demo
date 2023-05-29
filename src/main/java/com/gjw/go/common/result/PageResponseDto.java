package com.gjw.go.common.result;

import com.gjw.go.common.enums.SysCodeEnums;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PageResponseDto<T> extends ListResponseDto<T> {
    private int pageNo;
    private int pageSize;
    private long totalCount;

    public PageResponseDto(int pageNo, int pageSize, long totalCount, String code, String msg, List<T> data) {
        super(code, msg, data);
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
    }

    public static <T> PageResponseDto<T> success(int pageNo, int pageSize, long totalCount, List<T> data) {
        return new PageResponseDto(pageNo, pageSize, totalCount, SysCodeEnums.SUCCESS.getCode(), SysCodeEnums.SUCCESS.getMsg(), data);
    }
}
