package com.test.service.kt

import com.test.service.kt.ext.defaultAge
import com.test.service.kt.ext.doWork
import com.test.service.kt.ext.sayBye
import com.test.service.kt.ext.sayByeTwo

interface SayInterface{
    fun sayHello(name:String):String
}
class StudentVo(var name: String?, var age: Int?) : SayInterface {
    override fun sayHello(name:String):String{
        return "${this.name} say hello to $name"
    }
}


fun main() {
    var student = StudentVo("张三",10)
    println("${student.sayHello("历史")}")
    println(student.sayBye("潇潇").sayHello("叶问"))
    student.sayByeTwo("老鬼")

    println("年龄修改前 ${student.age}")
    student.defaultAge()
    println("年龄修改后 ${student.age}")






    fun aa(p1:String?, p2:Int?):String{
        return "$p1 - $p2"
    }

    student.doWork(::aa)
}


