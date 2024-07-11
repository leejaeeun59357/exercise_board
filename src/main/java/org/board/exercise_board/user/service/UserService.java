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
  private final JwtTokenProvider jwtTokenProvider;

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


  public UserDto signUp(SignUpForm signUpForm) {

    String encodePassword = passwordEncoder.encode(signUpForm.getPassword());
    User user = User.formToEntity(signUpForm, encodePassword);

    userRepository.save(user);

    return UserDto.entityToDto(user);
  }

  public JwtToken signin(SignInForm signInForm) {
    User user = userRepository.findByLoginId(signInForm.getLoginId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    String inputPassword = signInForm.getPassword();
    String encodedPassword = user.getPassword();

    boolean isMatched = passwordEncoder.matches(inputPassword, encodedPassword);

    if(!isMatched) {
      throw new CustomException(ErrorCode.NOT_MATCHED_PASSWORD);
    }

    if(!user.getVerifiedStatus()) {
      throw new CustomException(ErrorCode.NOT_VERIFIED_EMAIL);
    }

    JwtToken token = jwtTokenProvider.createToken(
            user.getLoginId(), user.getRole().toString());

    return token;
  }


  public boolean isExistEmail(String email) {
    return userRepository.existsByEmail(email);
  }
  public boolean isExistLoginId(String loginId) {
    return userRepository.existsByLoginId(loginId);
  }

  public User findUser(String loginId) {
    return userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
  }


  @Override
  public User loadUserByUsername(String loginId) throws UsernameNotFoundException {
    log.info(" --- 회원 정보 찾기, {} --- ", loginId);
    return userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
  }
}
