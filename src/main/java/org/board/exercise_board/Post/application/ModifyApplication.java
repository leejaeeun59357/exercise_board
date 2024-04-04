package org.board.exercise_board.Post.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Post.domain.Dto.PostDto;
import org.board.exercise_board.Post.domain.form.ModifyForm;
import org.board.exercise_board.Post.domain.model.Post;
import org.board.exercise_board.Post.exception.PostCustomException;
import org.board.exercise_board.Post.exception.PostErrorCode;
import org.board.exercise_board.Post.service.PostService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyApplication {

  private final PostService postService;

  public PostDto modifyPost(ModifyForm modifyForm, String writerId) {
    // 변경 전 제목 null 일 때,
    if (modifyForm.getBeforeSubject() == null || modifyForm.getBeforeSubject().isEmpty()) {
      throw new PostCustomException(PostErrorCode.SUBJECT_IS_EMPTY);
    }

    // 변경 후 제목 null 일 때,
    if (modifyForm.getAfterSubject() == null || modifyForm.getAfterSubject().isEmpty()) {
      throw new PostCustomException(PostErrorCode.SUBJECT_IS_EMPTY);
    }

    // 내용 null 일 때,
    if (modifyForm.getContent() == null || modifyForm.getContent().isEmpty()) {
      throw new PostCustomException(PostErrorCode.CONTENT_IS_EMPTY);
    }

    // 해당 제목의 게시물 찾기
    Post post = postService.findPosts(writerId, modifyForm.getBeforeSubject());

    // 변경 내용 저장
    PostDto postDto = postService.modifyPost(post, modifyForm);

    return postDto;
  }
}
