package com.gjw.go.mapstruct

import com.gjw.go.domain.StudentPo
import com.gjw.go.excel.listener.student.StudentImpDto
import org.mapstruct.Mapper


@Mapper
interface StudentMapper {

    fun studentImpDtoToStudentPo(studentImpDto: StudentImpDto): StudentPo
}