package org.board.exercise_board.comment.service;


import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.comment.domain.dto.CommentDto;
import org.board.exercise_board.comment.domain.form.CommentForm;
import org.board.exercise_board.comment.domain.model.Comment;
import org.board.exercise_board.comment.domain.repository.CommentRepository;
import org.board.exercise_board.exception.CustomException;
import org.board.exercise_board.exception.ErrorCode;
import org.board.exercise_board.notification.service.NotificationService;
import org.board.exercise_board.post.domain.model.Post;
import org.board.exercise_board.post.domain.repository.PostRepository;
import org.board.exercise_board.user.domain.model.User;
import org.board.exercise_board.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public CommentDto writeComment(CommentForm commentForm, Post post, User user) {

    Comment comment = Comment.builder()
            .content(commentForm.getContent())
            .post(post)
            .user(user)
            .build();

    return CommentDto.entityToDto(commentRepository.save(comment));
  }

  public Post findPost(Long postId) {
    return postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_IS_NOT_EXIST));
  }


  /**
   * 해당 게시물 내에 댓글이 존재하는지 확인
   *
   * @param post
   * @return
   */
  public Comment findCommentInPost(Post post, Long commentId) {
    return commentRepository.findByIdAndPost(commentId, post)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));
  }

  public CommentDto modifyComment(
          CommentForm commentForm, String loginId, Long postId, Long commentId
  ) {
    Post post = this.findPost(postId);
    Comment comment = this.findCommentInPost(post, commentId);

    if(!Objects.equals(comment.getUser().getLoginId(), loginId)) {
      throw new CustomException(ErrorCode.WRITER_ONLY);
    }

    comment.editContent(commentForm.getContent());

    return CommentDto.entityToDto(commentRepository.save(comment));
  }


  public String deleteComment(Long postId, Long commentId, String userId) {
    Post post = this.findPost(postId);
    Comment comment = this.findCommentInPost(post, commentId);

    // 게시글 작성자이거나, 댓글 작성자만 삭제 가능
    if (Objects.equals(userId, comment.getUser().getLoginId()) ||
            Objects.equals(userId, post.getUser().getLoginId())) {
      commentRepository.delete(comment);
      return "삭제가 완료되었습니다.";
    } else {
      throw new CustomException(ErrorCode.WRITER_ONLY);
    }
  }


}
