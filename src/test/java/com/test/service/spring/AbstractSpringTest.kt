package com.test.service.spring

import com.gjw.go.GoApp
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [GoApp::class])
abstract class AbstractSpringTest