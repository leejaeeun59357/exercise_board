package org.board.exercise_board.user.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
  private final ErrorCode errorCode;

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getDetail());
    this.errorCode = errorCode;
  }
}
