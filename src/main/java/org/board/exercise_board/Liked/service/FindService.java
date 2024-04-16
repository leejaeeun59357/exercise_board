package org.board.exercise_board.Liked.service;

import org.board.exercise_board.Comment.service.CommentService;
import org.board.exercise_board.Liked.domain.model.Type;
import org.board.exercise_board.Post.service.PostService;

public class FindService implements FindSomething {

  public Object object;

  public FindService(Type type, Long id) {
    if(type == Type.POST) {
      object = new PostService().find(id);
    } else if(type == Type.COMMENT) {
      object = new CommentService().find(id);
    }
  }

  @Override
  public Object find(Long id) {
    return null;
  }
}
