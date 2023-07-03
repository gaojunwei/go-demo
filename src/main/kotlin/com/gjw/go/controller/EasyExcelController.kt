package com.gjw.go.controller

import com.alibaba.excel.EasyExcel
import com.alibaba.fastjson2.JSON
import com.gjw.go.common.enums.SysCodeEnums
import com.gjw.go.common.exception.AppException
import com.gjw.go.common.inline.log
import com.gjw.go.common.result.BasicRespDto
import com.gjw.go.common.result.ListRespDto
import com.gjw.go.common.result.SingleRespDto
import com.gjw.go.common.validation.AddGroup
import com.gjw.go.domain.dto.StudentAddDTO
import com.gjw.go.domain.entity.StudentPo
import com.gjw.go.excel.listener.StudentListener
import com.gjw.go.excel.listener.student.StudentImpDto
import com.gjw.go.service.EasyExcelService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
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
    fun addStudent(@RequestBody @Validated(value = [AddGroup::class]) studentPo: StudentAddDTO): BasicRespDto {
        log.info("添加学生 请求参数:{}", JSON.toJSONString(studentPo))
        return BasicRespDto.success()
    }

    /**
     * 查询所有学生
     */
    @GetMapping("list/all")
    fun listAll(): ListRespDto<StudentPo> {
        log.info("查询所有学生")
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
    fun getById(@PathVariable id: Int): SingleRespDto<StudentPo> {
        log.info("查询单个学生信息 id:{}", id)
        return SingleRespDto<StudentPo>().apply {
            code = SysCodeEnums.SUCCESS.code
            msg = SysCodeEnums.SUCCESS.msg
            data = easyExcelService.getById(id)
        }
    }

    /**
     * 学生信息excel批量导入
     */
    @PostMapping("/import")
    fun batchImport(@RequestParam("file") file: MultipartFile): ListRespDto<StudentImpDto> {
        log.info("学生信息excel批量导入 fileName:{}", file.originalFilename)
        return ListRespDto<StudentImpDto>().apply {
            code = SysCodeEnums.SUCCESS.code
            msg = SysCodeEnums.SUCCESS.msg
            data = EasyExcel.read(file.inputStream, StudentImpDto::class.java, StudentListener<StudentImpDto>()).sheet()
                .headRowNumber(1).doReadSync()
        }
    }
}