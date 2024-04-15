package org.board.exercise_board.Liked.service;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.Comment.service.CommentService;
import org.board.exercise_board.Liked.domain.model.Liked;
import org.board.exercise_board.Liked.domain.model.Type;
import org.board.exercise_board.Liked.domain.repository.LikedRepository;
import org.board.exercise_board.Post.service.PostService;
import org.board.exercise_board.User.domain.model.User;
import org.board.exercise_board.User.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikedService {

  private final LikedRepository likedRepository;
  private final PostService postService;
  private final CommentService commentService;
  private final UserService userService;

  public String saveLiked(Type type, Long id, String loginId) {

    // 게시글이 존재하지 않을 때
    if(type == Type.POST) {
      var result = postService.findPost(id);

    } else if(type == Type.COMMENT) {

      // 댓글이 존재하지 않을 때
      var result = commentService.findComment(id);
    }

    User user = userService.findUser(loginId);

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
