package org.board.exercise_board.User.service;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.User.domain.Dto.UserDto;
import org.board.exercise_board.User.domain.Form.SignUpForm;
import org.board.exercise_board.User.domain.model.User;
import org.board.exercise_board.User.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * 이미 등록된 email 인지 검사
   * @param email
   * @return true/false
   */
  public boolean isExistEmail(String email) {
    return userRepository.findByEmail(email).isPresent();
  }

  /**
   * 이미 등록된 ID 인지 검사
   * @param loginId
   * @return true/false
   */
  public boolean isExistLoginId(String loginId) {
    return userRepository.findByLoginId(loginId).isPresent();
  }

  /**
   * 입력받은 form 정보를 pw는 encoding 하여 db에 저장
   * @param signUpForm
   * @return
   */
  public User save(SignUpForm signUpForm) {
    User user = UserDto.formToEntity(signUpForm);
    user.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
    return userRepository.save(user);
  }
}
