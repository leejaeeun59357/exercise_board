package org.board.exercise_board.Comment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CommentExceptionController {

  @ExceptionHandler({
      CommentCustomException.class
  })
  public ResponseEntity<CommentExceptionResponse> commentCustomExceptionHandler(
      final CommentCustomException c
  ) {
    log.warn("CommentException   :   {}", c.getCommentErrorCode());
    return ResponseEntity.badRequest().body(
        new CommentExceptionResponse(c.getMessage(),c.getCommentErrorCode())
    );
  }


  @Getter
  @ToString
  @AllArgsConstructor
  public static class CommentExceptionResponse {
    private String message;
    private CommentErrorCode commentErrorCode;
  }
}
