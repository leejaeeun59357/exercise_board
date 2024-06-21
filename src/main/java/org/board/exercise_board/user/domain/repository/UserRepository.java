package org.board.exercise_board.user.domain.repository;

import java.util.Optional;
import org.board.exercise_board.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByLoginId(String loginId);
  boolean existsByEmail(String email);
  boolean existsByLoginId(String loginId);
}
