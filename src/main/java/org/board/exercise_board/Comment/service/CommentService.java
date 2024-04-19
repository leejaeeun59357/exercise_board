package org.board.exercise_board.Comment.service;

import static org.board.exercise_board.Comment.exception.CommentErrorCode.NOT_FOUND_COMMENT;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Comment.domain.dto.CommentDto;
import org.board.exercise_board.Comment.domain.form.CommentForm;
import org.board.exercise_board.Comment.domain.model.Comment;
import org.board.exercise_board.Comment.domain.repository.CommentRepository;
import org.board.exercise_board.Comment.exception.CommentCustomException;
import org.board.exercise_board.Liked.service.FindByType;
import org.board.exercise_board.Post.domain.model.Post;
import org.board.exercise_board.Post.service.PostService;
import org.board.exercise_board.User.domain.model.User;
import org.board.exercise_board.User.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService implements FindByType<Comment> {

  private final CommentRepository commentRepository;
  private final PostService postService;
  private final UserService userService;

  @Transactional
  public CommentDto saveComment(CommentForm commentForm, Long postId, String writerId) {
    Post post = postService.find(postId);
    User user = userService.findUser(writerId);

    Comment comment = Comment.formToEntity(commentForm);

    comment.setPost(post);
    comment.setUser(user);

    return CommentDto.entityToDto(commentRepository.save(comment));
  }

  /**
   * 해당 게시물에 작성된 댓글 리스트 반환
   *
   * @param post
   * @return
   */
  public List<CommentDto> findComments(Post post) {
    return commentRepository.findAllByPost(post).stream()
        .map(CommentDto::entityToDto)
        .collect(Collectors.toList());
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

  public CommentDto modifyComment(Comment comment, CommentForm commentForm) {
    comment.setContent(commentForm.getContent());
    return CommentDto.entityToDto(commentRepository.save(comment));
  }



  /**
   * 해당 댓글 삭제하는 메서드
   *
   * @param comment
   * @return
   */
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
