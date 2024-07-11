package org.board.exercise_board.user.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.exception.CustomException;
import org.board.exercise_board.exception.ErrorCode;
import org.board.exercise_board.user.domain.Dto.UserDto;
import org.board.exercise_board.user.domain.Form.SignUpForm;
import org.board.exercise_board.user.domain.model.EmailToken;
import org.board.exercise_board.user.service.EmailService;
import org.board.exercise_board.user.service.UserService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignUpApplication {

    private final UserService userService;
    private final EmailService emailService;

    public UserDto signUp(SignUpForm signUpForm) {
        if(userService.isExistLoginId(signUpForm.getLoginId())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERD_ID);
        }
        if(userService.isExistEmail(signUpForm.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERD_EMAIL);
        }

        UserDto userDto = userService.signUp(signUpForm);

        EmailToken emailToken = emailService.createEmailToken(userDto);
        emailService.sendEmail(userDto.getEmail(), emailToken);

        return userDto;
    }
}
