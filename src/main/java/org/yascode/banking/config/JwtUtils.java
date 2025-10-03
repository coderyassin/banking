package org.yascode.banking.config;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtUtils {

    String extractUsername(String token);

    Date extractExpiration(String token);

    boolean hasClaim(String token, String claimName);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    Claims extractAllClaims(String token);

    Boolean isTokenExpired(String token);

    String generateToken(UserDetails userDetails);

    String generateToken(UserDetails userDetails, Map<String, Object> claims);

    String createToken(Map<String, Object> claims, UserDetails userDetails);

    Boolean isTokenValid(String token, UserDetails userDetails);

    Key getSigningKey(String _secretKey);
}
