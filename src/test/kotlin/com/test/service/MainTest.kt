package com.test.service

import cn.hutool.core.util.IdUtil
import com.alibaba.fastjson2.JSON
import com.gjw.go.excel.listener.student.StudentImpDto
import com.gjw.go.mapstruct.StudentMapper
import org.junit.jupiter.api.DisplayName
import java.time.LocalDateTime
import kotlin.test.Test

class MainTestKt {
    @Test
    fun mainssss() {


        var studentImpDto = StudentImpDto().apply {
            name = "小明"
            age = 18
            schoolName = "中山大学"
            clzz = "初一三班"
            teacherName = "刘能"
            createTime = LocalDateTime.now()
        }

        println("studentImpDto = " + JSON.toJSONString(studentImpDto))
        var studentPo = StudentMapper.INSTANCE.studentImpDtoToStudentPo(studentImpDto)
        println("    studentPo = " + JSON.toJSONString(studentPo))
    }
}