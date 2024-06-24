package org.board.exercise_board.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  // 회원가입 관련
  ALREADY_REGISTERD_ID(HttpStatus.BAD_REQUEST, "이미 등록된 ID입니다."),
  ALREADY_REGISTERD_EMAIL(HttpStatus.BAD_REQUEST, "이미 등록된 Email입니다."),
  EMAIL_IS_NULL(HttpStatus.BAD_REQUEST, "Email은 필수 입력 항목입니다."),
  LOGIN_ID_IS_NULL(HttpStatus.BAD_REQUEST, "ID는 필수 입력 항목입니다."),
  PASSWORD_IS_NULL(HttpStatus.BAD_REQUEST, "비밀번호는 필수 입력 항목입니다."),

  // 인증 관련
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "해당 사용자를 찾을 수 없습니다."),
  ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "이미 인증이 완료되었습니다."),
  MAIL_SEND_FAIL(HttpStatus.BAD_REQUEST, "메일 전송에 실패하였습니다."),
  EXPIRATION_TIME_IS_OVER(HttpStatus.BAD_REQUEST, "인증 유효시간이 지났습니다."),

  // 게시글 관련
  SUBJECT_IS_EMPTY(HttpStatus.BAD_REQUEST, "제목은 필수 입력 항목입니다."),
  CONTENT_IS_EMPTY(HttpStatus.BAD_REQUEST, "내용은 필수 입력 항목입니다."),

  NOT_VERIFIED_EMAIL(HttpStatus.BAD_REQUEST,"이메일 인증이 되지 않았습니다."),

  POST_IS_NOT_EXIST(HttpStatus.BAD_REQUEST, "해당 게시물이 존재하지 않습니다"),

  NOT_HAVE_RIGHT(HttpStatus.BAD_REQUEST, "게시글 권한이 없습니다"),

  KEYWORD_IS_EMPTY(HttpStatus.BAD_REQUEST,"검색 키워드를 입력하세요."),

  // 댓글 관련
  NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "해당 댓글을 찾을 수 없습니다"),
  WRITER_ONLY(HttpStatus.BAD_REQUEST, "해당 권한이 없습니다.");


  private final HttpStatus httpStatus;
  private final String message;
}
