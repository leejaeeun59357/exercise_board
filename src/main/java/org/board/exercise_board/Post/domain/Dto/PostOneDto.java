package org.board.exercise_board.Post.domain.Dto;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.board.exercise_board.Comment.domain.dto.CommentDto;
import org.board.exercise_board.Post.domain.model.Post;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostOneDto {
  private Long postId;
  private String subject;
  private Map<String, Object> content;
  private String writerLoginId;
  private List<CommentDto> commentList;

  public static PostOneDto entityToDto(Post post, List<CommentDto> commentList) {
    return PostOneDto.builder()
        .postId(post.getId())
        .subject(post.getSubject())
        .content(post.getContent())
        .writerLoginId(post.getUser().getLoginId())
        .commentList(commentList)
        .build();
  }
}
