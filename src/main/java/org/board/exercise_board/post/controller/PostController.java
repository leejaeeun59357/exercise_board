package org.board.exercise_board.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.post.application.ModifyApplication;
import org.board.exercise_board.post.domain.Dto.PostDto;
import org.board.exercise_board.post.domain.Dto.PostOneDto;
import org.board.exercise_board.post.domain.form.ModifyForm;
import org.board.exercise_board.post.domain.form.WriteForm;
import org.board.exercise_board.post.domain.model.Post;
import org.board.exercise_board.post.service.PostService;
import org.board.exercise_board.user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

//  private final ReadApplication readApplication;
  private final ModifyApplication modifyApplication;
  private final PostService postService;

  @Operation(summary = "게시글 작성")
  @PostMapping("/write")
  public ResponseEntity<PostDto> writePost(
      @AuthenticationPrincipal User user,
      @RequestBody WriteForm writeForm) {

    return ResponseEntity.ok(
        postService.writePost(writeForm, user.getUsername()));
  }


  @Operation(summary = "게시글 삭제")
  @DeleteMapping("/delete")
  public String deletePost(
      @AuthenticationPrincipal User user,
      @RequestParam Long postId
  ) {
    return postService.deletePost(user.getUsername(), postId);
  }

  @Operation(summary = "게시글 10개씩 조회")
  @GetMapping("/read")
  public ResponseEntity<Page<Post>> readAllPost(
      @PageableDefault(size = 10, sort = "createdDate", direction = Direction.DESC) Pageable pageable
  ) {
    return ResponseEntity.ok(postService.readAllPosts(pageable));
  }

  @Operation(summary = "게시글 1개 조회")
  @GetMapping("/read/{postId}")
  public ResponseEntity<PostOneDto> readOnePost(
      @PathVariable(value = "postId") Long postId
  ) {
    return ResponseEntity.ok(postService.readOnePost(postId));
  }

  @Operation(summary = "게시글 수정")
  @PutMapping("/modify")
  public ResponseEntity<PostDto> modifyPost(
      @AuthenticationPrincipal User user,
      @RequestParam Long postId,
      @RequestBody ModifyForm modifyForm
  ) {
    return ResponseEntity.ok(
        modifyApplication.modifyPost(postId,modifyForm, user.getUsername()));
  }


  @Operation(summary = "게시글 키워드 검색")
  @GetMapping("/search")
  public ResponseEntity<List<PostDto>> searchPost(
      @RequestParam String keyword,
      @PageableDefault(size = 10, sort = "createdDate", direction = Direction.DESC) Pageable pageable
  ) {
    return ResponseEntity.ok(postService.searchPost(keyword, pageable));
  }
}
