package com.gjw.go.common.result

import com.gjw.go.common.enums.SysCodeEnums

open class BasicRespDto {
    var code: String? = null
    var msg: String? = null
    var sn:String? = null

    companion object {
        fun success(): BasicRespDto {
            return BasicRespDto().apply {
                code = SysCodeEnums.SUCCESS.code
                msg = SysCodeEnums.SUCCESS.msg
            }
        }

        fun error(code: String?, msg: String?): BasicRespDto {
            return BasicRespDto().apply {
                this.code = code ?: SysCodeEnums.ERROR.code
                this.msg = msg ?: SysCodeEnums.ERROR.msg
            }
        }
    }
}