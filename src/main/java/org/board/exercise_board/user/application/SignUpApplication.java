package org.board.exercise_board.user.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.board.exercise_board.user.domain.Dto.UserDto;
import org.board.exercise_board.user.domain.Form.SignUpForm;
import org.board.exercise_board.user.domain.model.Token;
import org.board.exercise_board.user.domain.model.User;
import org.board.exercise_board.user.exception.CustomException;
import org.board.exercise_board.user.exception.ErrorCode;
import org.board.exercise_board.user.service.EmailService;
import org.board.exercise_board.user.service.TokenService;
import org.board.exercise_board.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignUpApplication {
  private final UserService userService;
  private final TokenService tokenService;
  private final EmailService emailService;

  public UserDto signup(SignUpForm signUpForm) {

    if(userService.isExistLoginId(signUpForm.getLoginId())) {
      throw new CustomException(ErrorCode.ALREADY_REGISTERD_ID);
    }

    if(userService.isExistEmail(signUpForm.getEmail())) {
      throw new CustomException(ErrorCode.ALREATY_REGISTERD_EMAIL);
    }

    // signupform을 통해 입력받은 user 정보 저장
    User user = userService.save(signUpForm);

    // user 정보를 사용하여 token 생성
    Token token = tokenService.createToken(user);

    // 생성된 token 정보를 인증 링크에 포함시켜 전송
    emailService.sendEmail(signUpForm.getEmail(), token);

    return UserDto.entityToDto(user);
  }
}
