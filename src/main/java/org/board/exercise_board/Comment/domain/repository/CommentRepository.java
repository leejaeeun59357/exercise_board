package org.board.exercise_board.Comment.domain.repository;

import java.util.List;
import org.board.exercise_board.Comment.domain.model.Comment;
import org.board.exercise_board.Post.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findAllByPost(Post post);
}
