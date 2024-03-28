package org.board.exercise_board.User.controller;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.User.application.SignUpApplication;
import org.board.exercise_board.User.application.VerifyEmailApplication;
import org.board.exercise_board.User.domain.Dto.UserDto;
import org.board.exercise_board.User.domain.Form.SignUpForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

  private final SignUpApplication signUpApplication;
  private final VerifyEmailApplication verifyEmailApplication;

  @PostMapping("/signup")
  public ResponseEntity<UserDto> signup(@RequestBody SignUpForm signUpForm) {
    return ResponseEntity.ok(signUpApplication.signup(signUpForm));
  }

  /**
   * 인증링크를 통해 입력된 tokenId를 사용하여 사용자 verified_status를 true로 변경
   * @param tokenId
   * @return
   */
  @GetMapping("/verified/{tokenId}")
  public String verifyEmail(@PathVariable("tokenId") String tokenId) {
    verifyEmailApplication.verifyEmail(tokenId);
    return "인증이 완료되었습니다.";
  }
}
