package org.board.exercise_board.User.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.User.domain.Form.SignInForm;
import org.board.exercise_board.User.domain.model.JwtToken;
import org.board.exercise_board.User.domain.repository.UserRepository;
import org.board.exercise_board.User.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

  private final UserService userService;

  public JwtToken signin(SignInForm signInForm) {
    return userService.signin(signInForm.getLoginId(), signInForm.getPassword());
  }
}
