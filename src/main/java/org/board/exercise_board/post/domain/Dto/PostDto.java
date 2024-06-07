package org.board.exercise_board.post.domain.Dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.board.exercise_board.post.domain.model.Post;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
  private Long postId;
  private String subject;
  private Map<String, Object> content;
  private String writerLoginId;

  public static PostDto entityToDto(Post post) {
    return PostDto.builder()
        .postId(post.getId())
        .subject(post.getSubject())
        .content(post.getContent())
        .writerLoginId(post.getUser().getLoginId())
        .build();
  }
}
