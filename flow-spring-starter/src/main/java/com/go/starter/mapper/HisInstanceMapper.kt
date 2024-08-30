package com.go.starter.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.go.starter.domain.HisInstance
import org.apache.ibatis.annotations.Mapper

@Mapper
interface HisInstanceMapper : BaseMapper<HisInstance>