package com.gjw.go.excel.listener.student

import com.alibaba.excel.annotation.ExcelProperty
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.gjw.go.excel.converter.LocalDateTimeConverter
import com.gjw.go.excel.converter.LocalTimeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * 导入模板
 */
open class StudentImpDto{
    //学生ID
    @ExcelProperty("学生ID")
    @JsonSerialize(using = ToStringSerializer::class)
    var id: Long? = null

    //姓名
    @ExcelProperty("姓名")
    var name: String? = null

    //年龄
    @ExcelProperty("年龄")
    var age: Int? = null

    //学校
    @ExcelProperty("学校")
    var schoolName: String? = null

    //班级
    @ExcelProperty("班级")
    var clzz: String? = null

    //班主任
    @ExcelProperty("班主任")
    var teacherName: String? = null

    //生日
    @ExcelProperty("生日")
    var birthDay: LocalDate? = null

    //出生时间点
    @ExcelProperty("出生时间点", converter = LocalTimeConverter::class)
    //@ExcelIgnore
    var birthTime: LocalTime? = null

    //注册日期
    @ExcelProperty("注册日期", converter = LocalDateTimeConverter::class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createTime: LocalDateTime? = null
}