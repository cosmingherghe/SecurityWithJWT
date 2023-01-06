package dev.cosmingherghe.security.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  private static final String SECRET_KEY = "576E5A7234753778214125442A472D4B6150645367556B58703273357638792F";

  public String extractUserName(String jwToken) {
    return extractClaim(jwToken, Claims::getSubject);
  }

  private Claims extractAllClaims(String jwToken) {
    return Jwts
      .parserBuilder()
      .setSigningKey(getSignInKey())
      .build()
      .parseClaimsJws(jwToken)
      .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  //Extracting All Claims method
  public <T> T extractClaim(String jwToken, Function<Claims, T> claimsResorver) {
    final Claims claims = extractAllClaims(jwToken);
    return claimsResorver.apply(claims);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
    return Jwts
          .builder()
          .setClaims(extraClaims)
          .setSubject(userDetails.getUsername())
          .setIssuedAt(new Date(System.currentTimeMillis()))
          .setExpiration(new Date(System.currentTimeMillis() + 1000 + 60 * 24)) //24 hours 
          .signWith(getSignInKey(), SignatureAlgorithm.HS256)
          .compact();
  }

  //Generating a Token without extra claims, only from the username
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  //Method that can validate a Token
  public boolean isTokenValir(String jwToken, UserDetails userDetails) {
    final String username = extractUserName(jwToken);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwToken));
  }

  private boolean isTokenExpired(String jwToken) {
    return extractExpiration(jwToken).before(new Date());
  }

  private Date extractExpiration(String jwToken) {
    return extractClaim(jwToken, Claims::getExpiration);
  }
}
