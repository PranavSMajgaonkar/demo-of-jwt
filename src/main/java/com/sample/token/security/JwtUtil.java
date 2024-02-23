package com.sample.token.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

//    @Value("${jwt.secret}")
    private static final String SECRET_KEY = "vbDFMXnwHVGXER0n8hYG1wnJl+IvRDNcVcqK5t4WSoUal4JlRHHMvwtU9Cm3vf/6XWKdXDNnFcp+4dUv2MhKbw==";
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    // retrieve username from jwt token
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    // retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims :: getExpiration);
    }

    // for retrieving any information from token we will need the secret key
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        if (claims != null){
            return claimsResolver.apply(claims);
        }
        return null;
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",userDetails.getAuthorities());
        return createToken(claims, userDetails.getUsername());
    }
    // generate token for user
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities());
        return doGenerateRefreshToken(claims, userDetails.getUsername());
    }

    // while creating the token -
    // 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
    // 2. Sign the JWT using the HS512 algorithm and secret key.
    // 3. According to JWS Compact
    // Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    // compaction of the JWT to a URL-safe string
    private String createToken(Map<String, Object> claims, String subject) {
        Header header = Jwts.header().setType("JWT").build();
        return Jwts.builder().setHeader((Map<String, Object>) header).setClaims(claims).setSubject(subject)
                .setIssuer("pranav_majgaonkar").setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY).compact();
    }

    // refresh token
    private String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        Header header = Jwts.header().setType("JWT").build();
        return Jwts.builder().setHeader((Map<String, Object>) header).setClaims(claims).setSubject(subject)
                .setIssuer("rbackspring").setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    // check if the token has expired
    public static boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) || !isTokenExpired(token));
    }

    private static boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    private static Date extractExpiration(String token) {
        return (Date) extractClaims(token).get("iat"); //getExpiration();
    }

    private static String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    private static Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }
}

