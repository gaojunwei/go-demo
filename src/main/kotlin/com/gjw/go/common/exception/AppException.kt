package com.gjw.go.common.exception

import com.gjw.go.common.enums.SysCodeEnums

class AppException : RuntimeException() {
    var code: String? = null
    var msg: String? = null

    companion object {
        fun error(msg:String?):AppException{
            return AppException().apply {
                this.code = SysCodeEnums.ERROR.code
                this.msg = msg?:SysCodeEnums.ERROR.msg
            }
        }
        fun assertNotNull(any: Any,msg: String){
            if(any == null){
                throw error(msg)
            }
        }
    }

    override fun toString(): String {
        return "AppException(code=$code, msg=$msg)"
    }
}