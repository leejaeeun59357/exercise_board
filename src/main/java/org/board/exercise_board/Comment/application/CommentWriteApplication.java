package org.board.exercise_board.Comment.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Comment.domain.dto.CommentDto;
import org.board.exercise_board.Comment.domain.form.CommentWriteForm;
import org.board.exercise_board.Comment.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentWriteApplication {

  private final CommentService commentService;

  @Transactional
  public CommentDto saveComment(CommentWriteForm commentWriteForm, Long postId, String writerId) {
    return commentService.saveComment(commentWriteForm,postId,writerId);
  }

}