package org.board.exercise_board.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.board.exercise_board.user.Security.JwtTokenProvider;
import org.board.exercise_board.user.domain.Dto.UserDto;
import org.board.exercise_board.user.domain.Form.SignInForm;
import org.board.exercise_board.user.domain.Form.SignUpForm;
import org.board.exercise_board.user.domain.model.JwtToken;
import org.board.exercise_board.user.domain.model.EmailToken;
import org.board.exercise_board.user.domain.model.User;
import org.board.exercise_board.user.domain.repository.UserRepository;
import org.board.exercise_board.exception.CustomException;
import org.board.exercise_board.exception.ErrorCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final JwtTokenProvider jwtTokenProvider;
  private final EmailService emailService;
//  private final NotificationService notificationService;


  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


  public UserDto signUp(SignUpForm signUpForm) {

    if(userRepository.existsByLoginId(signUpForm.getLoginId())) {
      throw new CustomException(ErrorCode.ALREADY_REGISTERD_ID);
    }
    if(this.userRepository.existsByEmail(signUpForm.getEmail())) {
      throw new CustomException(ErrorCode.ALREADY_REGISTERD_EMAIL);
    }

    String encodePassword = passwordEncoder.encode(signUpForm.getPassword());
    User user = User.formToEntity(signUpForm, encodePassword);

    userRepository.save(user);

    EmailToken emailToken = emailService.createEmailToken(user);
    emailService.sendEmail(signUpForm.getEmail(), emailToken);

    return UserDto.entityToDto(user);
  }

  public JwtToken signin(SignInForm signInForm) {
    // 1. token 생성
    UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(
        signInForm.getLoginId(), signInForm.getPassword());

    // 2. 해당 token을 이용하여 인증 절차 거친다.
    Authentication authentication = authenticationManagerBuilder.getObject()
        .authenticate(authenticationToken);

    // 3. 인증 절차 통과하면 인증이 완료되었다는 뜻
    // 따라서 token을 발행한다.
    JwtToken token = jwtTokenProvider.createToken(authentication);

    // TODO - 로그인이 되자마자 SSE 연결하는 메서드 필요함
//    notificationService.subscribe(signInForm.getLoginId());

    return token;
  }

  public void verifyEmail(String tokenId) {

    EmailToken emailToken = emailService.findToken(tokenId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    // 이미 인증완료되었다면 Exception 발생
    if(emailToken.getUser().getVerifiedStatus()) {
      throw new CustomException(ErrorCode.ALREADY_VERIFIED);
    }

    // 만료 시간이 지났다면 Exception 발생
    if(emailService.verifyExpirationDateTime(emailToken)) {
      emailService.updateVerifyStatus(emailToken);
    } else {
      throw new CustomException(ErrorCode.EXPIRATION_TIME_IS_OVER);
    }
  }



  @Override
  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
    log.info(" --- 회원 정보 찾기, {} --- ", loginId);
    return userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
  }
}
