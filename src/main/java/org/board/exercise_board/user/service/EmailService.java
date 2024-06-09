package org.board.exercise_board.user.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.board.exercise_board.user.domain.model.Token;
import org.board.exercise_board.user.exception.CustomException;
import org.board.exercise_board.user.exception.ErrorCode;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender javaMailSender;

  public void sendEmail(String receiverEmail, Token token) {
    MimeMessage message = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

      // 메일 수신자 설정
      messageHelper.setTo(receiverEmail);

      // 메일 제목 설정
      messageHelper.setSubject("회원가입 인증 메일");

      // 메일 내용 설정
      // HTML 적용됨
      String linkAddress = "<a href=http://localhost:8080/user/verified/"+token.getId()+">인증 링크</a>";
      messageHelper.setText(linkAddress, true);

      // 메일 전송
      javaMailSender.send(message);

    } catch (Exception e) {
      throw new CustomException(ErrorCode.MAIL_SEND_FAIL);
    }
  }
}