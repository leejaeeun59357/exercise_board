package org.board.exercise_board.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.board.exercise_board.user.application.SignInApplication;
import org.board.exercise_board.user.application.VerifyEmailApplication;
import org.board.exercise_board.user.domain.Form.SignUpForm;
import org.board.exercise_board.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Mock
    private VerifyEmailApplication verifyEmailApplication;
    @Mock
    private SignInApplication signInApplication;
    @Mock
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @DisplayName("회원 가입 성공")
    @Test
    void signup() throws Exception {

        // given
        SignUpForm signUpForm = SignUpForm.builder()
                .loginId("test")
                .password("test")
                .email("test@naver.com")
                .build();


        ResultActions resultActions =
                mockMvc.perform(post("/user/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(objectMapper.writeValueAsString(signUpForm))
                        )
                        .andExpect(status().isOk());

    }

    @Test
    void verifyEmail() {
    }

    @Test
    void signin() {
    }
}