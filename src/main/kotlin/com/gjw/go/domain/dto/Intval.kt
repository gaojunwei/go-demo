package com.gjw.go.domain.dto

data class Intval(
    val day: Long,
    val interval: Int,
    var generateId: Long,
){
    companion object{
        @JvmStatic
        fun instance(day: Long,interval: Int,generateId: Long):Intval{
            return Intval(day,interval,generateId)
        }
    }
}