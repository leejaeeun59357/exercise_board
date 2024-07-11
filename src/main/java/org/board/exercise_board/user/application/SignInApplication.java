package org.board.exercise_board.user.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.user.domain.Form.SignInForm;
import org.board.exercise_board.user.domain.model.JwtToken;
import org.board.exercise_board.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {
    private final UserService userService;

    public JwtToken signIn(SignInForm signInForm) {
        return userService.signin(signInForm);
    }
}
