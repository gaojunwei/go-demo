package com.gjw.go.service.impl

import com.gjw.go.common.inline.log
import com.gjw.go.domain.entity.StudentPo
import com.gjw.go.service.EasyExcelService
import com.gjw.go.service.data.StudentData
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@Primary
class EasyExcelServiceImpl : EasyExcelService {
    override fun listAll(): List<StudentPo> {
        log.info("EasyExcelServiceImpl  listAll")
        return StudentData.getAll()
    }

    override fun getById(id: Int): StudentPo? {
        log.info("EasyExcelServiceImpl  getById")
        return StudentData.getById(id)
    }
}