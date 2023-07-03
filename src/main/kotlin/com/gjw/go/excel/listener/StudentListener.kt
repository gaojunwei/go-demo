package com.gjw.go.excel.listener

import com.alibaba.excel.context.AnalysisContext
import com.alibaba.excel.exception.ExcelDataConvertException
import com.alibaba.excel.read.listener.ReadListener
import com.alibaba.fastjson2.JSON
import com.gjw.go.common.exception.AppException
import com.gjw.go.common.inline.log


class StudentListener<T> : ReadListener<T> {

    /**
     * 缓存的数据
     */
    private var cachedDataList = mutableListOf<T>()

    /**
     * 这个每一条数据解析都会来调用
     */
    override fun invoke(data: T, p1: AnalysisContext?) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        cachedDataList.add(data);
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    override fun doAfterAllAnalysed(p0: AnalysisContext?) {
        log.info("数据解析完成 {}", JSON.toJSONString(cachedDataList))
    }

    /**
     * 抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行
     */
    override fun onException(exception: Exception?, context: AnalysisContext?) {
        log.error("excel解析失败 数据行号：{}", context?.readRowHolder()?.rowIndex)
        // 如果是某一个单元格的转换异常 能获取到具体行号
        if (exception is ExcelDataConvertException) {
            log.error("excel解析失败 第{}行，第{}列解析异常", exception.rowIndex, exception.columnIndex)
            throw AppException.error("excel解析失败 第${exception.rowIndex}行，第${exception.columnIndex}列解析异常")
        }
        throw AppException.error("excel解析失败 第${context?.readRowHolder()?.rowIndex}行解析异常")
    }
}