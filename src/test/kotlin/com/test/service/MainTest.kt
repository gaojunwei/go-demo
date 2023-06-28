package com.test.service

import com.alibaba.fastjson2.JSON
import com.gjw.go.excel.listener.student.StudentImpDto
import com.gjw.go.mapstruct.StudentMapper
import org.mapstruct.factory.Mappers

class MainTest

//https://www.baeldung.com/kotlin/mapstruct-data-classes
//https://juejin.cn/s/kotlin%20mapstruct%20maven
fun main() {
    var studentImpDto = StudentImpDto().apply {
        name = "小明"
        age = 18
        school = "中山大学"
        clzz = "初一三班"
        teacher = "刘能"
    }

    var userMapper: StudentMapper = Mappers.getMapper(StudentMapper::class.java)

    var studentPo = userMapper.studentImpDtoToStudentPo(studentImpDto)
    println(JSON.toJSONString(studentPo))

}