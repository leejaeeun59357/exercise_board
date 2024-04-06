package org.board.exercise_board.User.service;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Post.exception.PostCustomException;
import org.board.exercise_board.Post.exception.PostErrorCode;
import org.board.exercise_board.User.Security.JwtTokenProvider;
import org.board.exercise_board.User.domain.Form.SignUpForm;
import org.board.exercise_board.User.domain.model.JwtToken;
import org.board.exercise_board.User.domain.model.User;
import org.board.exercise_board.User.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final JwtTokenProvider jwtTokenProvider;

  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * 이미 등록된 email 인지 검사
   *
   * @param email
   * @return true/false
   */
  public boolean isExistEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  /**
   * 이미 등록된 ID 인지 검사
   *
   * @param loginId
   * @return true/false
   */
  public boolean isExistLoginId(String loginId) {
    return userRepository.findByLoginId(loginId).isPresent();
  }

  /**
   * 입력받은 form 정보를 pw는 encoding 하여 db에 저장
   *
   * @param signUpForm
   * @return
   */
  public User save(SignUpForm signUpForm) {
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

  /**
   * 해당 사용자가 이메일 인증을 받았는지 검사
   *
   * @param writerId
   * @return
   */
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
}
