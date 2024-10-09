package com.go.security

/**
 * 模拟数据
 */
class MockData {
    companion object {
        //请求头中token的key
        const val TOKEN_HEADER = "Authorization"

        /**
         * 获取用户权限 - 测试使用
         */
        fun getPerms(): Set<String> {
            return setOf("test:show", "test:show1", "test:show2")
        }

        /**
         * 自定义认证管理器的菜单数据,权限数据 - 测试使用
         */
        fun goAuthorizationManagerMenuData(): Map<String, String> {
            return mapOf("/four/m_1" to "test:show")
        }
    }
}