package org.board.exercise_board.Post.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Post.domain.Dto.PostDto;
import org.board.exercise_board.Post.domain.form.WriteForm;
import org.board.exercise_board.Post.exception.PostCustomException;
import org.board.exercise_board.Post.exception.PostErrorCode;
import org.board.exercise_board.Post.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WriteApplication {

  private final PostService postService;

  /**
   * 제목과 내용이 null값으로 들어왔는지, 작성하고자 하는 사용자가 이메일 인증이 완료되었는지 검사 후 게시글 저장
   *
   * @param writeForm
   * @param writerId
   * @return
   */
  @Transactional
  public PostDto writePost(WriteForm writeForm, String writerId) {

    // 제목이나 내용이 null값인지 검사
    if (writeForm.getSubject() == null || writeForm.getSubject().isEmpty()) {
      throw new PostCustomException(PostErrorCode.SUBJECT_IS_EMPTY);
    }

    if (writeForm.getContent() == null || writeForm.getContent().isEmpty()) {
      throw new PostCustomException(PostErrorCode.CONTENT_IS_EMPTY);
    }

    // 이메일 인증이 완료되었는지 검사
    if (!postService.isEmailVerified(writerId)) {
      throw new PostCustomException(PostErrorCode.NOT_VERIFIED_EMAIL);
    }

    return postService.savePost(writeForm, writerId);
  }
}
