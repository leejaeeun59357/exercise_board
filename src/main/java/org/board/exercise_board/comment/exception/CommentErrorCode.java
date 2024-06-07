package org.board.exercise_board.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode {
  NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "해당 댓글을 찾을 수 없습니다"),
  WRITER_ONLY(HttpStatus.BAD_REQUEST, "해당 권한이 없습니다.");

  private final HttpStatus httpStatus;
  private final String detail;
}
