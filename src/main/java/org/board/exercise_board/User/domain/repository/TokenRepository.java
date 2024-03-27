package org.board.exercise_board.User.domain.repository;

import org.board.exercise_board.User.domain.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

}
