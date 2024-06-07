package org.board.exercise_board.user.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionController {

  // 컨트롤러 form validation 핸들러
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> methodInvalidException(
      final MethodArgumentNotValidException e
  ) {
    BindingResult bindingResult = e.getBindingResult();
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(MethodInvalidResponse.builder()
            .errorCode(bindingResult.getFieldErrors().get(0).getCode())
            .errorMessage(bindingResult.getFieldErrors().get(0).getDefaultMessage())
            .build()
        );
  }

  @Getter
  @ToString
  @AllArgsConstructor
  @Builder
  public static class MethodInvalidResponse {
    private String errorCode;
    private String errorMessage;
  }
}