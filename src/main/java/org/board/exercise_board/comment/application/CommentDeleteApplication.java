package org.board.exercise_board.comment.application;

import static org.board.exercise_board.comment.exception.CommentErrorCode.WRITER_ONLY;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.comment.domain.model.Comment;
import org.board.exercise_board.comment.exception.CommentCustomException;
import org.board.exercise_board.comment.service.CommentService;
import org.board.exercise_board.post.domain.model.Post;
import org.board.exercise_board.post.service.PostService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentDeleteApplication {

  private final CommentService commentService;
  private final PostService postService;

  public String deleteComment(Long postId, Long commentId, String userId) {
    // 해당 게시글에 해당 댓글이 존재하는지 검사
    Post post = postService.find(postId);
    Comment comment = commentService.findCommentInPost(post, commentId);

    // 게시글 작성자이거나, 댓글 작성자만 삭제 가능
    if (Objects.equals(userId, comment.getUser().getLoginId()) ||
        Objects.equals(userId, post.getUser().getLoginId())) {
      return commentService.deleteComment(comment);
    } else {
      throw new CommentCustomException(WRITER_ONLY);
    }
  }
}
