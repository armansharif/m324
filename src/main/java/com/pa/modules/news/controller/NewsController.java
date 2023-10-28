package com.pa.modules.news.controller;


import com.pa.commons.CommonUtils;
import com.pa.commons.Routes;

import com.pa.modules.news.model.Category;
import com.pa.modules.news.model.Post;
import com.pa.modules.news.service.NewsService;
import com.pa.modules.user.model.Users;
import com.pa.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(produces = "application/json")
public class NewsController {

    private NewsService newsService;
    private UserService userService;

    @Autowired
    public NewsController(NewsService newsService, UserService userService) {
        this.newsService = newsService;
        this.userService = userService;
    }


    @GetMapping(value = {Routes.GET_news_post})
    public List<Post> getAllPosts(HttpServletResponse response,HttpServletRequest request,
                                  @RequestParam(required = false, defaultValue = "id") String sort,
                                  @RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = "10") int perPage) {
        return newsService.findAllPost(  request,page, perPage, sort);
    }

    @GetMapping(value = {Routes.GET_news_category})
    public List<Category> getAllCategories(HttpServletResponse response) {
        return newsService.findAllCategory();
    }

    @PostMapping(value = {Routes.POST_news_post_like})
    public ResponseEntity<Object> likePost(HttpServletResponse response, HttpServletRequest request,
                                           @PathVariable(required = false) long postId) {
        Post post = newsService.findPost(postId).orElse(null);
        Users users = userService.getUserByToken(request);
        if (CommonUtils.isNull(post) || CommonUtils.isNull(post))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok()
                .body(newsService.likePost(post, users,false) );
    }


    @PostMapping(value = {Routes.POST_news_post_dislike})
    public ResponseEntity<Object> dislikePost(HttpServletResponse response, HttpServletRequest request,
                                           @PathVariable(required = false) long postId) {
        Post post = newsService.findPost(postId).orElse(null);
        Users users = userService.getUserByToken(request);
        if (CommonUtils.isNull(post) || CommonUtils.isNull(post))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok()
                .body(newsService.disLikePost(post, users,false) );
    }


    @PostMapping(value = {Routes.POST_news_post_double_like})
    public ResponseEntity<Object> doubleLikePost(HttpServletResponse response, HttpServletRequest request,
                                           @PathVariable(required = false) long postId) {
        Post post = newsService.findPost(postId).orElse(null);
        Users users = userService.getUserByToken(request);
        if (CommonUtils.isNull(post) || CommonUtils.isNull(post))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok()
                .body(newsService.likePost(post, users,true) );
    }

    @PostMapping(value = {Routes.POST_news_post_double_dislike})
    public ResponseEntity<Object> doubleDislikePost(HttpServletResponse response, HttpServletRequest request,
                                              @PathVariable(required = false) long postId) {
        Post post = newsService.findPost(postId).orElse(null);
        Users users = userService.getUserByToken(request);
        if (CommonUtils.isNull(post) || CommonUtils.isNull(post))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok()
                .body(newsService.disLikePost(post, users,true) );
    }
}
