package org.board.exercise_board.post.domain.form;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriteForm {
  private String subject;
  private Map<String, Object> content;
}
