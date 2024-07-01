package org.board.exercise_board.user.Security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.board.exercise_board.exception.CustomException;
import org.board.exercise_board.exception.ErrorCode;
import org.board.exercise_board.user.domain.model.JwtToken;
import org.board.exercise_board.user.domain.model.User;
import org.board.exercise_board.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {

  private static final String AUTHORITIES_KEY = "auth";
  private static final String BEARER_TYPE = "Bearer";

  //유효시간 = 1시간
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L;

  // 유효시간 = 7일
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;
  private final Key key;
  private final UserRepository userRepository;

  public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey,
                          UserRepository userRepository) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
    this.userRepository = userRepository;
  }


  public JwtToken createToken(String loginId, String role) {


    var now = new Date();

    // AccessToken 생성
    var accessTokenExpiredDate =
            new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

    String accessToken = Jwts.builder()
            .setSubject(loginId)
            .claim(AUTHORITIES_KEY, role)
            .setIssuedAt(now)
            .setExpiration(accessTokenExpiredDate)
            .signWith(key)
            .compact();


    // refreshToken 생성
    var refreshTokenExpiredDate =
            new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);

    String refreshToken = Jwts.builder()
            .setSubject(loginId)
            .claim(AUTHORITIES_KEY, role)
            .setIssuedAt(now)
            .setExpiration(refreshTokenExpiredDate)
            .signWith(key)
            .compact();

    return JwtToken.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
  }


  // Jwt 토큰을 복호화하여 토큰에 들어있는 인증 정보를 꺼내는 메서드
  public Authentication getAuthentication(String accessToken) {
    Claims claims = parseClaims(accessToken);

    if (claims.get(AUTHORITIES_KEY) == null) {
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    // 해당 사용자의 권한 정보 가져오기
    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    User user = userRepository.findByLoginId(claims.getSubject())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    return new UsernamePasswordAuthenticationToken(user, accessToken, authorities);
  }


  /**
   * 토큰 정보 검증
   * @param token
   * @return
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty.", e);
    }
    return false;
  }

  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(accessToken)
          .getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
