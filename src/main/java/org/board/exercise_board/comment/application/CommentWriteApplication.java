package org.board.exercise_board.comment.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.comment.domain.dto.CommentDto;
import org.board.exercise_board.comment.domain.form.CommentForm;
import org.board.exercise_board.comment.service.CommentService;
import org.board.exercise_board.comment.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentWriteApplication {

  private final CommentService commentService;
  private final NotificationService notificationService;

  @Transactional
  public CommentDto saveComment(CommentForm commentForm, Long postId, String writerId) {

    notificationService.notifyComment(postId, writerId);
    return commentService.saveComment(commentForm,postId,writerId);
  }

}