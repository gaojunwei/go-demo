package com.go.mapper.domain

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serial
import java.io.Serializable

@TableName("user", autoResultMap = true)
class User : Serializable, UserDetails {
    @TableId(type = IdType.AUTO)
    var userId: Long? = null

    //登录密码
    var loginPassword: String? = null

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

    // 权限信息
    val perms: MutableSet<String> = mutableSetOf()

    // 角色信息
    val roleSet: MutableSet<String> = mutableSetOf()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return AuthorityUtils.createAuthorityList(perms)
    }

    override fun getPassword(): String = loginPassword!!

    override fun getUsername(): String = loginName!!

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = state!!

    companion object {
        @Serial
        private val serialVersionUID = 1L
    }
}