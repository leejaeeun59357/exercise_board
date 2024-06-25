package org.board.exercise_board.post.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.comment.domain.dto.CommentDto;
import org.board.exercise_board.comment.domain.repository.CommentRepository;
import org.board.exercise_board.exception.CustomException;
import org.board.exercise_board.exception.ErrorCode;
import org.board.exercise_board.post.domain.Dto.PostDto;
import org.board.exercise_board.post.domain.Dto.PostOneDto;
import org.board.exercise_board.post.domain.form.ModifyForm;
import org.board.exercise_board.post.domain.form.WriteForm;
import org.board.exercise_board.post.domain.model.Post;
import org.board.exercise_board.post.domain.repository.PostRepository;
import org.board.exercise_board.user.domain.model.User;
import org.board.exercise_board.user.domain.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final CommentRepository commentRepository;


  public PostDto writePost(WriteForm writeForm, String writerId) {
    // 제목이나 내용이 null값인지 검사
    if (writeForm.getSubject() == null || writeForm.getSubject().isEmpty()) {
      throw new CustomException(ErrorCode.SUBJECT_IS_EMPTY);
    }

    if (writeForm.getContent() == null || writeForm.getContent().isEmpty()) {
      throw new CustomException(ErrorCode.CONTENT_IS_EMPTY);
    }
    User user = userRepository.findByLoginId(writerId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    // 이메일 인증이 완료되었는지 검사
    if (!user.getVerifiedStatus()) {
      throw new CustomException(ErrorCode.NOT_VERIFIED_EMAIL);
    }

    Post post = Post.formToEntity(writeForm);
    post.setUser(user);

    return PostDto.entityToDto(postRepository.save(post));
  }



  public Post find(Long postId) {
    return postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_IS_NOT_EXIST));
  }

  public String deletePost(String writerId, Long postId) {
    Post post = this.find(postId);

    // 로그인한 사용자가 작성자인지 확인
    if(!Objects.equals(writerId, post.getUser().getLoginId())) {
      throw new CustomException(ErrorCode.NOT_HAVE_RIGHT);
    }

    postRepository.delete(post);

    return "게시물 삭제가 완료되었습니다.";
  }


  public Page<Post> readAllPosts(Pageable pageable) {
    return postRepository.findAll(pageable);
  }

  public PostOneDto readOnePost(Long postId) {
    Post post = this.find(postId);

    List<CommentDto> comments = commentRepository.findAllByPost(post)
            .stream().map(CommentDto::entityToDto)
            .collect(Collectors.toList());

    return PostOneDto.entityToDto(post, comments);
  }


  public PostDto modifyPost(Long postId,ModifyForm modifyForm, String writerId) {
    // 변경 후 제목 null 일 때,
    if (ObjectUtils.isEmpty(modifyForm.getAfterSubject())) {
      throw new CustomException(ErrorCode.SUBJECT_IS_EMPTY);
    }

    // 내용 null 일 때,
    if (ObjectUtils.isEmpty(modifyForm.getContent())) {
      throw new CustomException(ErrorCode.CONTENT_IS_EMPTY);
    }

    // 해당 제목의 게시물 찾기
    Post post = this.find(postId);

    // 수정하려는 사람과 작성자가 동일 인물인지 확인
    if(!Objects.equals(writerId, post.getUser().getLoginId())) {
      throw new CustomException(ErrorCode.NOT_HAVE_RIGHT);
    }

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
      throw new CustomException(ErrorCode.KEYWORD_IS_EMPTY);
    }

    return postRepository.findBySubjectContaining(keyword,pageable)
        .stream().map(PostDto::entityToDto)
        .collect(Collectors.toList());
  }
}
