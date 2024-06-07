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
public class ModifyForm {
  private String afterSubject;
  private Map<String, Object> content;
}
