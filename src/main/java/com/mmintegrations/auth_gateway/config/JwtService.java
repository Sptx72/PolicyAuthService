package com.mmintegrations.auth_gateway.config;

import com.mmintegrations.auth_gateway.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "fe49f1a77019aaacc06efc770b96c6d9a23a920f1f26f60adc82feb84c7f4c91a314a5c3f67a92a7521407fc37ad8462d5777c8422c8f3af20214533dbdf8f80a1a9bea05cd5a308232b453066a594aefe63bd18c6f875b63e77054bb401436a0f53de9f7cc3906d09562dbd8a0d506f7d622537a6d7de7344dd265c27ca16ef0b4b28e7be81b721a18fc81a2c6ceae2776d055445f867a005558a60b35c05f1297c4f77fdf9860192d4ee4ef58cf835dde39ab1bfa2416ce0821bc2101c57f08fed0758a815a3b1285814f95b00ec074cc63a1e5f79d86362f5b6a610b13ddf556425ceae6285d720ebee5a4eddb91de8a9c33d8f71ad5fbf2968dc71f910c6";

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public String generateToken(User userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            User userDetails) {
         return Jwts.builder()
                 .setClaims(extraClaims)
                 .setSubject(userDetails.getUsername())
                 .setIssuedAt(new Date(System.currentTimeMillis()))
                 .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                 .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                 .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractRole(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }
}
