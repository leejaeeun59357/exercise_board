package org.board.exercise_board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class ExerciseBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExerciseBoardApplication.class, args);
    }

}
