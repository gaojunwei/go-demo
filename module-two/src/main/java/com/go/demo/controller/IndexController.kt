package com.go.demo.controller

import cn.hutool.core.util.IdUtil
import com.go.common.extension.log
import com.go.common.response.R
import com.go.demo.service.IndexService
import jakarta.annotation.Resource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class IndexController {

    @Resource
    private lateinit var indexService: IndexService

    @GetMapping("bulkhead")
    fun extractTextFromImage(): R<String> {
        var id = IdUtil.nanoId()
        var msg = indexService.processOne(id)
        log.info("响应数据: {}", msg)
        return R<String>().apply {
            this.data = msg
        }
    }
}