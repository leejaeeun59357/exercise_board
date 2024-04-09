package org.board.exercise_board.Comment.service;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Comment.domain.dto.CommentDto;
import org.board.exercise_board.Comment.domain.form.CommentWriteForm;
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
  public CommentDto saveComment(CommentWriteForm commentWriteForm, Long postId, String writerId) {
    Post post = postService.findPost(postId);
    User user = userService.findUser(writerId);

    Comment comment = Comment.formToEntity(commentWriteForm);

    comment.setPost(post);
    comment.setUser(user);

    return CommentDto.entityToDto(commentRepository.save(comment));
  }


}
