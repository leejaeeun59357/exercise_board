package org.board.exercise_board.post.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class PostExceptionController {

  @ExceptionHandler({
      PostCustomException.class
  })
  public ResponseEntity<PostExceptionResponse> postCustomRequestException(
      final PostCustomException p
  ) {
    log.warn("api PostException   :   {}  ", p.getPostErrorCode());
    return ResponseEntity.badRequest().body(
        new PostExceptionResponse(p.getMessage(), p.getPostErrorCode())
    );
  }


  @Getter
  @ToString
  @AllArgsConstructor
  public static class PostExceptionResponse {
    private String message;
    private PostErrorCode postErrorCode;
  }
}
