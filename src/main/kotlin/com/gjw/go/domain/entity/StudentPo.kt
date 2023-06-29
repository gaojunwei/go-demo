package com.gjw.go.domain.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * 学生信息
 */
open class StudentPo{
    //学生ID
    @JsonSerialize(using = ToStringSerializer::class)
    var id: Long? = null

    //姓名
    var name: String? = null

    //年龄
    var age: Int? = null

    //学校
    var school: String? = null

    //班级
    var clzz: String? = null

    //班主任
    var teacher: String? = null

    //生日
    var birthDay: LocalDate? = null

    //出生时间点
    @JsonFormat(pattern = "HH:mm:ss")
    var birthTime: LocalTime? = null

    //注册日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createTime: LocalDateTime? = null
}