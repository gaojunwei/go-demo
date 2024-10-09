package com.go.security.handler

import cn.hutool.json.JSONUtil
import com.go.common.extension.log
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler

/**
 * 退出登陆处理器
 */
class GoLogoutSuccessHandler:LogoutSuccessHandler {
    override fun onLogoutSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication?
    ) {
        // todo 执行业务操作，清除cookie,记录退出日志,清除用户缓存等
        log.info(" 执行业务操作，清除cookie,记录退出日志,清除用户缓存等")
        response.characterEncoding = "UTF-8"
        response.writer.write(JSONUtil.toJsonStr(mapOf("code" to 200, "msg" to "注销成功")))
    }
}