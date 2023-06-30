package com.test.service

import com.gjw.go.common.utils.SpringUtils
import com.gjw.go.service.EasyExcelService
import kotlin.test.Test

class SpringUtilsTest : AbstractSpringTest() {

    @Test
    fun test() {
        SpringUtils.getBean<EasyExcelService>("easyExcelServiceImpl").listAll()
        Thread.sleep(1000)
        SpringUtils.getBean<EasyExcelService>("myExcelServiceImp").listAll()
        Thread.sleep(1000)
    }

    @Test
    fun test33() {
        SpringUtils.getBean("easyExcelServiceImpl", EasyExcelService::class.java)?.listAll()
        Thread.sleep(1000)
        SpringUtils.getBean("myExcelServiceImp", EasyExcelService::class.java)?.listAll()
        Thread.sleep(1000)
    }

    @Test
    fun test3ee3() {
        var list = SpringUtils.getBeansOfType(EasyExcelService::class.java)
        list.values.forEach {
            it.listAll()
            println("---------")
        }
    }

}