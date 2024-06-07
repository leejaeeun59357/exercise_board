package org.board.exercise_board.post.service;

import java.util.List;
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

  /**
   * 해당 사용자 정보와 입력 받은 Post 제목, 내용을 함께 DB에 저장
   *
   * @param writeForm
   * @param writerId
   * @return
   */
  public PostDto savePost(WriteForm writeForm, String writerId) {
    User user = userService.findUser(writerId);

    Post post = Post.formToEntity(writeForm);
    post.setUser(user);

    return PostDto.entityToDto(postRepository.save(post));
  }




  @Override
  public Post find(Long postId) {
    return postRepository.findById(postId)
        .orElseThrow(() -> new PostCustomException(PostErrorCode.POST_IS_NOT_EXIST));
  }

  public String deletePost(Post post) {
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

    return postRepository.findBySubjectContaining(keyword,pageable)
        .stream().map(PostDto::entityToDto)
        .collect(Collectors.toList());
  }
}
