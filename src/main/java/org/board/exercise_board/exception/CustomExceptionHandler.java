package org.board.exercise_board.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

  // 컨트롤러 form validation 핸들러
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> methodInvalidException(
      final MethodArgumentNotValidException e
  ) {
    BindingResult bindingResult = e.getBindingResult();
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(Response.builder()
            .errorCode(bindingResult.getFieldErrors().get(0).getCode())
            .errorMessage(bindingResult.getFieldErrors().get(0).getDefaultMessage())
            .build()
        );
  }

  @ExceptionHandler(CustomException.class)
  protected ResponseEntity<Response> handleCustomException (CustomException e) {
    Response response = Response.builder()
            .errorCode(String.valueOf(e.getErrorCode()))
            .errorMessage(e.getErrorMessage())
            .build();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @Getter
  @ToString
  @AllArgsConstructor
  @Builder
  public static class Response {
    private String errorCode;
    private String errorMessage;
  }
}