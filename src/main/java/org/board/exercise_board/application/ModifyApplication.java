package org.board.exercise_board.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.comment.domain.dto.CommentDto;
import org.board.exercise_board.comment.domain.form.CommentForm;
import org.board.exercise_board.comment.domain.model.Comment;
import org.board.exercise_board.comment.service.CommentService;
import org.board.exercise_board.exception.CustomException;
import org.board.exercise_board.exception.ErrorCode;
import org.board.exercise_board.post.domain.Dto.PostDto;
import org.board.exercise_board.post.domain.form.ModifyForm;
import org.board.exercise_board.post.domain.model.Post;
import org.board.exercise_board.post.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ModifyApplication {

    private final PostService postService;
    private final CommentService commentService;

    public CommentDto modifyComment(
            CommentForm commentForm, String loginId, Long postId, Long commentId
    ) {
        Post post = postService.findPost(postId);
        Comment comment = commentService.findCommentInPost(post, commentId);

        if(!Objects.equals(comment.getUser().getLoginId(), loginId)) {
            throw new CustomException(ErrorCode.WRITER_ONLY);
        }


        return commentService.modifyComment(commentForm, comment);
    }

    public PostDto modifyPost(Long postId, ModifyForm modifyForm, String writerId) {
        if (ObjectUtils.isEmpty(modifyForm.getAfterSubject())) {
            throw new CustomException(ErrorCode.SUBJECT_IS_EMPTY);
        }

        if (ObjectUtils.isEmpty(modifyForm.getContent())) {
            throw new CustomException(ErrorCode.CONTENT_IS_EMPTY);
        }

        Post post = postService.findPost(postId);

        // 수정하려는 사람과 작성자가 동일 인물인지 확인
        if(!Objects.equals(writerId, post.getUser().getLoginId())) {
            throw new CustomException(ErrorCode.NOT_HAVE_RIGHT);
        }

        return postService.modifyPost(modifyForm, post);
    }
}