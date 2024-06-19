package org.board.exercise_board.post.domain.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.board.exercise_board.post.domain.form.WriteForm;
import org.board.exercise_board.user.domain.model.User;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  private String subject;

  @Type(JsonType.class)
  @Column(name = "CONTENT", columnDefinition = "longtext")
  @Setter
  private Map<String, Object> content;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @Setter
  private User user;

  @CreatedDate
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime modifiedDate;

  public static Post formToEntity(WriteForm writeForm) {
    return Post.builder()
        .subject(writeForm.getSubject())
        .content(writeForm.getContent())
        .build();
  }
}