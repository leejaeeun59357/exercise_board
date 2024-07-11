package org.board.exercise_board.liked.service;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.exception.CustomException;
import org.board.exercise_board.exception.ErrorCode;
import org.board.exercise_board.liked.domain.model.Liked;
import org.board.exercise_board.liked.domain.model.Type;
import org.board.exercise_board.liked.domain.repository.LikedRepository;
import org.board.exercise_board.user.domain.model.User;
import org.board.exercise_board.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikedService {

  private final LikedRepository likedRepository;
  private final UserRepository userRepository;


  public String saveLiked(Type type, Long id, String loginId) {

    User user = userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    // 이메일 인증이 완료되었는지 검사
    if (!user.getVerifiedStatus()) {
      throw new CustomException(ErrorCode.NOT_VERIFIED_EMAIL);
    }

    synchronized(this){

      // 좋아요가 중복되었을 때
      if(likedRepository.existsByTypeAndTypeIdAndUser(type,id,user)) {
        Liked liked = likedRepository.findByTypeAndTypeIdAndUser(type,id,user);
        likedRepository.delete(liked);

        return "좋아요를 취소했습니다.";
      }

      Liked liked = Liked.builder()
          .type(type)
          .typeId(id)
          .user(user)
          .build();

      // 정상적으로 저장
      likedRepository.save(liked);
      return "좋아요를 눌렀습니다.";
    }
  }

}
