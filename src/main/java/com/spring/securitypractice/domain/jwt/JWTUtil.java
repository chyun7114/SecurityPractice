package com.spring.securitypractice.domain.jwt;

/*
    jwt토큰을 생성, 관리, 인증하기 위한 클래스
 */

import com.spring.securitypractice.domain.user.data.dao.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@Slf4j
public class JWTUtil {
    private final Key key;
    private final int accessTokenExpireTime;

    public JWTUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration_time}")int accessTokenExpireTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpireTime = accessTokenExpireTime;
    }

    /*
    *
    * accessToken 생성
    * @param user
    * @return Access Token String
    * */
    public String createAccessToken(User user){
        return createToken(user, accessTokenExpireTime);
    }

    /*
    * token 생성
    * @param user
    * @param expireTime
    * @return token string
    */

    private String createToken(User user, int accessTokenExpireTime) {
        Claims claims = Jwts.claims();
        claims.put("id", user.getUserId());
        claims.put("username", user.getUsername());
        claims.put("roles", user.getRoles());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(accessTokenExpireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * token에서 userId 추출
     * @param token String
     * @return userId
     */
    public String getUserIdByToken(String token){
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getId();
    }

    /**
     *
     * token의 유효성 검증
     * @param token
     * @return boolean
     */
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("[validateToken] 유효하지 않은 JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("[validateToken] 기간이 만료된 JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("[validateToken] 지원하지않는 JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("[validateToken] JWT claims string이 비어있습니다.", e);
        }
        return false;
    }

    /**
     * JWT Claims 추출
     * @param accessToken
     * @return JWT Claims
     */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
