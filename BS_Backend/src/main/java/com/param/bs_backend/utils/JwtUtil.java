package com.param.bs_backend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component  // 添加此註解以使其成為Spring管理的組件
public class JwtUtil {

    //过期时间15分钟
    private static final long EXPIRE_TIME = 15 * 60 * 1000;

    // 从配置文件中读取密钥
    private static String TOKEN_SECRET;

    // 使用注入的值覆蓋靜態字段
    @Value("${jwt.secretKey}")
    public void setTokenSecret(String tokenSecret) {
        JwtUtil.TOKEN_SECRET = tokenSecret;
    }

    //生成簽名,15分鐘後過期
    public static String sign(int userId) {
        //過期時間
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        // 使用密鑰進行加密
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        //設置頭信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //附帶userID生成簽名
        return JWT.create().withHeader(header).withClaim("userId", userId)
                .withExpiresAt(date).sign(algorithm);
    }


    public static boolean verity(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (IllegalArgumentException | JWTVerificationException e) {
            return false;
        }
    }

    // 從 token 中解析出 userId
    public static int getUserIdFromToken(String token) {
        try {
            return JWT.decode(token).getClaim("userId").asInt();
        } catch (JWTDecodeException e) {
            return -1;
        }
    }

}
