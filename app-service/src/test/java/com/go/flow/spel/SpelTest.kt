package com.go.flow.spel

import com.alibaba.fastjson2.JSON
import com.go.starter.core.utils.ProcessAnalysisUtil
import org.junit.jupiter.api.Test
import org.springframework.expression.spel.support.StandardEvaluationContext


class SpelTest {

    @Test
    fun test001(){
        val context = StandardEvaluationContext()

        val map = mutableMapOf<String,Any>("laoshi" to JSON.toJSONString(mutableListOf("1","2")))
        map["a"] ="6"
        map["b"] ="7"
        map["c"] ="8"
        map["leaveDays"] ="8"

        Integer.valueOf("1")

        for ((key, value) in map) {
            context.setVariable(key, value)
        }
        println(ProcessAnalysisUtil.eval("T(Integer).parseInt(#leaveDays) > 7 ", map) as String?)
        println(ProcessAnalysisUtil.eval("T(Integer).parseInt(#leaveDays) > 7 ", map) as String?)
        println(ProcessAnalysisUtil.eval("T(Integer).parseInt(#leaveDays) > 7 ", map) as String?)


        println(ProcessAnalysisUtil.eval("1==1", map) as Boolean?)
       /* val dd = ProcessAnalysisUtil.eval("#a == #b",map) as Boolean?
        println(dd)
        val cc = ProcessAnalysisUtil.eval("#aMap['aad'] == #bMap['bb']",map) as Boolean?
        println(cc)*/
    }
}