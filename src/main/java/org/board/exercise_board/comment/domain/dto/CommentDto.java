package org.board.exercise_board.comment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.board.exercise_board.comment.domain.model.Comment;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
  private Long commentId;
  private String content;
  private String postSubject;
  private String commentWriter;

  public static CommentDto entityToDto(Comment comment) {
    return CommentDto.builder()
        .commentId(comment.getId())
        .content(comment.getContent())
        .postSubject(comment.getPost().getSubject())
        .commentWriter(comment.getUser().getLoginId())
        .build();
  }
}
