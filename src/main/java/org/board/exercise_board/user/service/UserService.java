package org.board.exercise_board.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.board.exercise_board.post.exception.PostCustomException;
import org.board.exercise_board.post.exception.PostErrorCode;
import org.board.exercise_board.user.Security.JwtTokenProvider;
import org.board.exercise_board.user.domain.Form.SignUpForm;
import org.board.exercise_board.user.domain.model.JwtToken;
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

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


  public boolean isExistEmail(String email) {
    return userRepository.existsByEmail(email);
  }


  public boolean isExistLoginId(String loginId) {
    return userRepository.findByLoginId(loginId).isPresent();
  }


  public User signUp(SignUpForm signUpForm) {
    User user = User.formToEntity(signUpForm);
    user.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
    return userRepository.save(user);
  }

  public JwtToken signin(String loginId, String password) {
    // 1. token 생성
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginId, password);

    // 2. 해당 token을 이용하여 인증 절차 거친다.
    Authentication authentication = authenticationManagerBuilder.getObject()
        .authenticate(authenticationToken);

    // 3. 인증 절차 통과하면 인증이 완료되었다는 뜻
    // 따라서 token을 발행한다.
    JwtToken token = jwtTokenProvider.createToken(authentication);

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
