package com.go.flow.form

import com.go.starter.core.form.BaseForm

class TestForm:BaseForm {
    override fun fallback(instanceNo: String) {
        println("表单回滚 instanceNo:$instanceNo")
    }
}