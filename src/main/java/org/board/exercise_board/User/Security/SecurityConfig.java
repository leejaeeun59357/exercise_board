package org.board.exercise_board.User.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
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

        //홈,로그인,회원가입 페이지는 로그인 없이 접근 가능
        .authorizeHttpRequests(
            (request) -> request
                .requestMatchers("/","/user/signup","/user/login").permitAll()
                .anyRequest().authenticated()
        )


        .formLogin(
//            (form) -> form
//                .loginPage("/user/login")
//                .defaultSuccessUrl("/", true)
//                .permitAll()
            AbstractHttpConfigurer::disable
        );

    return http.build();
  }

}