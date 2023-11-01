package com.gjw.common.utils;

import com.gjw.common.enums.SystemCodeEnums;
import com.gjw.common.result.SingleResult;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * JWT 工具
 */
public class JwtUtils {
    /**
     * 签发JWT
     *
     * @param id
     * @param subject   可以是JSON数据 尽可能少
     * @param ttlMillis
     * @return String
     */
    public static String createJWT(String id, String subject, Long ttlMillis, String secretKey) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey scrtKey = generalKey(secretKey);
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(subject)// 主题
                .setIssuer("innovation")// 签发者
                .setIssuedAt(now)// 签发时间
                .signWith(signatureAlgorithm, scrtKey);// 签名算法以及密匙
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date expDate = new Date(expMillis);
            builder.setExpiration(expDate); // 过期时间
        }
        return builder.compact();
    }

    /**
     * 验证JWT
     */
    public static SingleResult<Claims> validateJWT(String jwtStr, String secretKey) {
        SingleResult<Claims> result = new SingleResult<>();
        try {
            result.setCode(SystemCodeEnums.SUCCESS.getCode());
            result.setMsg(SystemCodeEnums.SUCCESS.getMsg());
            result.setData(parseJWT(jwtStr, secretKey));
        } catch (ExpiredJwtException e) {
            result.setCode(SystemCodeEnums.JWT_EXPIRED.getCode());
            result.setMsg(SystemCodeEnums.JWT_EXPIRED.getMsg());
        } catch (Exception e) {
            result.setCode(SystemCodeEnums.FAIL.getCode());
            result.setMsg("验签失败");
        }
        return result;
    }

    private static SecretKey generalKey(String secretKey) {
        byte[] encodedKey = Base64.decodeBase64(secretKey);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 解析JWT字符串
     */
    public static Claims parseJWT(String jwt, String secretKey) {
        return Jwts.parser().setSigningKey(generalKey(secretKey)).parseClaimsJws(jwt).getBody();
    }
}