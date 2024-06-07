package org.board.exercise_board.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.user.application.SignInApplication;
import org.board.exercise_board.user.application.SignUpApplication;
import org.board.exercise_board.user.application.VerifyEmailApplication;
import org.board.exercise_board.user.domain.Dto.UserDto;
import org.board.exercise_board.user.domain.Form.SignInForm;
import org.board.exercise_board.user.domain.Form.SignUpForm;
import org.board.exercise_board.user.domain.model.JwtToken;
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
  private final SignInApplication signInApplication;

  @PostMapping("/signup")
  public ResponseEntity<UserDto> signup(@Valid @RequestBody SignUpForm signUpForm) {
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

  /**
   * jwt를 사용한 로그인 기능
   * @param signInForm
   * @return
   */
  @PostMapping("/signin")
  public ResponseEntity<JwtToken> signin(@Valid @RequestBody SignInForm signInForm) {
    return ResponseEntity.ok(signInApplication.signin(signInForm));
  }
}
