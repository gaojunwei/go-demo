package com.gjw.go.domain.dto

import java.time.LocalDate

data class Order(
    //订单原价⾦额
    var amout: Long,
    //积分
    var score: Long,

    var date:LocalDate?
)