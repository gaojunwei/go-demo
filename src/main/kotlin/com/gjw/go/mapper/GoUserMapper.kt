package com.gjw.go.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.gjw.go.domain.entity.GoUser
import org.apache.ibatis.annotations.Mapper

@Mapper
interface GoUserMapper : BaseMapper<GoUser> {

}