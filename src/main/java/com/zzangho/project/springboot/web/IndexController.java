package com.zzangho.project.springboot.web;

import com.zzangho.project.springboot.config.auth.LoginUser;
import com.zzangho.project.springboot.config.auth.dto.SessionUser;
import com.zzangho.project.springboot.service.posts.PostsService;
import com.zzangho.project.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession  httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());
        System.out.println("index page!");
        if ( user != null ) {
            System.out.println(user.getName());
            model.addAttribute("name", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }

    @GetMapping("/user/login")
    public String login() {
        System.out.println("login page!");
        return "login-page";
    }
}
