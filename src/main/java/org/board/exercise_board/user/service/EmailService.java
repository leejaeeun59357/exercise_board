package org.board.exercise_board.user.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.board.exercise_board.user.domain.model.EmailToken;
import org.board.exercise_board.user.domain.model.User;
import org.board.exercise_board.user.domain.repository.EmailTokenRepository;
import org.board.exercise_board.exception.CustomException;
import org.board.exercise_board.exception.ErrorCode;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender javaMailSender;
  private final EmailTokenRepository emailTokenRepository;

  // 만료 시간은 5분으로 설정
  private final long EMAIL_TOKNE_EXPIRATION_MINUTE_TIME_VALUE = 5L;

  public void sendEmail(String receiverEmail, EmailToken emailToken) {
    MimeMessage message = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

      // 메일 수신자 설정
      messageHelper.setTo(receiverEmail);

      // 메일 제목 설정
      messageHelper.setSubject("회원가입 인증 메일");

      // 메일 내용 설정
      // HTML 적용됨
      String linkAddress = "<a href=http://localhost:8080/user/verified/"+ emailToken.getId()+">인증 링크</a>";
      messageHelper.setText(linkAddress, true);

      // 메일 전송
      javaMailSender.send(message);

    } catch (Exception e) {
      throw new CustomException(ErrorCode.MAIL_SEND_FAIL);
    }
  }

  public EmailToken createEmailToken(User user) {

    EmailToken emailToken = null;
    if (emailTokenRepository.existsByUser(user)) {
      emailToken = emailTokenRepository.findByUser(user);
    } else {
      emailToken = new EmailToken();
      emailToken.setUser(user);
    }
    emailToken.setExpirationTime(LocalDateTime.now().plusMinutes(
            EMAIL_TOKNE_EXPIRATION_MINUTE_TIME_VALUE));
    return emailTokenRepository.save(emailToken);
  }


  public Optional<EmailToken> findToken(String tokenId) {
    return emailTokenRepository.findById(tokenId);
  }


  public boolean verifyExpirationDateTime(EmailToken emailToken) {
    // 현재 시간이 만료시간을 지나지 않았을 때 true
    return !LocalDateTime.now().isAfter(emailToken.getExpirationDateTime());
  }


  public void updateVerifyStatus(EmailToken emailToken) {
    User user = emailToken.getUser();
    user.verifiedEmail();
    emailTokenRepository.save(emailToken);
  }
}
