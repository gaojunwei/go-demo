package com.gjw.go.controller

import com.gjw.go.common.exception.AppException
import com.gjw.go.common.inline.log
import com.gjw.go.common.result.BasicRespDto
import com.gjw.go.common.result.ListRespDto
import com.gjw.go.common.result.SingleRespDto
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("test")
class TestController {

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
    fun addStudent(): BasicRespDto {
        log.info("添加学生 请求参数:{}")
        return BasicRespDto.success()
    }

    /**
     * 查询所有学生
     */
    @GetMapping("list/all")
    fun listAll(): ListRespDto<String>? {
        log.info("查询所有学生")
        return null
    }

    /**
     * 查询单个学生信息
     */
    @GetMapping("{id}")
    fun getById(@PathVariable id: Int): SingleRespDto<String>? {
        log.info("查询单个学生信息 id:{}", id)
        return null
    }

    /**
     * 学生信息excel批量导入
     */
    @PostMapping("/import")
    fun batchImport(@RequestParam("file") file: MultipartFile): ListRespDto<String>? {
        log.info("学生信息excel批量导入 fileName:{}", file.originalFilename)
        return null
    }
}