package org.board.exercise_board.Post.domain.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.board.exercise_board.Post.domain.form.WriteForm;
import org.board.exercise_board.User.domain.model.User;
import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = PostBaseEntity.class)
public class Post extends PostBaseEntity {
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

  public static Post formToEntity(WriteForm writeForm) {
    return Post.builder()
        .subject(writeForm.getSubject())
        .content(writeForm.getContent())
        .build();
  }
}