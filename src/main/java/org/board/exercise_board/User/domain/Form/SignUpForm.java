package org.board.exercise_board.User.domain.Form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpForm {

  @NotBlank(message = "ID는 필수 입력값입니다.")
  private String loginId;

  @NotBlank(message = "비밀번호는 필수 입력값입니다.")
  private String password;

  @NotBlank(message = "이메일은 필수 입력값입니다.")
  @Email(message = "이메일 형식을 확인해주세요.")
  private String email;
}
