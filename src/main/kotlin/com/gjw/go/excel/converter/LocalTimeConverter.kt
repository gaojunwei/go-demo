package com.gjw.go.excel.converter

import com.alibaba.excel.converters.Converter
import com.alibaba.excel.converters.ReadConverterContext
import com.alibaba.excel.converters.WriteConverterContext
import com.alibaba.excel.enums.CellDataTypeEnum
import com.alibaba.excel.metadata.data.WriteCellData
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LocalTimeConverter : Converter<LocalTime> {
    override fun supportJavaTypeKey(): Class<*> {
        return super.supportJavaTypeKey()
    }

    override fun supportExcelTypeKey(): CellDataTypeEnum {
        return super.supportExcelTypeKey()
    }

    override fun convertToJavaData(context: ReadConverterContext<*>?): LocalTime {
        var time = context?.readCellData?.stringValue
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"))
    }

    override fun convertToExcelData(context: WriteConverterContext<LocalTime>?): WriteCellData<*> {
        return super.convertToExcelData(context)
    }
}