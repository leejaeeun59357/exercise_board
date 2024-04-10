package org.board.exercise_board.Comment.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Comment.domain.dto.CommentDto;
import org.board.exercise_board.Comment.domain.form.CommentForm;
import org.board.exercise_board.Comment.domain.model.Comment;
import org.board.exercise_board.Comment.domain.repository.CommentRepository;
import org.board.exercise_board.Post.domain.model.Post;
import org.board.exercise_board.Post.service.PostService;
import org.board.exercise_board.User.domain.model.User;
import org.board.exercise_board.User.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostService postService;
  private final UserService userService;

  @Transactional
  public CommentDto saveComment(CommentForm commentForm, Long postId, String writerId) {
    Post post = postService.findPost(postId);
    User user = userService.findUser(writerId);

    Comment comment = Comment.formToEntity(commentForm);

    comment.setPost(post);
    comment.setUser(user);

    return CommentDto.entityToDto(commentRepository.save(comment));
  }

  /**
   * 해당 게시물에 작성된 댓글 리스트 반환
   *
   * @param post
   * @return
   */
  public List<CommentDto> findComments(Post post) {
    return commentRepository.findAllByPost(post).stream()
        .map(CommentDto::entityToDto)
        .collect(Collectors.toList());
  }

}
