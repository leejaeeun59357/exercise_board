package org.board.exercise_board.User.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;

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

        // Jwt를 사용하기 때문에 session은 사용X
        .sessionManagement( (session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        .httpBasic(AbstractHttpConfigurer::disable)

        //홈,로그인,회원가입 페이지는 로그인 없이 접근 가능
        .authorizeHttpRequests(
            (request) -> request
                .requestMatchers("/post/read","/user/**","/post/search","/post/read/**").permitAll()
                .anyRequest().authenticated()
        )


        .formLogin(
            AbstractHttpConfigurer::disable
        )

        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

}