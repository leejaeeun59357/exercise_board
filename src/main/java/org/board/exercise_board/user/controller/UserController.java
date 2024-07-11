package org.board.exercise_board.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.user.application.SignInApplication;
import org.board.exercise_board.user.application.SignUpApplication;
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
  private final SignInApplication signInApplication;

  @Operation(summary = "회원가입")
  @PostMapping("/signup")
  public ResponseEntity<UserDto> signup(@Valid @RequestBody SignUpForm signUpForm) {
    return ResponseEntity.ok(signUpApplication.signUp(signUpForm));
  }


  @Operation(summary = "이메일 인증")
  @GetMapping("/verified/{tokenId}")
  public String verifyEmail(@PathVariable("tokenId") String tokenId) {
    signUpApplication.verifyEmail(tokenId);
    return "인증이 완료되었습니다.";
  }

  @Operation(summary = "로그인")
  @PostMapping("/signin")
  public ResponseEntity<JwtToken> signin(@Valid @RequestBody SignInForm signInForm) {
    return ResponseEntity.ok(signInApplication.signIn(signInForm));
  }
}
