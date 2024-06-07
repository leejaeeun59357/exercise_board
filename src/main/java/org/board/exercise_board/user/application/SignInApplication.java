package org.board.exercise_board.user.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.comment.service.NotificationService;
import org.board.exercise_board.user.domain.Form.SignInForm;
import org.board.exercise_board.user.domain.model.JwtToken;
import org.board.exercise_board.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

  private final UserService userService;
  private final NotificationService notificationService;

  /**
   * 로그인하는 메서드, 로그인 완료되면 sse 연결을 시도한다.
   *
   * @param signInForm
   * @return
   */
  public JwtToken signin(SignInForm signInForm) {
    JwtToken token = userService.signin(signInForm.getLoginId(), signInForm.getPassword());
    notificationService.subscribe(signInForm.getLoginId());
    return token;
  }
}
