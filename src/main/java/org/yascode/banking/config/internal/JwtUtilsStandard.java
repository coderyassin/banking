package org.yascode.banking.config.internal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.yascode.banking.config.JwtUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtUtilsStandard implements JwtUtils {

    private String jwtSigningKey = "5cb60ca32309e4b2b64df8753babfce69895452776f20abe3bd3bf2f39f0a135";

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public boolean hasClaim(String token, String claimName) {
        final Claims claims = extractAllClaims(token);
        return claims.get(claimName) != null;
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey(jwtSigningKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails);
    }

    @Override
    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        return createToken(claims, userDetails);
    }

    @Override
    public String createToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(200)))
                .signWith(SignatureAlgorithm.HS256, jwtSigningKey).compact();
    }

    @Override
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public Key getSigningKey(String _secretKey) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(_secretKey));
    }
}
