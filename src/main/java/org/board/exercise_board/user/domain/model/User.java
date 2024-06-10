package org.board.exercise_board.user.domain.model;

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
import org.board.exercise_board.user.domain.Form.SignUpForm;
import org.hibernate.envers.AuditOverride;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@AuditOverride(forClass = BaseEntity.class)
public class User extends BaseEntity implements UserDetails {

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

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(this.getRole().toString()));
    return authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.loginId;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
