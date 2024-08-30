package com.go.starter.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.go.starter.domain.HisVariable
import org.apache.ibatis.annotations.Mapper

@Mapper
interface HisVariableMapper : BaseMapper<HisVariable>

