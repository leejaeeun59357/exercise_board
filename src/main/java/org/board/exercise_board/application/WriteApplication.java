package org.board.exercise_board.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.comment.domain.dto.CommentDto;
import org.board.exercise_board.comment.domain.form.CommentForm;
import org.board.exercise_board.comment.service.CommentService;
import org.board.exercise_board.exception.CustomException;
import org.board.exercise_board.exception.ErrorCode;
import org.board.exercise_board.notification.service.NotificationService;
import org.board.exercise_board.post.domain.Dto.PostDto;
import org.board.exercise_board.post.domain.form.WriteForm;
import org.board.exercise_board.post.domain.model.Post;
import org.board.exercise_board.post.service.PostService;
import org.board.exercise_board.user.domain.model.User;
import org.board.exercise_board.user.service.UserService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WriteApplication {
    private final PostService postService;
    private final CommentService commentService;
    private final NotificationService notificationService;
    private final UserService userService;

    public PostDto writePost(WriteForm writeForm, String writerId) {
        // 제목이나 내용이 null값인지 검사
        if (writeForm.getSubject() == null || writeForm.getSubject().isEmpty()) {
            throw new CustomException(ErrorCode.SUBJECT_IS_EMPTY);
        }

        if (writeForm.getContent() == null || writeForm.getContent().isEmpty()) {
            throw new CustomException(ErrorCode.CONTENT_IS_EMPTY);
        }


        return postService.writePost(writeForm, writerId);
    }


    public CommentDto writeComment(CommentForm commentForm, Long postId, String writerId) {
        notificationService.notifyComment(postId,writerId);

        Post post = postService.findPost(postId);
        User user = userService.findUser(writerId);

        // 이메일 인증이 완료되었는지 검사
        if (!user.getVerifiedStatus()) {
            throw new CustomException(ErrorCode.NOT_VERIFIED_EMAIL);
        }


        return commentService.writeComment(commentForm, post, user);
    }
}
