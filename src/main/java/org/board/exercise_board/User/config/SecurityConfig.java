package org.board.exercise_board.User.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // csrf 방지
        .csrf(
            AbstractHttpConfigurer::disable
        )

//        .authorizeHttpRequests(
//            (authorizeRequests) ->
//                authorizeRequests
//                    .requestMatchers("/","/**").permitAll()
//                    .anyRequest().authenticated()
//        );

        // 기본 로그인페이지 없애기
        .formLogin(
            AbstractHttpConfigurer::disable
        );

    return http.build();
  }

}
