package org.board.exercise_board.Post.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Post.domain.model.Post;
import org.board.exercise_board.Post.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RemoveApplication {

  private final PostService postService;

  @Transactional
  public String removePost(String writerId, String subject) {
    Post post = postService.findPosts(writerId, subject);
    postService.removePost(post);
    return "게시물 삭제가 완료되었습니다.";
  }

}
