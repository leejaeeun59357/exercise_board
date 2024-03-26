package org.board.exercise_board.User.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  // 회원가입 관련
  ALREADY_REGISTERD_ID(HttpStatus.BAD_REQUEST, "이미 등록된 ID입니다."),
  ALREATY_REGISTERD_EMAIL(HttpStatus.BAD_REQUEST, "이미 등록된 Email입니다.");

  private final HttpStatus httpStatus;
  private final String detail;
}
