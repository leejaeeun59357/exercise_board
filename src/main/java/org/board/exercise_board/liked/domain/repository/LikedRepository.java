package org.board.exercise_board.liked.domain.repository;

import org.board.exercise_board.liked.domain.model.Liked;
import org.board.exercise_board.liked.domain.model.Type;
import org.board.exercise_board.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedRepository extends JpaRepository<Liked, Long> {
  boolean existsByTypeAndTypeIdAndUser(Type type, Long typeId, User user);
  Liked findByTypeAndTypeIdAndUser(Type type, Long typeId, User user);
}
