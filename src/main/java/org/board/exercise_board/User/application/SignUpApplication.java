package org.board.exercise_board.User.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.board.exercise_board.User.domain.Dto.UserDto;
import org.board.exercise_board.User.domain.Form.SignUpForm;
import org.board.exercise_board.User.domain.model.Token;
import org.board.exercise_board.User.domain.model.User;
import org.board.exercise_board.User.exception.CustomException;
import org.board.exercise_board.User.exception.ErrorCode;
import org.board.exercise_board.User.service.EmailService;
import org.board.exercise_board.User.service.TokenService;
import org.board.exercise_board.User.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignUpApplication {
  private final UserService userService;
  private final TokenService tokenService;
  private final EmailService emailService;

  public UserDto signup(SignUpForm signUpForm) {
    if (signUpForm.getEmail() == null || ObjectUtils.isEmpty(signUpForm.getEmail())) {
      throw new CustomException(ErrorCode.EMAIL_IS_NULL);
    }
    if (signUpForm.getLoginId() == null || ObjectUtils.isEmpty(signUpForm.getLoginId())) {
      throw new CustomException(ErrorCode.LOGIN_ID_IS_NULL);
    }
    if (signUpForm.getPassword() == null || ObjectUtils.isEmpty(signUpForm.getPassword())) {
      throw new CustomException(ErrorCode.PASSWORD_IS_NULL);
    }

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
