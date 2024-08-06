package com.example.api_gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil implements Serializable {
    private static final long serialVersionUID = 1L;

    @Value("${app.jwtSecret}")
    private String secret;

    @Value("${app.jwtExpirationMs}")
    private String expirationTime;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(key()).parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


}
