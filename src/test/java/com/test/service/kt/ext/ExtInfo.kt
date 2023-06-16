package com.test.service.kt.ext

import com.test.service.kt.StudentVo
import java.time.LocalDateTime
import kotlin.reflect.KProperty

typealias Say = StudentVo

//扩展函数
fun Say.sayBye(name: String): Say = apply {
    println("A内联函数 ${this.name} say bye bye to $name")
}

fun Say.sayByeTwo(name: String): Unit {
    this.name = "嘻嘻"
    return
}

fun Say.defaultAge() = apply {
    age = 10000
}

fun Say.doWork(fun001: (String?, Int?) -> String): String {
    return fun001.invoke(this.name, age)
}
//Int类型判断是否为偶数
inline fun Int.isEven():Boolean{
    return this%2==0
}

inline fun <T : Any> T?.ifNotNull(block: (T) -> Unit) {
    if (this != null) block(this)
}






































































































































































































































































































































































































































































































































