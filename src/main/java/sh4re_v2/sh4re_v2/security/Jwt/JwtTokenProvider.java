package sh4re_v2.sh4re_v2.security.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import sh4re_v2.sh4re_v2.domain.User;
import sh4re_v2.sh4re_v2.exception.error_code.AuthErrorCode;
import sh4re_v2.sh4re_v2.exception.exception.BusinessException;
import sh4re_v2.sh4re_v2.security.TokenStatus;

@Slf4j
@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.access-token-expiration}")
  private long accessTokenExpiration;

  @Value("${jwt.refresh-token-expiration}")
  private long refreshTokenExpiration;

  private Key getSigningKey() {
    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateAccessToken(User user) {
    return generateToken(user, accessTokenExpiration);
  }

  public String generateRefreshToken(User user) {
    return generateToken(user, refreshTokenExpiration);
  }

  private String generateToken(User user, long expiration) {
    long now = System.currentTimeMillis();
    Date issuedAt = new Date(now);
    Date expiryDate = new Date(now + expiration);

    return Jwts.builder()
        .setHeader(createHeader())
        .setClaims(createClaims(user))
        .setSubject(user.getUsername())
        .setIssuedAt(issuedAt)
        .setExpiration(expiryDate)
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Map<String, Object> createHeader() {
    Map<String, Object> header = new HashMap<>();
    header.put("typ", "JWT");
    header.put("alg", "HS256");
    return header;
  }

  private Map<String, Object> createClaims(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", user.getId());
    claims.put("role", user.getRole().getKey());
    return claims;
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Long extractUserId(String token) {
    return extractAllClaims(token).get("id", Long.class);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException e) {
      throw new BusinessException(AuthErrorCode.EXPIRED_JWT, e);
    } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
      throw new BusinessException(AuthErrorCode.INVALID_JWT, e);
    }
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    try {
      return extractExpiration(token).before(new Date());
    } catch (ExpiredJwtException e) {
      return true;
    }
  }

  public TokenStatus validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token);
      return TokenStatus.AUTHENTICATED;
    } catch (ExpiredJwtException e) {
      log.error("Expired JWT token: {}", e.getMessage());
      return TokenStatus.EXPIRED;
    } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
      return TokenStatus.INVALID;
    }
  }
}
