package org.board.exercise_board.post.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.liked.service.FindByType;
import org.board.exercise_board.post.domain.Dto.PostDto;
import org.board.exercise_board.post.domain.form.ModifyForm;
import org.board.exercise_board.post.domain.form.WriteForm;
import org.board.exercise_board.post.domain.model.Post;
import org.board.exercise_board.post.domain.repository.PostRepository;
import org.board.exercise_board.post.exception.PostCustomException;
import org.board.exercise_board.post.exception.PostErrorCode;
import org.board.exercise_board.user.domain.model.User;
import org.board.exercise_board.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService implements FindByType<Post> {

  private final PostRepository postRepository;
  private final UserService userService;


  @Transactional
  public PostDto writePost(WriteForm writeForm, String writerId) {
    // 제목이나 내용이 null값인지 검사
    if (writeForm.getSubject() == null || writeForm.getSubject().isEmpty()) {
      throw new PostCustomException(PostErrorCode.SUBJECT_IS_EMPTY);
    }

    if (writeForm.getContent() == null || writeForm.getContent().isEmpty()) {
      throw new PostCustomException(PostErrorCode.CONTENT_IS_EMPTY);
    }

    User user = userService.findUser(writerId);

    // 이메일 인증이 완료되었는지 검사
    if (!user.getVerifiedStatus()) {
      throw new PostCustomException(PostErrorCode.NOT_VERIFIED_EMAIL);
    }

    Post post = Post.formToEntity(writeForm);
    post.setUser(user);

    return PostDto.entityToDto(postRepository.save(post));
  }




  @Override
  public Post find(Long postId) {
    return postRepository.findById(postId)
        .orElseThrow(() -> new PostCustomException(PostErrorCode.POST_IS_NOT_EXIST));
  }

  @Transactional
  public String deletePost(String writerId, Long postId) {
    Post post = this.find(postId);

    // 로그인한 사용자가 작성자인지 확인
    if(!Objects.equals(writerId, post.getUser().getLoginId())) {
      throw new PostCustomException(PostErrorCode.NOT_HAVE_RIGHT);
    }

    postRepository.delete(post);

    return "게시물 삭제가 완료되었습니다.";
  }



  /**
   * 최근 날짜 순으로 모든 게시물 조회(Entity -> DTO)
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Page<Post> readAllPosts(Pageable pageable) {
    return postRepository.findAll(pageable);
  }


  /**
   * 입력받은 form대로 게시글 수정
   * @param post
   * @param modifyForm
   * @return
   */
  public PostDto modifyPost(Post post, ModifyForm modifyForm) {
    post.setSubject(modifyForm.getAfterSubject());
    post.setContent(modifyForm.getContent());
    return PostDto.entityToDto(postRepository.save(post));
  }

  /**
   * 해당 키워드를 포함하고 있는 게시글 조회
   *
   * @param keyword
   * @return
   */
  @Transactional
  public List<PostDto> searchPost(String keyword, Pageable pageable) {
    // 검색 키워드가 null 일 때
    if(Objects.equals(keyword, "") || keyword == null) {
      throw new PostCustomException(PostErrorCode.KEYWORD_IS_EMPTY);
    }

    return postRepository.findBySubjectContaining(keyword,pageable)
        .stream().map(PostDto::entityToDto)
        .collect(Collectors.toList());
  }
}
