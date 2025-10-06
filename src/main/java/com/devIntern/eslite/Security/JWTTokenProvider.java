package com.devIntern.eslite.Security;


import com.devIntern.eslite.Exceptions.SecureEchoAPIException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Date;

@Component
public class JWTTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMS;


    public String generateToken(Authentication authentication){
        String name = authentication.getName();
        Date date = new Date();
        Date expDate = new Date(date.getTime() + jwtExpirationInMS);

        String token = Jwts.builder()
                .setSubject(name)
                .setIssuedAt(date)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512 , jwtSecret)
                .compact();

        return token;
    }

    public String getUserName(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token).getBody();

        return  claims.getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
             return true;
        } catch (MalformedJwtException ex) {
            throw new SecureEchoAPIException(HttpStatus.BAD_REQUEST , "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new SecureEchoAPIException(HttpStatus.BAD_REQUEST , "Expired JWT token");
        } catch(UnsupportedJwtException ex){
            throw new SecureEchoAPIException(HttpStatus.BAD_REQUEST , "Unsupported JWT token");
        } catch(IllegalArgumentException ex) {
            throw new SecureEchoAPIException(HttpStatus.BAD_REQUEST , "JWT claims String is empty");
        }
    }

}
