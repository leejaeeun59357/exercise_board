package org.board.exercise_board.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.board.exercise_board.user.Security.JwtTokenProvider;
import org.board.exercise_board.user.domain.Dto.UserDto;
import org.board.exercise_board.user.domain.Form.SignInForm;
import org.board.exercise_board.user.domain.Form.SignUpForm;
import org.board.exercise_board.user.domain.model.Role;
import org.board.exercise_board.user.domain.model.User;
import org.board.exercise_board.user.domain.repository.UserRepository;
import org.board.exercise_board.user.service.EmailService;
import org.board.exercise_board.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void signup_success() throws Exception {
        // given
        SignUpForm signUpForm = SignUpForm.builder()
                .loginId("test")
                .password("test")
                .email("test@naver.com")
                .build();

        UserDto userDto = UserDto.builder()
                .loginId("test")
                .email("test@naver.com")
                .verifiedStatus(false)
                .build();

        // when
        when(userService.signUp(signUpForm)).thenReturn(userDto);

        // then
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpForm))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("로그인 - 성공")
    @WithMockUser
    void signin_success() throws Exception {
        // given
        User user = User.builder()
                .id(1L)
                .loginId("test")
                .password(passwordEncoder.encode("test"))
                .email("test@naver.com")
                .role(Role.USER)
                .verifiedStatus(true)
                .createdDate(LocalDateTime.now())
                .build();

        SignInForm signInForm = SignInForm.builder()
                .loginId("test")
                .password("test")
                .build();

        // when
        when(userRepository.findByLoginId(signInForm.getLoginId()))
                .thenReturn(Optional.of(user));

        // then
        mockMvc.perform(post("/user/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInForm))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}