package com.gjw.go.domain.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName("go_user")
class GoUser : BaseEntity() {
    /**
     * 用户ID
     */
    @TableId(type = IdType.INPUT)
    var userId: Long? = null

    /**
     * 用户账号
     */
    var userAccount: String? = null

    /**
     * 昵称
     */
    var nickName: String? = null

    /**
     * 密码
     */
    var userPwd: String? = null
}