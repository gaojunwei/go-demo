package com.test.service.kt

import java.io.Serializable


data class Student(var name: String, var age: Int)

class StudentService {
    fun getStudent(name: String): Student = Student("小曼", 8)
}

class KLazilyDCLSingleton private constructor() : Serializable {//private constructor()构造器私有化

    fun doSomething() {
        println("start do some thing")

        val student: Student? by lazy {
            println("获取学生信息")
            if (1 == 2) {
                return@lazy null
                //throw Exception("该数据不存在")
            } else {
                return@lazy StudentService().getStudent("ss")
            }
        }

        println("start do some thing 2")
        println("start do some thing 3 $student")

        var student2 = student.apply {
            println("app")
            student?.name = "dd"
            student?.age = 9
        }

        println("start do some thing 4 $student  $student2 ${student === student2}")
        if (student?.age == 8) {
            println("合格")
        } else {
            println("不合规")
        }
        println("结束")
    }

    private fun readResolve(): Any {//防止单例对象在反序列化时重新生成对象
        return instance
    }

    companion object {
        //通过@JvmStatic注解，使得在Java中调用instance直接是像调用静态函数一样，
        //类似KLazilyDCLSingleton.getInstance(),如果不加注解，在Java中必须这样调用: KLazilyDCLSingleton.Companion.getInstance().
        //@JvmStatic
        //使用lazy属性代理，并指定LazyThreadSafetyMode为SYNCHRONIZED模式保证线程安全
        val instance: KLazilyDCLSingleton by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { KLazilyDCLSingleton() }

        val str: String by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            println("压缩")
            return@lazy "ss"
        }
    }
}

//在Kotlin中调用，直接通过KLazilyDCLSingleton类名调用instance
fun main(args: Array<String>) {
    //KLazilyDCLSingleton.instance.doSomething()



    val map = mutableMapOf<String,Any>()
    map["k1"] = "b"
    map["k3"] = "b"
    map["k2"] = 2

    var ss = map.put("k3","new b")
    println(ss)
    println(map)

    var keyList = map.map { it.key }.toSet()
    println(keyList)

    //niming(age = "age")
    //niming(name = null)
}


fun niming(name: String? = "默认值", age: String = "") {
    println("打印 [$name - $age]")
}