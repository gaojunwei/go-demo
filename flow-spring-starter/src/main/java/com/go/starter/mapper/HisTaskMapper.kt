package com.go.starter.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.go.starter.domain.HisTask
import org.apache.ibatis.annotations.Mapper

@Mapper
interface HisTaskMapper : BaseMapper<HisTask>
