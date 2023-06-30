package com.test.service

import com.gjw.go.GoApp
import com.gjw.go.common.log.log
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [GoApp::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractSpringTest {

    @BeforeTest
    fun beforeTest(){
        log.info("BeforeTest 进行资源初始化操作")
    }

    @AfterTest
    fun afterTest(){
        log.info("AfterTest 进行资源释放操作")
    }
}