package org.board.exercise_board.User.domain.repository;

import java.util.Optional;
import org.board.exercise_board.User.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByLoginId(String loginId);
  Boolean existsByEmail(String email);
}
