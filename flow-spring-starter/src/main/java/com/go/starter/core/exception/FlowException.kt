package com.go.starter.core.exception

import java.io.Serial

/**
 * 自定义异常
 */
class FlowException : RuntimeException() {
    val code: Int = 9
    override var message: String = ""

    companion object {
        @Serial
        private val serialVersionUID = 1L

        fun assertFalse(expression: Boolean, msg: String) {
            if (expression) {
                throw FlowException().apply {
                    this.message = msg
                }
            }
        }

        fun throwException(msg: String) {
            throw FlowException().apply {
                this.message = msg
            }
        }
    }
}