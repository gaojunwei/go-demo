package com.go.controller

import com.alibaba.fastjson2.JSON
import com.go.starter.service.IProcessService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class IndexController(
    private val processService: IProcessService
) {
    @GetMapping("insert")
    fun one(): String {
        processService.deploy("xxxxxxxxxx")
        val sss = processService.detailByProcessKeyForce("process_key_001")
        return "success -> ${JSON.toJSONString(sss)}"
    }
}