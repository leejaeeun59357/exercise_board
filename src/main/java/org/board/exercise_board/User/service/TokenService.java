package org.board.exercise_board.User.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.User.domain.model.Token;
import org.board.exercise_board.User.domain.model.User;
import org.board.exercise_board.User.domain.repository.TokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;

  // 만료 시간은 5분으로 설정
  private final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L;

  public Token createToken(User user) {
    Token token = new Token();
    token.setUser(user);
    token.setExpirationDate(LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE));
    return tokenRepository.save(token);
  }


  // 토큰 유효시간 확인 필요

}
