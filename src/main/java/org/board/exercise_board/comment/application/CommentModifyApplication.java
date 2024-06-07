package org.board.exercise_board.comment.application;

import static org.board.exercise_board.comment.exception.CommentErrorCode.WRITER_ONLY;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.comment.domain.dto.CommentDto;
import org.board.exercise_board.comment.domain.form.CommentForm;
import org.board.exercise_board.comment.domain.model.Comment;
import org.board.exercise_board.comment.exception.CommentCustomException;
import org.board.exercise_board.comment.service.CommentService;
import org.board.exercise_board.post.domain.model.Post;
import org.board.exercise_board.post.service.PostService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentModifyApplication {

  private final CommentService commentService;
  private final PostService postService;


  public CommentDto modifyComment(CommentForm commentForm, String loginId, Long postId, Long commentId) {

    // 해당 게시글에 해당 댓글이 있는지 검사
    Post post = postService.find(postId);
    Comment comment = commentService.findCommentInPost(post, commentId);

    // 현재 로그인된 사용자와 댓글 작성자가 동일한지 검사
    if(!Objects.equals(comment.getUser().getLoginId(), loginId)) {
      throw new CommentCustomException(WRITER_ONLY);
    }

    // 댓글 수정
    return commentService.modifyComment(comment, commentForm);
  }
}
