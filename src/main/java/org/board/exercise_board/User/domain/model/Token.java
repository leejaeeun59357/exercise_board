package org.board.exercise_board.User.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Token extends BaseEntity{
  @Id
  private Long id;

  @OneToOne(fetch = FetchType.EAGER)
  @MapsId
  @JoinColumn(name = "user_id")
  @Setter
  private User user;

  @Setter
  private LocalDateTime expirationDate;
}
