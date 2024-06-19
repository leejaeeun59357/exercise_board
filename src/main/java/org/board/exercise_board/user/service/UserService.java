package org.board.exercise_board.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.board.exercise_board.comment.service.NotificationService;
import org.board.exercise_board.post.exception.PostCustomException;
import org.board.exercise_board.post.exception.PostErrorCode;
import org.board.exercise_board.user.Security.JwtTokenProvider;
import org.board.exercise_board.user.domain.Dto.UserDto;
import org.board.exercise_board.user.domain.Form.SignInForm;
import org.board.exercise_board.user.domain.Form.SignUpForm;
import org.board.exercise_board.user.domain.model.JwtToken;
import org.board.exercise_board.user.domain.model.Token;
import org.board.exercise_board.user.domain.model.User;
import org.board.exercise_board.user.domain.repository.UserRepository;
import org.board.exercise_board.user.exception.CustomException;
import org.board.exercise_board.user.exception.ErrorCode;
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
  private final TokenService tokenService;
  private final EmailService emailService;
//  private final NotificationService notificationService;


  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


  public boolean isExistEmail(String email) {
    return userRepository.existsByEmail(email);
  }


  public boolean isExistLoginId(String loginId) {
    return userRepository.findByLoginId(loginId).isPresent();
  }


  public UserDto signUp(SignUpForm signUpForm) {

    if(this.isExistLoginId(signUpForm.getLoginId())) {
      throw new CustomException(ErrorCode.ALREADY_REGISTERD_ID);
    }
    if(this.isExistEmail(signUpForm.getEmail())) {
      throw new CustomException(ErrorCode.ALREATY_REGISTERD_EMAIL);
    }

    // TODO - User Entity setter 어노테이션 삭제 예정 따라서 수정 필요함
    User user = User.formToEntity(signUpForm);
    user.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
    userRepository.save(user);

    Token token = tokenService.createToken(user);
    emailService.sendEmail(signUpForm.getEmail(), token);

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


  public boolean isEmailVerified(String writerId) {
    User user = userRepository.findByLoginId(writerId)
        .orElseThrow(() -> new PostCustomException(PostErrorCode.NOT_FOUND_USER));
    return user.getVerifiedStatus();
  }

  /**
   * ID로 해당 사용자 찾는 메서드
   *
   * @param writerId
   * @return
   */
  public User findUser(String writerId) {
    User user = userRepository.findByLoginId(writerId)
        .orElseThrow(() -> new PostCustomException(PostErrorCode.NOT_FOUND_USER));

    return user;
  }

  @Override
  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
    log.info(" --- 회원 정보 찾기, {} --- ", loginId);
    return userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
  }
}
