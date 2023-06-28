package com.gjw.go.excel.listener

import com.alibaba.excel.context.AnalysisContext
import com.alibaba.excel.read.listener.ReadListener
import com.alibaba.fastjson2.JSON
import com.gjw.go.common.log.log

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
}