package com.hrms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * JWT 工具类：签发与校验令牌。
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /** 签发令牌：subject 为用户名，authorities 为角色（ROLE_*）与权限编码 */
    public String generateToken(String username, List<String> authorities) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .claim("authorities", authorities)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsername(String token) {
        return parse(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getAuthorities(String token) {
        return parse(token).get("authorities", List.class);
    }

    public boolean validate(String token) {
        try {
            parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
    }
}
