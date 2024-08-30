package com.go.starter.mapstruct

import com.go.starter.domain.HisVariable
import com.go.starter.domain.RuVariable
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface HisVariableConverter {
    fun toEntity(param: RuVariable): HisVariable

    companion object {
        val INSTANCE: HisVariableConverter = Mappers.getMapper(HisVariableConverter::class.java)
    }
}