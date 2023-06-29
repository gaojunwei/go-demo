package com.gjw.go.mapstruct

import com.gjw.go.domain.StudentPo
import com.gjw.go.excel.listener.student.StudentImpDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers


@Mapper
interface StudentMapper {
    companion object {
        var INSTANCE = Mappers.getMapper(StudentMapper::class.java)
    }

    @Mapping(source = "schoolName", target = "school")
    @Mapping(source = "teacherName", target = "teacher")
    fun studentImpDtoToStudentPo(studentImpDto: StudentImpDto): StudentPo
}