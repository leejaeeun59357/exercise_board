package org.board.exercise_board.user.domain.repository;

import java.util.Optional;
import org.board.exercise_board.user.domain.model.EmailToken;
import org.board.exercise_board.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTokenRepository extends JpaRepository<EmailToken, String> {
  Optional<EmailToken> findById(String tokenId);
  Boolean existsByUser(User user);
  EmailToken findByUser(User user);
}
