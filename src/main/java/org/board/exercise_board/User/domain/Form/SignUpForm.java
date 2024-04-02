package org.board.exercise_board.User.domain.Form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpForm {
  private String loginId;
  private String password;
  private String email;
}
