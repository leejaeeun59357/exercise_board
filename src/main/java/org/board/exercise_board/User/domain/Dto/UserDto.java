package org.board.exercise_board.User.domain.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.board.exercise_board.User.domain.Form.SignUpForm;
import org.board.exercise_board.User.domain.model.User;
import org.board.exercise_board.User.domain.model.Role;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private String loginId;
  private String email;
  private Boolean verified_status;

  /**
   * 입력받은 form을 저장하기 위해 entity로 형태 변환
   * @param signUpForm
   * @return
   */
  public static User formToEntity(SignUpForm signUpForm) {
    return User.builder()
        .loginId(signUpForm.getLoginId())
        .email(signUpForm.getEmail())
        .password(signUpForm.getPassword())
        .verified_status(false)
        .role(Role.USER)
        .build();
  }

  /**
   * User Entity를 사용하고 싶은 데이터만 DTO로 변환
   * @param user
   * @return
   */
  public static UserDto entityToDto(User user) {
    return UserDto.builder()
        .loginId(user.getLoginId())
        .email(user.getEmail())
        .verified_status(user.getVerified_status())
        .build();
  }
}
