package org.board.exercise_board.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.application.DeleteApplication;
import org.board.exercise_board.application.ModifyApplication;
import org.board.exercise_board.application.WriteApplication;
import org.board.exercise_board.comment.domain.dto.CommentDto;
import org.board.exercise_board.comment.domain.form.CommentForm;
import org.board.exercise_board.user.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

  private final WriteApplication writeApplication;
  private final ModifyApplication modifyApplication;
  private final DeleteApplication deleteApplication;

  @Operation(summary = "댓글 작성")
  @PostMapping("/{postId}/write")
  public ResponseEntity<CommentDto> writeComment(
      @AuthenticationPrincipal User user,
      @PathVariable(value = "postId") Long postId,
      @RequestBody @Valid CommentForm commentForm
  ) {
    return ResponseEntity.ok(
        writeApplication.writeComment(commentForm, postId,
            user.getUsername()));
  }

  @Operation(summary = "댓글 수정")
  @PutMapping("/{postId}/{commentId}")
  public ResponseEntity<CommentDto> modifyComment(
      @PathVariable(value = "postId") Long postId,
      @PathVariable(value = "commentId") Long commentId,
      @AuthenticationPrincipal User user,
      @RequestBody @Valid CommentForm commentForm
  ) {
    return ResponseEntity.ok(
        modifyApplication.modifyComment(commentForm, user.getUsername(), postId,
            commentId)
    );
  }

  @Operation(summary = "댓글 삭제")
  @DeleteMapping("/{postId}/{commentId}")
  public ResponseEntity<String> deleteComment(
      @PathVariable(value = "postId") Long postId,
      @PathVariable(value = "commentId") Long commentId,
      @AuthenticationPrincipal User user
  ) {
    return ResponseEntity.ok(deleteApplication.deleteComment(
        postId, commentId, user.getUsername()));
  }
}
