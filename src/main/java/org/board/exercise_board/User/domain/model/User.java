package org.board.exercise_board.User.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.board.exercise_board.User.domain.Form.SignUpForm;
import org.hibernate.envers.AuditOverride;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@AuditOverride(forClass = BaseEntity.class)
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(unique = true)
  private String loginId;

  @Setter
  private String password;

  @Column(unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  private Role role;

  // 이메일 인증이 완료되면 true로 변경하기 위해 @Setter 사용
  @Setter
  private Boolean verifiedStatus;

  /**
   * 입력받은 form을 저장하기 위해 entity로 형태 변환
   *
   * @param signUpForm
   * @return
   */
  public static User formToEntity(SignUpForm signUpForm) {
    return User.builder()
        .loginId(signUpForm.getLoginId())
        .email(signUpForm.getEmail())
        .password(signUpForm.getPassword())
        .verifiedStatus(false)
        .role(Role.USER)
        .build();
  }
}
