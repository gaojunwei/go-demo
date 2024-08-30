package com.go.starter.mapstruct

import com.go.starter.domain.HisTask
import com.go.starter.domain.RuTask
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface HisTaskConverter {
    fun toEntity(param: RuTask): HisTask

    companion object {
        val INSTANCE: HisTaskConverter = Mappers.getMapper(HisTaskConverter::class.java)
    }
}