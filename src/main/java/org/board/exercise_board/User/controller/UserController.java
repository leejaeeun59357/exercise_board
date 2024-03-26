package org.board.exercise_board.User.controller;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.User.application.SignUpApplication;
import org.board.exercise_board.User.domain.Dto.UserDto;
import org.board.exercise_board.User.domain.Form.SignUpForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

  private final SignUpApplication signUpApplication;

  @PostMapping("/signup")
  public ResponseEntity<UserDto> signup(@RequestBody SignUpForm signUpForm) {
    return ResponseEntity.ok(signUpApplication.signup(signUpForm));
  }

}
