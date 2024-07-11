package org.board.exercise_board.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.comment.domain.model.Comment;
import org.board.exercise_board.comment.service.CommentService;
import org.board.exercise_board.exception.CustomException;
import org.board.exercise_board.exception.ErrorCode;
import org.board.exercise_board.post.domain.model.Post;
import org.board.exercise_board.post.service.PostService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeleteApplication {
    private final PostService postService;
    private final CommentService commentService;

    public String deletePost(String writerId, Long postId) {
        Post post = postService.findPost(postId);

        // 로그인한 사용자가 작성자인지 확인
        if(!Objects.equals(writerId, post.getUser().getLoginId())) {
            throw new CustomException(ErrorCode.NOT_HAVE_RIGHT);
        }

        return postService.deletePost(post);
    }

    public String deleteComment(Long postId, Long commentId, String userId) {
        Post post = postService.findPost(postId);
        Comment comment = commentService.findCommentInPost(post, commentId);

        // 게시글 작성자이거나, 댓글 작성자만 삭제 가능
        if (Objects.equals(userId, comment.getUser().getLoginId()) ||
                Objects.equals(userId, post.getUser().getLoginId())) {
            return commentService.deleteComment(comment);
        } else {
            throw new CustomException(ErrorCode.WRITER_ONLY);
        }
    }
}
