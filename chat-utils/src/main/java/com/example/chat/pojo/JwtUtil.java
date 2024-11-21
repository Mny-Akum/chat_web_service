package com.example.chat.pojo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class JwtUtil {
    private static final Long time= 43200000L;
    private static final MacAlgorithm alg = Jwts.SIG.HS512;
    private static final SecretKey key = alg.key().build();
    public static String generateJwt(Map<String,Object> clamis){
        return Jwts.builder()
                .issuer("chat_server")
                .claims(clamis)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + time))
                .id(UUID.randomUUID().toString())
                .signWith(key)
                .compact();
                //
//                .setClaims(clamis)
//                .signWith(SignatureAlgorithm.HS256,"token")
//                .setExpiration(new Date(System.currentTimeMillis() + time))
//                .compact();

    }
    public static Claims parseJWT(String jwt){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
