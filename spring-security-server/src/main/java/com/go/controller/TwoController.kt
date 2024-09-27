package com.go.controller

import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PostFilter
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.access.prepost.PreFilter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 基于方法鉴权
 */
@RestController
@RequestMapping
class TwoController {
    /**
     * @PreAuthorize注解调用方法前，判断是否有权限
     * 作用：使用类或者方法上，拥有指定权限才可以访问
     * String类型参数：语法是spring的el表达式,hasRole中的权限字符为 Authority加ROLE_前缀规则；
     */
    @PreAuthorize("hasAuthority('test:show') || hasAuthority('test:show2')")
    @GetMapping("/m_1")
    fun preAuthorizeHasAuthority(): String {
        return "preAuthorizeHasAuthority，基于方法鉴权，测试@PreAuthorize注解"
    }

    /**
     * @PostAuthorize 方法返回时校验，返回用户名，如果用户名的长度大于3位认为是合法的
     * returnObject:是固定写法，就是返回值对象
     */
    @PreAuthorize("hasAuthority('user:name')")
    @PostAuthorize("returnObject.length() > 3")
    @GetMapping("/m_2")
    fun postAuthorizeHasAuthority(): String {
        return "preAuthorizeHasAuthority，基于方法鉴权，测试@PostAuthorize注解,方法返回时校验"
    }

    /**
     * @PostFilter：过滤符合条件的数据返回
     * 返回结果集中元素的长度大于3的数据
     */
    @PreAuthorize("hasAuthority('user:list')")
    @PostFilter("filterObject.length() > 3")
    @GetMapping("/m_3")
    fun postFilter(): List<String> {
        return mutableListOf("admin", "gjw", "tiger", "12")
    }

    /**
     * @PreFilter：过滤符合条件的数据进入到接口，数据必须是Collection、map、Array类型数组
     */
    @PreAuthorize("hasAuthority('user:list')")
    @PreFilter("returnObject.length() > 3")
    @GetMapping("/m_4")
    fun preFilter(): List<String> {
        return mutableListOf("admin", "gjw", "tiger", "12")
    }

}