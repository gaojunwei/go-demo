package com.go.starter.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.go.starter.domain.Process
import org.apache.ibatis.annotations.Mapper

@Mapper
interface FwProcessMapper : BaseMapper<Process>
