package org.board.exercise_board.post.application;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.post.domain.model.Post;
import org.board.exercise_board.post.exception.PostCustomException;
import org.board.exercise_board.post.exception.PostErrorCode;
import org.board.exercise_board.post.service.PostService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteApplication {

  private final PostService postService;

  public String deletePost(String writerId, Long postId) {
    Post post = postService.find(postId);

    // 로그인한 사용자가 작성자인지 확인
    if(!Objects.equals(writerId, post.getUser().getLoginId())) {
      throw new PostCustomException(PostErrorCode.NOT_HAVE_RIGHT);
    }

    return postService.deletePost(post);
  }

}
