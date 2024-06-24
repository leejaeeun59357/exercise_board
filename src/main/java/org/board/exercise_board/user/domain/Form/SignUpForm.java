package org.board.exercise_board.user.domain.Form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
  @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
  private String email;
}
