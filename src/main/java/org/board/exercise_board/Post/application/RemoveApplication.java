package org.board.exercise_board.Post.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Post.domain.model.Post;
import org.board.exercise_board.Post.service.PostService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveApplication {

  private final PostService postService;

  public String removePost(String writerId, String subject) {
    Post post = postService.findPosts(writerId, subject);
    postService.removePost(post);
    return "게시물 삭제가 완료되었습니다.";
  }

}
