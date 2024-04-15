package org.board.exercise_board.Liked.domain.repository;

import org.board.exercise_board.Liked.domain.model.Liked;
import org.board.exercise_board.Liked.domain.model.Type;
import org.board.exercise_board.User.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedRepository extends JpaRepository<Liked, Long> {
  Boolean existsByTypeAndTypeIdAndUser(Type type, Long typeId, User user);
  Liked findByTypeAndTypeIdAndUser(Type type, Long typeId, User user);
}
