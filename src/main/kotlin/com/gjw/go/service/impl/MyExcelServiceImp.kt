package com.gjw.go.service.impl

import com.gjw.go.common.log.log
import com.gjw.go.domain.entity.StudentPo
import com.gjw.go.service.EasyExcelService
import com.gjw.go.service.data.StudentData
import org.springframework.stereotype.Component

@Component
class MyExcelServiceImp : EasyExcelService {
    override fun listAll(): List<StudentPo> {
        log.info("MyExcelServiceImp  listAll")
        return StudentData.getAll()
    }

    override fun getById(id: Int): StudentPo? {
        log.info("MyExcelServiceImp  getById")
        return StudentData.getById(id)
    }
}