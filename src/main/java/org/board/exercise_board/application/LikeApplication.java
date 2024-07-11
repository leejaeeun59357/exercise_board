package org.board.exercise_board.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.comment.service.CommentService;
import org.board.exercise_board.liked.domain.model.Type;
import org.board.exercise_board.liked.service.LikedService;
import org.board.exercise_board.post.service.PostService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LikeApplication {
    private final LikedService likedService;
    private final PostService postService;
    private final CommentService commentService;

    public String saveLiked(Type type, Long id, String loginId) {
        if (type == Type.POST) {
            postService.findPost(id);
        } else if (type == Type.COMMENT) {
            commentService.findComment(id);
        }

        return likedService.saveLiked(type, id, loginId);
    }
}
