package org.board.exercise_board.post.application;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.post.domain.Dto.PostDto;
import org.board.exercise_board.post.exception.PostCustomException;
import org.board.exercise_board.post.exception.PostErrorCode;
import org.board.exercise_board.post.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchApplication {
  private final PostService postService;

  @Transactional
  public List<PostDto> searchPost(String keyword, Pageable pageable) {

    // 검색 키워드가 null 일 때
    if(Objects.equals(keyword, "") || keyword == null) {
      throw new PostCustomException(PostErrorCode.KEYWORD_IS_EMPTY);
    }

    List<PostDto> postDtos = postService.searchPost(keyword,pageable);

    if (postDtos.isEmpty()) {
      throw new PostCustomException(PostErrorCode.POST_IS_NOT_EXIST);
    }

    return postDtos;
  }
}
