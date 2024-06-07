package org.board.exercise_board.comment.domain.repository;

import java.util.List;
import java.util.Optional;
import org.board.exercise_board.comment.domain.model.Comment;
import org.board.exercise_board.post.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findAllByPost(Post post);
  Optional<Comment> findByIdAndPost(Long commentId, Post post);
}
