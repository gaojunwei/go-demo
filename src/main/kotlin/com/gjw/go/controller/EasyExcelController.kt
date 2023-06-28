package com.gjw.go.controller

import com.alibaba.fastjson2.JSON
import com.gjw.go.common.enums.SysCodeEnums
import com.gjw.go.common.exception.AppException
import com.gjw.go.common.log.log
import com.gjw.go.common.result.BasicRespDto
import com.gjw.go.common.result.ListRespDto
import com.gjw.go.common.result.SingleRespDto
import com.gjw.go.domain.StudentPo
import com.gjw.go.service.EasyExcelService
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource

@RestController
@RequestMapping("st")
class EasyExcelController {

    @Resource
    lateinit var easyExcelService: EasyExcelService

    /**
     * 抛出异常
     */
    @GetMapping("app/exception")
    fun doAppException(): BasicRespDto {
        throw AppException.error("自定义异常")
        return BasicRespDto.success()
    }

    @GetMapping("exception")
    fun doException(): BasicRespDto {
        var a = 1 / 0
        log.info("计算结果:{}", a)
        return BasicRespDto.success()
    }

    /**
     * 添加学生
     */
    @PostMapping
    fun addStudent(@RequestBody studentPo: StudentPo): BasicRespDto {
        log.info("添加学生 请求参数:{}", JSON.toJSONString(studentPo))
        return BasicRespDto.success()
    }

    /**
     * 查询所有学生
     */
    @GetMapping("list/all")
    fun listAll(): ListRespDto<StudentPo> {
        return ListRespDto<StudentPo>().apply {
            code = SysCodeEnums.SUCCESS.code
            msg = SysCodeEnums.SUCCESS.msg
            data = easyExcelService.listAll()
        }
    }

    /**
     * 查询单个学生信息
     */
    @GetMapping("{id}")
    fun getById(@PathVariable id:Int): SingleRespDto<StudentPo> {
        return SingleRespDto<StudentPo>().apply {
            code = SysCodeEnums.SUCCESS.code
            msg = SysCodeEnums.SUCCESS.msg
            data = easyExcelService.getById(id)
        }
    }
}