package com.gjw.go.drools

import com.alibaba.fastjson2.JSON
import com.gjw.go.common.inline.insertAll
import com.gjw.go.domain.dto.Intval
import org.kie.api.KieServices
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.test.Test


/**
 * Drools测试
 */
class TestDrools {

    @Test
    fun test1() {
        //构造订单对象，设置订单⾦额，由规则引擎计算获得的积分
        var intval = Intval(1, 5, 1)

        var count = 0
        while (count<=50){

            intval = convert(intval)
            count++
        }


    }

    private fun convert(intval: Intval): Intval {
        val eqpMaintReminders = mutableListOf<Intval>()

        val kieServices = KieServices.Factory.get()
        val kieContainer = kieServices.kieClasspathContainer
        //会话对象,⽤于和规则引擎交互
        val session = kieContainer.newKieSession()

        session.setGlobal("intvalList", eqpMaintReminders)

        //将数据交给规则引擎，规则引擎会根据提供的数据进⾏规则匹配
        session.insert(intval)
        //激活规则引擎，如果匹配成功则执⾏规则
        //session.fireAllRules{it.rule.name.startsWith("score")};
        session.fireAllRules()
        //关闭会话
        session.dispose()
        //打印结果;
        println("计算结果   $intval   "+ JSON.toJSONString(eqpMaintReminders))
        return intval
    }

    @Test
    fun test2() {/*
        var orderList = mutableListOf(
            Order(100, 0),
            Order(200, 0),
            Order(300, 0),
            Order(400, 0),
            Order(500, 0),
            Order(600, 0)
        )

        val kieServices = KieServices.Factory.get()
        val kieContainer = kieServices.kieClasspathContainer
        //会话对象,⽤于和规则引擎交互
        val kieSession = kieContainer.newKieSession()
        //构造订单对象，设置订单⾦额，由规则引擎计算获得的积分
        //匹配规则：$order:Order();
        kieSession.insertAll(orderList)
        val customer = Customer()

        //匹配规则： $customer:Customer(orderList contains $order);
        //orderList.add(order);
        customer.orderList = orderList
        customer.name = "Jack"
        //将数据交给规则引擎，规则引擎会根据提供的数据进⾏规则匹配
        kieSession.insert(customer)
        //激活规则引擎，如果匹配成功则执⾏规则
        kieSession.fireAllRules()
        //关闭会话
        kieSession.dispose()*/
    }
}