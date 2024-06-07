package org.board.exercise_board.comment.domain.model;

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
import org.board.exercise_board.comment.domain.form.CommentForm;
import org.board.exercise_board.post.domain.model.Post;
import org.board.exercise_board.user.domain.model.User;
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

  public static Comment formToEntity(CommentForm commentForm) {
    return Comment.builder()
        .content(commentForm.getContent())
        .build();
  }

}
