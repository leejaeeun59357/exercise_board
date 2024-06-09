package org.board.exercise_board.liked.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.liked.domain.model.Type;
import org.board.exercise_board.liked.service.LikedService;
import org.board.exercise_board.user.Security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikedController {

  private final LikedService likedService;

  @Operation(summary = "게시글 좋아요")
  @PostMapping("/post")
  public ResponseEntity<String> postLiked(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestParam Long postId
  ) {
    return ResponseEntity.ok(likedService.saveLiked(Type.POST,postId,
        customUserDetails.getUsername()));
  }

  @Operation(summary = "댓글 좋아요")
  @PostMapping("/comment")
  public ResponseEntity<String> commentLiked(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestParam Long commentId
  ) {
    return ResponseEntity.ok(likedService.saveLiked(Type.COMMENT,commentId,
        customUserDetails.getUsername()));
  }
}
