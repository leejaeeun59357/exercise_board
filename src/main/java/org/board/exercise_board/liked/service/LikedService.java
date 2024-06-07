package org.board.exercise_board.liked.service;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.comment.service.CommentService;
import org.board.exercise_board.liked.domain.model.Liked;
import org.board.exercise_board.liked.domain.model.Type;
import org.board.exercise_board.liked.domain.repository.LikedRepository;
import org.board.exercise_board.post.service.PostService;
import org.board.exercise_board.user.domain.model.User;
import org.board.exercise_board.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikedService {

  private final LikedRepository likedRepository;
  private final PostService postService;
  private final CommentService commentService;
  private final UserService userService;
  Map<Type, FindByType> findServiceByType;

  @PostConstruct
  void init() {
    findServiceByType = Map.of(Type.POST, postService, Type.COMMENT, commentService);
  }

  public String saveLiked(Type type, Long id, String loginId) {

    findServiceByType.get(type).find(id);

    User user = userService.findUser(loginId);

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
