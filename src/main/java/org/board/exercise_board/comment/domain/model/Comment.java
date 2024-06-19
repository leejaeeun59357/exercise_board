package org.board.exercise_board.comment.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.board.exercise_board.comment.domain.form.CommentForm;
import org.board.exercise_board.post.domain.model.Post;
import org.board.exercise_board.user.domain.model.User;
import org.hibernate.envers.AuditOverride;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Comment {

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

  @CreatedDate
  private LocalDateTime createdDateTime;

  @LastModifiedDate
  private LocalDateTime modifiedDateTime;

  public static Comment formToEntity(CommentForm commentForm) {
    return Comment.builder()
        .content(commentForm.getContent())
        .build();
  }

}
