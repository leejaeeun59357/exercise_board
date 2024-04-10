package org.board.exercise_board.Comment.domain.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentForm {

  @NotBlank(message = "댓글 내용 작성은 필수입니다.")
  private String content;
}
