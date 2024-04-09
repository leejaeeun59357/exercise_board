package org.board.exercise_board.Comment.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.board.exercise_board.Comment.domain.form.CommentWriteForm;
import org.board.exercise_board.Post.domain.model.Post;
import org.board.exercise_board.User.domain.model.User;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = CommentBaseEntity.class)
public class Comment extends CommentBaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  private String content;

  @ManyToOne
  @JoinColumn(name = "post_id")
  @Setter
  private Post post;
  
  @ManyToOne
  @JoinColumn(name = "user_id")
  @Setter
  private User user;

  public static Comment formToEntity(CommentWriteForm commentWriteForm) {
    return Comment.builder()
        .content(commentWriteForm.getContent())
        .build();
  }

}
