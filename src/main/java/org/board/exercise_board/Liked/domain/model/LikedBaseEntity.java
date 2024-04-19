package org.board.exercise_board.Liked.domain.model;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class LikedBaseEntity {

  @CreatedDate
  private LocalDateTime createdDateTime;

  @LastModifiedDate
  private LocalDateTime modifiedDateTime;
}
