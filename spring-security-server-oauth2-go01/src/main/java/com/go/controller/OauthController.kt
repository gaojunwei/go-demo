package com.go.controller

import cn.hutool.http.HttpUtil
import cn.hutool.json.JSONUtil
import com.go.common.extension.log
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/oauth")
class OauthController {

    /**
     * gitee 回调地址
     */
    @GetMapping("/notify")
    fun authNotify(@RequestParam code: String): String {
        //获取token
        val param = mapOf(
            "grant_type" to "authorization_code",
            "code" to code,
            "client_id" to "4acc995f3b994fe11d5cca9c4d2a942211afd732c391fc6da739bb5274dd22af",
            "redirect_uri" to "http://localhost:9002/oauth/notify",
            "client_secret" to "55b243f5af4f8238d23c7233d6b3263aaa6ade6b7cd67b43026c9cf7a7169b09",
        )
        //通过code获取token
        val post = HttpUtil.post("https://gitee.com/oauth/token", param)
        val loginVo = JSONUtil.toBean(post, GiteeOauthLoginVo::class.java)
        log.info("通过code获取token 响应数据:${JSONUtil.toJsonStr(loginVo)}")
        //根据accessToken获取用户信息
        val userInfo = HttpUtil.get("https://gitee.com/api/v5/user?access_token=${loginVo.accessToken}")
        log.info("根据accessToken获取用户信息 响应数据:$userInfo")
        val userInfoStr = JSONUtil.toBean(userInfo, GiteeUserInfoVo::class.java)
        return JSONUtil.toJsonStr(userInfoStr)
    }

    @GetMapping("/logout")
    fun logout() {
        log.info("退出成功")
    }


}

// gitee登录响应数据
class GiteeOauthLoginVo {
    var accessToken: String = ""
    var tokenType: String = ""
    var expiresIn: String = ""
    var refreshToken: String = ""
    var scope: String = ""
    var createdAt: Long = 0L
}
// gitee用户信息
class GiteeUserInfoVo {
    var name: String = ""
    var avatarUrl: String = ""
}