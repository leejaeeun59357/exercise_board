package org.board.exercise_board.Comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Comment.application.CommentWriteApplication;
import org.board.exercise_board.Comment.domain.dto.CommentDto;
import org.board.exercise_board.Comment.domain.form.CommentForm;
import org.board.exercise_board.User.Security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

  private final CommentWriteApplication commentWriteApplication;

  @PostMapping("/{postId}/write")
  public ResponseEntity<CommentDto> writeComment(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable(value = "postId") Long postId,
      @RequestBody @Valid CommentForm commentForm
  ) {
    return ResponseEntity.ok(
        commentWriteApplication.saveComment(commentForm, postId,
            customUserDetails.getUsername()));
  }
}
