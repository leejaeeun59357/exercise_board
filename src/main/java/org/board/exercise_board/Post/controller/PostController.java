package org.board.exercise_board.Post.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Post.application.ModifyApplication;
import org.board.exercise_board.Post.application.ReadApplication;
import org.board.exercise_board.Post.application.DeleteApplication;
import org.board.exercise_board.Post.application.SearchApplication;
import org.board.exercise_board.Post.application.WriteApplication;
import org.board.exercise_board.Post.domain.Dto.PostDto;
import org.board.exercise_board.Post.domain.form.ModifyForm;
import org.board.exercise_board.Post.domain.form.WriteForm;
import org.board.exercise_board.Post.domain.model.Post;
import org.board.exercise_board.User.Security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  private final WriteApplication writeApplication;
  private final DeleteApplication deleteApplication;
  private final ReadApplication readApplication;
  private final ModifyApplication modifyApplication;
  private final SearchApplication searchApplication;

  /**
   * 게시글 작성하는 Controller
   *
   * @param customUserDetails 현재 로그인된 사용자 정보
   * @param writeForm         작성하고자 하는 제목, 내용
   * @return
   */
  @PostMapping("/write")
  public ResponseEntity<PostDto> writePost(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody WriteForm writeForm) {

    return ResponseEntity.ok(
        writeApplication.writePost(writeForm, customUserDetails.getUsername()));
  }

  /**
   * 해당 사용자와 제목이 일치하는 게시글 삭제
   *
   * @param customUserDetails
   * @param postId
   * @return
   */
  @DeleteMapping("/delete")
  public String deletePost(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestParam Long postId
  ) {
    return deleteApplication.deletePost(customUserDetails.getUsername(), postId);
  }

  /**
   * 전체 게시물을 10개씩 조회
   *
   * @param pageable
   * @return
   */
  @GetMapping("/read")
  public ResponseEntity<Page<Post>> readAllPost(
      @PageableDefault(size = 10, sort = "createdDate", direction = Direction.DESC) Pageable pageable
  ) {
    return ResponseEntity.ok(readApplication.readAllPosts(pageable));
  }

  /**
   * 작성자확인 후 작성자만 게시글 수정할 수 있도록 함
   *
   * @param customUserDetails
   * @param modifyForm
   * @return
   */
  @PutMapping("/modify")
  public ResponseEntity<PostDto> modifyPost(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody ModifyForm modifyForm
  ) {
    return ResponseEntity.ok(
        modifyApplication.modifyPost(modifyForm, customUserDetails.getUsername()));
  }

  /**
   * 키워드를 포함한 제목이 있는 게시글 조회, 별도의 인증이 필요하지 않음
   *
   * @param keyword
   * @param pageable
   * @return
   */
  @GetMapping("/search")
  public ResponseEntity<List<PostDto>> searchPost(
      @RequestParam String keyword,
      @PageableDefault(size = 10, sort = "createdDate", direction = Direction.DESC) Pageable pageable
  ) {
    return ResponseEntity.ok(searchApplication.searchPost(keyword, pageable));
  }
}
