package org.board.exercise_board.comment.service;


import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.comment.domain.dto.CommentDto;
import org.board.exercise_board.comment.domain.form.CommentForm;
import org.board.exercise_board.comment.domain.model.Comment;
import org.board.exercise_board.comment.domain.repository.CommentRepository;
import org.board.exercise_board.exception.CustomException;
import org.board.exercise_board.exception.ErrorCode;
import org.board.exercise_board.post.domain.model.Post;
import org.board.exercise_board.post.domain.repository.PostRepository;
import org.board.exercise_board.user.domain.model.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

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
            CommentForm commentForm, Comment comment
    ) {

        comment.editContent(commentForm.getContent());

        return CommentDto.entityToDto(commentRepository.save(comment));
    }


    public String deleteComment(Comment comment) {

      commentRepository.delete(comment);
      return "삭제가 완료되었습니다.";
    }

}
