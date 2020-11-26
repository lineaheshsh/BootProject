package com.zzangho.project.springboot.web;

import com.zzangho.project.springboot.service.posts.PostsService;
import com.zzangho.project.springboot.web.dto.PostsResponseDto;
import com.zzangho.project.springboot.web.dto.PostsSaveRequestDto;
import com.zzangho.project.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {
    // @Autowired를 안쓰는 이유는 생성자로 Bean 객체를 받도록 하기 위함.
    // 롬복의 @RequiredArgsConstructor 어노테이션이 "final"이 선언된 모든 필드를 인자값으로 하는 생성자를 대신 생성해준다.
    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }

}
