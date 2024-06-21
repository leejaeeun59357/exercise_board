package org.board.exercise_board.comment.service;

import static org.board.exercise_board.comment.exception.CommentErrorCode.NOT_FOUND_COMMENT;
import static org.board.exercise_board.comment.exception.CommentErrorCode.WRITER_ONLY;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.comment.domain.dto.CommentDto;
import org.board.exercise_board.comment.domain.form.CommentForm;
import org.board.exercise_board.comment.domain.model.Comment;
import org.board.exercise_board.comment.domain.repository.CommentRepository;
import org.board.exercise_board.comment.exception.CommentCustomException;
import org.board.exercise_board.liked.service.FindByType;
import org.board.exercise_board.post.domain.model.Post;
import org.board.exercise_board.post.domain.repository.PostRepository;
import org.board.exercise_board.post.exception.PostCustomException;
import org.board.exercise_board.post.exception.PostErrorCode;
import org.board.exercise_board.user.domain.model.User;
import org.board.exercise_board.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService implements FindByType<Comment> {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserService userService;
  private final NotificationService notificationService;

  @Transactional
  public CommentDto saveComment(CommentForm commentForm, Long postId, String writerId) {
    notificationService.notifyComment(postId,writerId);

    Post post = this.findPost(postId);
    User user = userService.findUser(writerId);

    Comment comment = Comment.formToEntity(commentForm);

    comment.setPost(post);
    comment.setUser(user);

    return CommentDto.entityToDto(commentRepository.save(comment));
  }

  public Post findPost(Long postId) {
    return postRepository.findById(postId)
            .orElseThrow(() -> new PostCustomException(PostErrorCode.POST_IS_NOT_EXIST));
  }


  /**
   * 해당 게시물 내에 댓글이 존재하는지 확인
   *
   * @param post
   * @return
   */
  public Comment findCommentInPost(Post post, Long commentId) {
    return commentRepository.findByIdAndPost(commentId, post)
        .orElseThrow(() -> new CommentCustomException(NOT_FOUND_COMMENT));
  }

  public CommentDto modifyComment(
          CommentForm commentForm, String loginId, Long postId, Long commentId
  ) {
    Post post = this.findPost(postId);
    Comment comment = this.findCommentInPost(post, commentId);

    if(!Objects.equals(comment.getUser().getLoginId(), loginId)) {
      throw new CommentCustomException(WRITER_ONLY);
    }

    comment.setContent(commentForm.getContent());

    return CommentDto.entityToDto(commentRepository.save(comment));
  }


  public String deleteComment(Comment comment) {
    commentRepository.delete(comment);
    return "삭제가 완료되었습니다.";
  }

  @Override
  public Comment find(Long commentId) {
    return commentRepository.findById(commentId)
        .orElseThrow(() -> new CommentCustomException(NOT_FOUND_COMMENT));
  }
}
