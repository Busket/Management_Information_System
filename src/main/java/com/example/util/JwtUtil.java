package com.example.util;

import com.example.entity.GPUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;

//@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {
    private String key ="wangmh";
    private long ttl =3600000;//一个小时
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public long getTtl() {
        return ttl;
    }
    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    /**
     * 生成JWT
     *
     * @param GPUser
     * @return
     */
    public String createJWT(GPUser GPUser) {
        long nowMillis = System.currentTimeMillis();
        System.out.println(nowMillis);
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setId(String.valueOf(GPUser.getId()))
                .setSubject("DAUser")//设置主题
                .setIssuedAt(now)//设置
                .signWith(SignatureAlgorithm.HS256, key)
                .claim("email", GPUser.getEmail());
//                .claim("password",daUser.getPassword());
        if (ttl > 0) {
            builder.setExpiration( new Date( nowMillis + ttl));
        }
        return builder.compact();
    }

    /**
     * 解析JWT
     * @param jwtStr
     * @return
     */
    public Claims parseJWT(String jwtStr){
        return  Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }

//    /**
//     * 获取签名信息
//     * @param token
//     * @author geYang
//     * @date 2018-05-18 16:47
//     */
//    public Claims getClaimByToken(String token) {
//        try {
//            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
//        } catch (Exception e) {
//            ttl.debug("validate is token error ", e);
//            return null;
//        }
//    }

}
