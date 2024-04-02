package org.board.exercise_board.User.domain.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.board.exercise_board.User.domain.model.User;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private String loginId;
  private String email;
  private Boolean verifiedStatus;


  /**
   * User Entity를 사용하고 싶은 데이터만 DTO로 변환
   *
   * @param user
   * @return
   */
  public static UserDto entityToDto(User user) {
    return UserDto.builder()
        .loginId(user.getLoginId())
        .email(user.getEmail())
        .verifiedStatus(user.getVerifiedStatus())
        .build();
  }
}
