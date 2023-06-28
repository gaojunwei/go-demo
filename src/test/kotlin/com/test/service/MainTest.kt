package com.test.service

import com.alibaba.fastjson2.JSON
import com.gjw.go.common.result.BasicRespDto
import com.gjw.go.domain.StudentPo

class MainTest

fun main() {
    var studentPo:StudentPo = StudentPo().apply {
        name = "小明"
        age = 18
        school = "中山大学"
        clzz = "初一三班"
        teacher = "刘能"
    }

    println(JSON.toJSONString(studentPo))

    var sss = BasicRespDto.error(null,"文档")

    println(JSON.toJSONString(sss))

    val tempFolder = System.getProperty("java.io.tmpdir")
    println(tempFolder)

}