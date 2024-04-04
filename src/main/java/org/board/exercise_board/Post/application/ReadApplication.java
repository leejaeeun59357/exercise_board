package org.board.exercise_board.Post.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Post.domain.model.Post;
import org.board.exercise_board.Post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadApplication {

  private final PostService postService;

  @Transactional(readOnly = true)
  public Page<Post> readAllPosts(Pageable pageable) {
    Page<Post> posts = postService.readAllPosts(pageable);

    return posts;
  }

}
