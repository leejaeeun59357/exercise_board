package org.board.exercise_board.post.exception;

import lombok.Getter;

@Getter
public class PostCustomException extends RuntimeException{
  private final PostErrorCode postErrorCode;

  public PostCustomException(PostErrorCode postErrorCode) {
    super(postErrorCode.getDetail());
    this.postErrorCode = postErrorCode;
  }
}
