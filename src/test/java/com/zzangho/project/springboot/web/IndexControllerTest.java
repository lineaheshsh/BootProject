package com.zzangho.project.springboot.web;

import com.zzangho.project.springboot.domain.posts.Posts;
import com.zzangho.project.springboot.domain.posts.PostsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IndexControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Test
    public void 메인페이지_로딩() {
        //when
        String body = this.restTemplate.getForObject("/", String.class);

        //then
        assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");

    }

    @Test
    public void 등록페이지_로딩() {
        //when
        String body = this.restTemplate.getForObject("/posts/save", String.class);

        //then
        assertThat(body).contains("게시글 등록");

    }

    @Test
    public void 수정페이지_로딩() {
        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long updateId = savedPosts.getId();

        //when
        String body = this.restTemplate.getForObject("/posts/update/" + updateId, String.class);

        //then
        assertThat(body).contains("게시글 수정");

    }
}
