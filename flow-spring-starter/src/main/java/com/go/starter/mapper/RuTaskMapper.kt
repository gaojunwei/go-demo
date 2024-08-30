package com.go.starter.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.go.starter.domain.RuTask
import org.apache.ibatis.annotations.Mapper

@Mapper
interface RuTaskMapper : BaseMapper<RuTask>

