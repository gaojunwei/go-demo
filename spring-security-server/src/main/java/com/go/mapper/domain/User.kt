package com.go.mapper.domain

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName("user", autoResultMap = true)
class User {
    @TableId(type = IdType.AUTO)
    var userId: Long? = null

    //用户名
    var userName: String? = null

    //登录密码
    var password: String? = null

    //登录用户名称
    var loginName: String? = null

    //性别
    var gender: Int? = null

    //手机号
    var phone: String? = null

    //地址
    var address: String? = null

    //部门 - 组织 ID
    var organizationId: Int? = null

    //用户状态
    var state: Boolean? = null

    //邮箱地址
    var email: String? = null

    //注释
    var remark: String? = null
}