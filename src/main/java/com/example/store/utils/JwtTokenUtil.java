package com.example.store.utils;

import com.example.store.entities.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenUtil {
  private static final long EXPIRE_DURATION = 12 * 60 * 60 * 1000; // 12 hour
  @Value("${app.jwt.secret}")
  private String SECRET_KEY;

  public String getSubject(String token){
    return parseClaims(token).getSubject();
  }

  public Claims parseClaims(String token){
    return Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();
  }

  public String generateToken(User user){
    return Jwts.builder()
            .setSubject(String.format("%s,%s", user.getId(), user.getRole().getId()))
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact();
  }

  //used to verify a given JWT. It returns true if the JWT is verified, or false otherwise.
  public boolean validateAccessToken(String token){
    try{
      Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException ex) {
      log.error("JWT expired", ex.getMessage());
    } catch (IllegalArgumentException ex) {
      log.error("Token is null, empty or only whitespace", ex.getMessage());
    } catch (MalformedJwtException ex) {
      log.error("JWT is invalid", ex.getMessage());
    } catch (UnsupportedJwtException ex) {
      log.error("JWT is not supported", ex.getMessage());
    } catch (SignatureException ex) {
      log.error("Signature validation failed", ex.getMessage());
    }
    return false;
  }

}
