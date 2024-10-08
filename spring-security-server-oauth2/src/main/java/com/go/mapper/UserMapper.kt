package com.go.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.go.mapper.domain.User
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UserMapper : BaseMapper<User>