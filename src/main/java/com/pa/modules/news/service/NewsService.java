package com.pa.modules.news.service;

import com.pa.commons.CommonUtils;
import com.pa.modules.news.model.Category;
import com.pa.modules.news.model.Likes;
import com.pa.modules.news.model.Post;
import com.pa.modules.news.repository.CategoryRepository;
import com.pa.modules.news.repository.LikeDisLikeDTO;
import com.pa.modules.news.repository.LikesRepository;
import com.pa.modules.news.repository.PostRepository;
import com.pa.modules.user.model.Users;
import com.pa.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private CategoryRepository categoryRepository;
    private PostRepository postRepository;
    private LikesRepository likesRepository;

    private UserService userService;


    @Autowired
    public NewsService(CategoryRepository categoryRepository, PostRepository postRepository, LikesRepository likesRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.likesRepository = likesRepository;
        this.userService = userService;
    }

    public List<Post> findAllPost(HttpServletRequest request, int page, int perPage, String sort) {

        Pageable postSortedAndPagination =
                PageRequest.of(page, perPage, Sort.by(sort).descending());
        Page<Post> pagedResult = this.postRepository.findAll(postSortedAndPagination);
        if (pagedResult.hasContent()) {
            List<Post> posts = pagedResult.getContent();
            Users user = userService.getUserByToken(request);
            for (Post post : posts) {
                Likes likes = likesRepository.findByUserAndPost(user, post);
                if (CommonUtils.isNotNull(likes))
                    switch (likes.getLikeValue()) {
                        case 1:
                            post.setIsLiked(1);
                            break;
                        case 2:
                            post.setIsDoubleLiked(1);
                            break;
                        case -1:
                            post.setIsDisliked(1);
                            break;
                        case -2:
                            post.setIsDoubleDisliked(1);
                            break;
                        default:
                            // code block
                    }
            }
            return posts;
        } else {
            return new ArrayList<Post>();
        }


    }

    public Optional<Post> findPost(long id) {
        return postRepository.findById(id);
    }

    public List<Category> findAllCategory() {
        return categoryRepository.findAll();
    }

    public LikeDisLikeDTO likePost(Post post, Users users, boolean isDouble) {
        Likes like = likesRepository.findByUserAndPost(users, post);
        if (CommonUtils.isNull(like)) {
            like = new Likes();
            like.setPost(post);
            like.setUser(users);
            if (isDouble)
                like.setLikeValue(2);
            else
                like.setLikeValue(1);
        } else {
            like.setPost(post);
            like.setUser(users);
            if (isDouble) {
                if (like.getLikeValue() == 2)
                    like.setLikeValue(0);
                else
                    like.setLikeValue(2);
            } else {
                if (like.getLikeValue() == 1)
                    like.setLikeValue(0);
                else
                    like.setLikeValue(1);
            }
        }
        likesRepository.save(like);
        LikeDisLikeDTO likeDisLikeDTO = likesRepository.countOfLikesDislikes(post.getId());
        post.setLikeCount(likeDisLikeDTO.getLike().intValue());
        post.setDislikeCount(likeDisLikeDTO.getDislike().intValue());
        post.setDoubleLikeCount(likeDisLikeDTO.getDoubleLike().intValue());
        post.setDoubleDislikeCount(likeDisLikeDTO.getDoubleDislike().intValue());

        postRepository.save(post);
        return likeDisLikeDTO;
    }


    public LikeDisLikeDTO disLikePost(Post post, Users users, boolean isDouble) {
//        Likes like = likesRepository.findUserAndPostAndLikeValue(users.getId(), post.getId(), -1);
        Likes like = likesRepository.findByUserAndPost(users, post);
        if (CommonUtils.isNull(like)) {
            like = new Likes();
            like.setPost(post);
            like.setUser(users);
            if (isDouble)
                like.setLikeValue(-2);
            else
                like.setLikeValue(-1);

        } else {
            like.setPost(post);
            like.setUser(users);

            if (isDouble) {
                if (like.getLikeValue() == -2)
                    like.setLikeValue(0);
                else
                    like.setLikeValue(-2);
            } else {
                if (like.getLikeValue() == -1)
                    like.setLikeValue(0);
                else
                    like.setLikeValue(-1);
            }
        }
        likesRepository.save(like);
        LikeDisLikeDTO likeDisLikeDTO = likesRepository.countOfLikesDislikes(post.getId());
        post.setLikeCount(likeDisLikeDTO.getLike().intValue());
        post.setDislikeCount(likeDisLikeDTO.getDislike().intValue());
        post.setDoubleLikeCount(likeDisLikeDTO.getDoubleLike().intValue());
        post.setDoubleDislikeCount(likeDisLikeDTO.getDoubleDislike().intValue());

        postRepository.save(post);
        return likeDisLikeDTO;
    }

}
