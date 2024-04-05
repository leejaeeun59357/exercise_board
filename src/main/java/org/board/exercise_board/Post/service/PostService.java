package org.board.exercise_board.Post.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Post.domain.Dto.PostDto;
import org.board.exercise_board.Post.domain.form.ModifyForm;
import org.board.exercise_board.Post.domain.form.WriteForm;
import org.board.exercise_board.Post.domain.model.Post;
import org.board.exercise_board.Post.domain.repository.PostRepository;
import org.board.exercise_board.Post.exception.PostCustomException;
import org.board.exercise_board.Post.exception.PostErrorCode;
import org.board.exercise_board.User.domain.model.User;
import org.board.exercise_board.User.domain.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  /**
   * 해당 사용자 정보와 입력 받은 Post 제목, 내용을 함께 DB에 저장
   *
   * @param writeForm
   * @param writerId
   * @return
   */
  public PostDto savePost(WriteForm writeForm, String writerId) {
    User user = findUser(writerId);

    Post post = Post.formToEntity(writeForm);
    post.setUser(user);

    return PostDto.entityToDto(postRepository.save(post));
  }

  /**
   * 해당 사용자가 이메일 인증을 받았는지 검사
   *
   * @param writerId
   * @return
   */
  public boolean isEmailVerified(String writerId) {
    User user = findUser(writerId);
    return user.getVerifiedStatus();
  }

  /**
   * 사용자의 ID와 제목으로 해당하는 게시물 중 첫번째 게시물 찾기
   *
   * @param writerId 작성자 ID
   * @param subject  게시물 제목
   * @return
   */
  public Post findPosts(String writerId, String subject) {
    User user = findUser(writerId);

    // 해당 게시물이 존재하지 않는다면 에러 발생
    if (postRepository.findAllBySubjectAndUser(subject, user).isEmpty()) {
      throw new PostCustomException(PostErrorCode.POST_IS_NOT_EXIST);
    }

    Post post = postRepository.findAllBySubjectAndUser(subject, user).get(0);

    return post;
  }

  public void removePost(Post post) {
    postRepository.delete(post);
  }


  /**
   * ID로 해당 사용자 찾는 메서드
   *
   * @param writerId
   * @return
   */
  public User findUser(String writerId) {
    User user = userRepository.findByLoginId(writerId)
        .orElseThrow(() -> new PostCustomException(PostErrorCode.NOT_FOUND_USER));

    return user;
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
    Page<Post> postLists = postRepository.findBySubjectContaining(keyword, pageable);

    List<PostDto> postDtos = new ArrayList<>();
    for (Post post : postLists) {
      postDtos.add(PostDto.entityToDto(post));
    }

    return postDtos;
  }
}
