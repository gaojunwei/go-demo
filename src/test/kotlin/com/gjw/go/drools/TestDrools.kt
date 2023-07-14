package com.gjw.go.drools

import com.gjw.go.common.inline.insertAll
import com.gjw.go.domain.dto.Customer
import com.gjw.go.domain.dto.Order
import org.kie.api.KieServices
import kotlin.test.Test


/**
 * Drools测试
 */
class TestDrools {

    @Test
    fun test1() {
        //构造订单对象，设置订单⾦额，由规则引擎计算获得的积分
        var list = mutableListOf(
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

        //将数据交给规则引擎，规则引擎会根据提供的数据进⾏规则匹配
        kieSession.insertAll(list)
        //激活规则引擎，如果匹配成功则执⾏规则
        kieSession.fireAllRules()
        //关闭会话
        kieSession.dispose()
        //打印结果;
        list.forEach {
            println("计算结果  amout = ${it.amout} 获得积分:${it.score}")
        }
    }

    @Test
    fun test2() {
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
        kieSession.dispose()
    }
}