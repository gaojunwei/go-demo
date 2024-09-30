package com.go.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*

/**
 * Jwt工具类
 */
class JwtUtils {
    companion object {
        private const val EXPIRE_TIME: Long = 1000 * 60 * 60 * 24 * 7 // 过期时间 7天
        private const val SECRET: String = "dskfks3hfowifei3*t978" // 秘钥

        /**
         * 生成token
         */
        fun createToken(map: MutableMap<String, Any>): String {
            return Jwts.builder()
                .setClaims(map)
                .setIssuedAt(Date())//创建 JWT 时的时间戳
                .setExpiration(Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET)// 设置签名
                .compact()
        }

        /**
         * 根据token解析出用户信息
         * @exception io.jsonwebtoken.ExpiredJwtException token过期异常
         */
        fun parseToken(token: String): Claims {
            return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .body
        }
    }
}