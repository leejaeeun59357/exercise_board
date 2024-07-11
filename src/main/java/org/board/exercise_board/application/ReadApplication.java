package org.board.exercise_board.application;

import lombok.RequiredArgsConstructor;
import org.board.exercise_board.exception.CustomException;
import org.board.exercise_board.exception.ErrorCode;
import org.board.exercise_board.post.domain.Dto.PostDto;
import org.board.exercise_board.post.domain.Dto.PostOneDto;
import org.board.exercise_board.post.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadApplication {
    private final PostService postService;

    public List<PostDto> readPosts(Pageable pageable) {
        return postService.readPosts(pageable).stream().map(
                PostDto::entityToDto).collect(Collectors.toList());
    }

    public PostOneDto readOnePost(Long postId) {
        return postService.readOnePost(postId);
    }

    @Transactional
    public List<PostDto> searchPost(String keyword, Pageable pageable) {
        // 검색 키워드가 null 일 때
        if(Objects.equals(keyword, "") || keyword == null) {
            throw new CustomException(ErrorCode.KEYWORD_IS_EMPTY);
        }

        return postService.searchPost(keyword, pageable);
    }
}
