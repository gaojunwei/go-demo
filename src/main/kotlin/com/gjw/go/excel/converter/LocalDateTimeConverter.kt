package com.gjw.go.excel.converter

import com.alibaba.excel.converters.Converter
import com.alibaba.excel.converters.ReadConverterContext
import com.alibaba.excel.converters.WriteConverterContext
import com.alibaba.excel.enums.CellDataTypeEnum
import com.alibaba.excel.metadata.data.WriteCellData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter : Converter<LocalDateTime> {
    override fun supportJavaTypeKey(): Class<*> {
        return super.supportJavaTypeKey()
    }

    override fun supportExcelTypeKey(): CellDataTypeEnum {
        return super.supportExcelTypeKey()
    }

    override fun convertToJavaData(context: ReadConverterContext<*>?): LocalDateTime {
        var dateTime = context?.readCellData?.stringValue
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

    override fun convertToExcelData(context: WriteConverterContext<LocalDateTime>?): WriteCellData<*> {
        return super.convertToExcelData(context)
    }
}