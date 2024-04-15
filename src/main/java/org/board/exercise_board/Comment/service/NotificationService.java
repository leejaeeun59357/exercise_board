package org.board.exercise_board.Comment.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.board.exercise_board.Post.domain.model.Post;
import org.board.exercise_board.Post.service.PostService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

  private final PostService postService;
  public Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

  /**
   * 사용자가 로그인하면 subscribe 연결 요청하도록 하는 메서드
   * @param userLoginId
   * @return
   */
  public SseEmitter subscribe(String userLoginId) {

    // 1. 현재 Client를 위한 SseEmitter 객체 생성
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    // 2. 연결
    try {
      sseEmitter.send(SseEmitter.event().name("connect"));
    } catch (IOException e) {
      log.warn("== SSE 연결 중 exception 발생 : {} ==", e.toString());
      e.printStackTrace();
    }

    // 3. 저장
    sseEmitters.put(userLoginId,sseEmitter);

    // 4. 연결 종료 처리

    // (1) sseEmitter 연결이 완료될 경우
    sseEmitter.onCompletion(() -> sseEmitters.remove(userLoginId));
    // (2) sseEmitter 연결에 타임아웃이 발생할 경우
    sseEmitter.onTimeout(() -> sseEmitters.remove(userLoginId));
    // (3) sseEmitter 연결에 오류가 발생할 경우
    sseEmitter.onError((e) -> sseEmitters.remove(userLoginId));

    return sseEmitter;
  }


  /**
   * 게시글 작성자에게 댓글 작성되면 바로 알림을 보내는 메서드
   * @param postId
   */
  @Async
  public void notifyComment(Long postId, String commentWriter) {
    Post post = postService.findPost(postId);

    String postWriter = post.getUser().getLoginId();

    if(sseEmitters.containsKey(postWriter)) {
      SseEmitter sseEmitter = sseEmitters.get(postWriter);

      try {
        Map<String, String> eventData = new EventData().eventData(commentWriter);

        sseEmitter.send(SseEmitter.event().name("addComment").data(eventData));
      } catch (Exception e) {
        sseEmitters.remove(postWriter);
      }
    }
  }
}


class EventData {
  public Map<String,String> eventData(String commentWriter) {
    Map<String, String> eventData = new HashMap<>();
    eventData.put("message","댓글이 달렸습니다.");
    eventData.put("sender", commentWriter);
    return eventData;
  }
}