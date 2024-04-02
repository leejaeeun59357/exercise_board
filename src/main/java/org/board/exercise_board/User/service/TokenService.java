package org.board.exercise_board.User.service;

import java.time.LocalDateTime;
import java.util.Optional;
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
  private final long EMAIL_TOKNE_EXPIRATION_MINUTE_TIME_VALUE = 5L;

  public Token createToken(User user) {

    Token token;
    if (tokenRepository.existsByUser(user)) {
      token = tokenRepository.findByUser(user);
    } else {
      token = new Token();
      token.setUser(user);
    }
    token.setExpirationDateTime(LocalDateTime.now().plusMinutes(
        EMAIL_TOKNE_EXPIRATION_MINUTE_TIME_VALUE));
    return tokenRepository.save(token);
  }


  public Optional<Token> findToken(String tokenId) {
    return tokenRepository.findById(tokenId);
  }

  /**
   * 현재 시간이 만료시간을 지났는지 검사
   *
   * @param token
   * @return
   */
  public boolean verifyExpirationDate(Token token) {
    // 현재 시간이 만료시간을 지나지 않았을 때 true
    return !LocalDateTime.now().isAfter(token.getExpirationDateTime());
  }

  /**
   * 인증링크를 통해 사용자 인증이 되면 user의 verified_Status를 true로 변경
   *
   * @param token
   */
  public void updateVerifyStatus(Token token) {
    token.getUser().setVerifiedStatus(true);
    tokenRepository.save(token);
  }

}
