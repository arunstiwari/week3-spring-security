package com.fil.week3springsecurity.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JWTUtil {
    //Secret Key
    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles",roles)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000* 60 *60)) // 1hr
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        return (extractUserName(token).equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Claims claims =parseClaimValue(token);
        return claims.getExpiration().before(new Date(System.currentTimeMillis()));
    }

    public String extractUserName(String token) {
        // parse claims -> extract username as well as i can verify as well
        Claims claims = parseClaimValue(token);
        return claims.getSubject();
    }

    public  Claims parseClaimValue(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }
}
