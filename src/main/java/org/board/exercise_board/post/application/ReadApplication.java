package org.board.exercise_board.post.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.comment.domain.dto.CommentDto;
import org.board.exercise_board.comment.service.CommentService;
import org.board.exercise_board.post.domain.Dto.PostOneDto;
import org.board.exercise_board.post.domain.model.Post;
import org.board.exercise_board.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadApplication {

  private final PostService postService;
  private final CommentService commentService;

  @Transactional(readOnly = true)
  public Page<Post> readAllPosts(Pageable pageable) {
    Page<Post> posts = postService.readAllPosts(pageable);

    return posts;
  }

  /**
   * 게시글과 해당 게시글에 작성된 댓글 리스트를 가진 PostOneDto 객체 반환
   *
   * @param postId
   * @return
   */
  public PostOneDto readOnePost(Long postId) {
    Post post = postService.find(postId);

    List<CommentDto> comments = commentService.findComments(post);

    return PostOneDto.entityToDto(post, comments);
  }

}
