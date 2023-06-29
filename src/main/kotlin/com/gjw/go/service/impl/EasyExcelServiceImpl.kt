package com.gjw.go.service.impl

import com.gjw.go.domain.entity.StudentPo
import com.gjw.go.service.EasyExcelService
import com.gjw.go.service.data.StudentData
import org.springframework.stereotype.Component

@Component
class EasyExcelServiceImpl :EasyExcelService {
    override fun listAll(): List<StudentPo> {
        return StudentData.getAll()
    }

    override fun getById(id: Int): StudentPo? {
        return StudentData.getById(id)
    }
}