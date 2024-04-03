package org.board.exercise_board.Post.service;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Post.domain.Dto.PostDto;
import org.board.exercise_board.Post.domain.form.WriteForm;
import org.board.exercise_board.Post.domain.model.Post;
import org.board.exercise_board.Post.domain.repository.PostRepository;
import org.board.exercise_board.Post.exception.PostCustomException;
import org.board.exercise_board.Post.exception.PostErrorCode;
import org.board.exercise_board.User.domain.model.User;
import org.board.exercise_board.User.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WriteService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public PostDto savePost(WriteForm writeForm, String writerId) {
    User user = userRepository.findByLoginId(writerId)
        .orElseThrow(() -> new PostCustomException(PostErrorCode.NOT_FOUND_USER));

    Post post = Post.formToEntity(writeForm);
    post.setUser(user);

    return PostDto.entityToDto(postRepository.save(post));
  }

  public boolean isEmailVerified(String writerId) {
    User user = userRepository.findByLoginId(writerId)
        .orElseThrow(() -> new PostCustomException(PostErrorCode.NOT_FOUND_USER));

    return user.getVerifiedStatus();
  }
}
