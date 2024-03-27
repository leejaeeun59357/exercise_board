package org.board.exercise_board.User.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@AuditOverride(forClass = BaseEntity.class)
public class User extends BaseEntity{

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

  private Role role;

  // 이메일 인증이 완료되면 true로 변경하기 위해 @Setter 사용
  @Setter
  private Boolean verified_status;


}
