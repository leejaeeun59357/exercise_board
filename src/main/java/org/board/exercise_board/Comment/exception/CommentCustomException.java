package org.board.exercise_board.Comment.exception;

import lombok.Getter;

@Getter
public class CommentCustomException extends RuntimeException{

  private final CommentErrorCode commentErrorCode;

  public CommentCustomException(CommentErrorCode commentErrorCode) {
    super(commentErrorCode.getDetail());
    this.commentErrorCode = commentErrorCode;
  }
}
