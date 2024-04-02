package org.board.exercise_board.User.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.board.exercise_board.User.domain.model.Token;
import org.board.exercise_board.User.exception.CustomException;
import org.board.exercise_board.User.exception.ErrorCode;
import org.board.exercise_board.User.service.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerifyEmailApplication {

  private final TokenService tokenService;

  @Transactional
  public void verifyEmail(String tokenId) {

    Token token = tokenService.findToken(tokenId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    // 이미 인증완료되었다면 Exception 발생
    if(token.getUser().getVerifiedStatus()) {
      throw new CustomException(ErrorCode.ALREADY_VERIFIED);
    }

    // 만료 시간이 지났다면 Exception 발생
    if(tokenService.verifyExpirationDate(token)) {
      tokenService.updateVerifyStatus(token);
    } else {
      throw new CustomException(ErrorCode.EXPIRATION_TIME_IS_OVER);
    }
  }
}