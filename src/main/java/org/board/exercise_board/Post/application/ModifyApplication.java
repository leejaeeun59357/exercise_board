package org.board.exercise_board.Post.application;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Post.domain.Dto.PostDto;
import org.board.exercise_board.Post.domain.form.ModifyForm;
import org.board.exercise_board.Post.domain.model.Post;
import org.board.exercise_board.Post.exception.PostCustomException;
import org.board.exercise_board.Post.exception.PostErrorCode;
import org.board.exercise_board.Post.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class ModifyApplication {

  private final PostService postService;

  public PostDto modifyPost(Long postId,ModifyForm modifyForm, String writerId) {

    // 변경 후 제목 null 일 때,
    if (ObjectUtils.isEmpty(modifyForm.getAfterSubject())) {
      throw new PostCustomException(PostErrorCode.SUBJECT_IS_EMPTY);
    }

    // 내용 null 일 때,
    if (ObjectUtils.isEmpty(modifyForm.getContent())) {
      throw new PostCustomException(PostErrorCode.CONTENT_IS_EMPTY);
    }

    // 해당 제목의 게시물 찾기
    Post post = postService.findPost(postId);

    // 수정하려는 사람과 작성자가 동일 인물인지 확인
    if(!Objects.equals(writerId, post.getUser().getLoginId())) {
      throw new PostCustomException(PostErrorCode.NOT_HAVE_RIGHT);
    }

    // 변경 내용 저장
    return postService.modifyPost(post, modifyForm);
  }
}
