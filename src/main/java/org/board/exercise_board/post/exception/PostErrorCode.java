package org.board.exercise_board.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode {

  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "해당 사용자를 찾을 수 없습니다."),

  SUBJECT_IS_EMPTY(HttpStatus.BAD_REQUEST, "제목은 필수 입력 항목입니다."),
  CONTENT_IS_EMPTY(HttpStatus.BAD_REQUEST, "내용은 필수 입력 항목입니다."),

  NOT_VERIFIED_EMAIL(HttpStatus.BAD_REQUEST,"이메일 인증이 되지 않았습니다."),

  POST_IS_NOT_EXIST(HttpStatus.BAD_REQUEST, "해당 게시물이 존재하지 않습니다"),

  NOT_HAVE_RIGHT(HttpStatus.BAD_REQUEST, "게시글 권한이 없습니다"),

  KEYWORD_IS_EMPTY(HttpStatus.BAD_REQUEST,"검색 키워드를 입력하세요.");


  private final HttpStatus httpStatus;
  private final String detail;
}
