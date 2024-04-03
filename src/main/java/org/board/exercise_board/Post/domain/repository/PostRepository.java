package org.board.exercise_board.Post.domain.repository;

import org.board.exercise_board.Post.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
