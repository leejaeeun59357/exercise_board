package org.board.exercise_board.User.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.board.exercise_board.User.domain.Dto.UserDto;
import org.board.exercise_board.User.domain.Form.SignUpForm;
import org.board.exercise_board.User.domain.model.Token;
import org.board.exercise_board.User.domain.model.User;
import org.board.exercise_board.User.exception.CustomException;
import org.board.exercise_board.User.exception.ErrorCode;
import org.board.exercise_board.User.service.TokenService;
import org.board.exercise_board.User.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignUpApplication {
  private final UserService userService;
  private final TokenService tokenService;

  public UserDto signup(SignUpForm signUpForm) {
    if(userService.isExistLoginId(signUpForm.getLoginId())) {
      throw new CustomException(ErrorCode.ALREADY_REGISTERD_ID);
    }

    if(userService.isExistEmail(signUpForm.getEmail())) {
      throw new CustomException(ErrorCode.ALREATY_REGISTERD_EMAIL);
    }

    User user = userService.save(signUpForm);
    Token token = tokenService.createToken(user);

    return UserDto.entityToDto(user);
  }
}
