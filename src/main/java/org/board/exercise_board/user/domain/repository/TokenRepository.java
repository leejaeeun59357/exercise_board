package org.board.exercise_board.user.domain.repository;

import java.util.Optional;
import org.board.exercise_board.user.domain.model.Token;
import org.board.exercise_board.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
  Optional<Token> findById(String tokenId);
  Boolean existsByUser(User user);
  Token findByUser(User user);
}
