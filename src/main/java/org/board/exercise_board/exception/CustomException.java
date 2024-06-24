package org.board.exercise_board.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomException extends RuntimeException{
  private ErrorCode errorCode;
  private String errorMessage;

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.errorMessage = errorCode.getMessage();
  }
}
