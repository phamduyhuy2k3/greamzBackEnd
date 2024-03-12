package com.greamz.backend.config;

import com.greamz.backend.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUsernameThatTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("sub", String.class);
        } catch (ExpiredJwtException ex) {
            //Lấy thông tin user từ token
            Claims claims = ex.getClaims();
            return claims.get("sub", String.class);

        }
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserPrincipal userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }
    public String generateTokenForResetPassword(UserPrincipal userDetails) {
        return generateToken(new HashMap<>(), userDetails, Duration.ofMinutes(15));
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserPrincipal userDetails
    ) {
        return buildToken(extraClaims, userDetails, Duration.ofMinutes(jwtExpiration));
    }
    public String generateToken(
            Map<String, Object> extraClaims,
            UserPrincipal userDetails,Duration expiration
    ) {
        return buildToken(extraClaims, userDetails, expiration);
    }
    public String generateRefreshToken(
            UserPrincipal userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, Duration.ofMinutes(refreshExpiration));
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserPrincipal userDetails,
            Duration expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration.toMillis()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserPrincipal userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) throws ExpiredJwtException {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public long getJwtExpiration() {
        return jwtExpiration;
    }

    public long getRefreshExpiration() {
        return refreshExpiration;
    }
}