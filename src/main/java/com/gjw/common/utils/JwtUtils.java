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

    private static final String secretKey = "123456";
    private static final Long survivalTime = 3600L;

    /**
     * 签发JWT
     *
     * @param id      数据ID
     * @param subject 可以是JSON数据 尽可能少
     */
    public static String createJWT(String id, String subject) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey scrtKey = generalKey(secretKey);
        long expMillis = System.currentTimeMillis() + survivalTime * 1000;
        Date expDate = new Date(expMillis);
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(subject)// 主题
                .setIssuer("go")// 签发者
                .setIssuedAt(new Date())// 签发时间
                .signWith(signatureAlgorithm, scrtKey)// 签名算法以及密匙
                .setExpiration(expDate); // 过期时间
        return builder.compact();
    }

    /**
     * 验证JWT
     */
    public static SingleResult<Claims> validateJWT(String jwtStr) {
        SingleResult<Claims> result = new SingleResult<>();
        try {
            result.setCode(SystemCodeEnums.SUCCESS.getCode());
            result.setMsg(SystemCodeEnums.SUCCESS.getMsg());
            result.setData(parseJWT(jwtStr));
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
    public static Claims parseJWT(String jwt) {
        return Jwts.parser().setSigningKey(generalKey(secretKey)).parseClaimsJws(jwt).getBody();
    }
}