package org.board.exercise_board.user.service;

import org.board.exercise_board.exception.CustomException;
import org.board.exercise_board.user.Security.JwtTokenProvider;
import org.board.exercise_board.user.domain.Dto.UserDto;
import org.board.exercise_board.user.domain.Form.SignInForm;
import org.board.exercise_board.user.domain.Form.SignUpForm;
import org.board.exercise_board.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private EmailService emailService;


    private PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() {
        // given
        SignUpForm signUpForm = SignUpForm.builder()
                .loginId("test1")
                .password("test1")
                .email("test1@naver.com")
                .build();

        // when
        UserDto userDto = userService.signUp(signUpForm);

        // then
        assertEquals(userDto.getLoginId(), "test1");
        assertEquals(userDto.getEmail(), "test1@naver.com");
        assertEquals(userDto.getVerifiedStatus(), false);
    }

    @Test
    @DisplayName("로그인 실패 - LoginId 가진 User 없음")
    void signin_Not_found_user() {

        //given
        SignInForm signInForm = SignInForm.builder()
                .loginId("NotExist")
                .password("NotExist")
                .build();

        // when
        when(userRepository.findByLoginId(signInForm.getLoginId()))
                .thenReturn(Optional.empty());
        Throwable exception =
                assertThrows(CustomException.class, () -> userService.signin(signInForm));


        // then
        assertEquals("해당 사용자를 찾을 수 없습니다.", exception.getMessage());
    }

}