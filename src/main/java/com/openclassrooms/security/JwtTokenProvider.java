package com.openclassrooms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.security.Keys;

/**
 * Provides methods to create, validate, and parse JWT tokens.
 * It also extracts tokens from HTTP requests and retrieves the username from
 * the token.
 */
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    /**
     * Generates a JWT token for the provided username without any roles.
     *
     * @param username the username to include in the token
     * @return the generated JWT token as a String
     */
    public String createToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        // Convert secretKey to SecretKey for signing the token
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // Build and sign the JWT token
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        // Log the generated token (for debugging purposes)
        System.out.println("Generated JWT Token: " + token);

        return token;
    }

    /**
     * Validates the JWT token.
     * It checks the signature and verifies that the token is not expired.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     * @throws RuntimeException if the token is invalid
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }

    /**
     * Extracts the username from a valid JWT token.
     *
     * @param token the JWT token from which to extract the username
     * @return the username as a String
     */
    public String getUsername(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Resolves the JWT token from the Authorization header of an HTTP request.
     * The token must start with "Bearer ".
     *
     * @param request the HTTP request from which to extract the token
     * @return the JWT token as a String, or null if not found
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove the "Bearer " prefix
        }
        return null;
    }
}
