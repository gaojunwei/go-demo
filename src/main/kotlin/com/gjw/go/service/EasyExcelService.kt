package com.gjw.go.service

import com.gjw.go.domain.entity.StudentPo

interface EasyExcelService {
    fun listAll(): List<StudentPo>
    fun getById(id: Int): StudentPo?
}