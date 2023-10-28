package com.pa.modules.news.repository;

import com.pa.modules.news.model.Likes;
import com.pa.modules.news.model.Post;
import com.pa.modules.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    Likes findByUserAndPost(Users users, Post post);

    @Query(nativeQuery = true, value = " SELECT * from likes where ( like_value  = 0 OR like_value=:likeValue ) and user_id=:userId and post_id=:postId ")
    Likes findUserAndPostAndLikeValue(@Param("postId") Long postId, @Param("userId") Long userId, @Param("likeValue") int likeValue);

    @Query(nativeQuery = true, value = "   SELECT" +
            " NVL(SUM(llike),0) 'like' ," +
            " NVL(SUM(dublelLike),0) doubleLike," +
            " NVL(SUM(disLike),0) disLike," +
            " NVL(SUM(dubleDisLike),0) doubleDislike" +
            " FROM ( " +
            "            (SELECT COUNT(*)  llike,0 dublelLike,0 disLike,0 dubleDisLike FROM likes " +
            "    WHERE  post_id= :postId  AND  like_value=1 " +
            "    GROUP BY like_value) " +
            "    UNION ALL " +
            "            (SELECT 0  llike,COUNT(*) dublelLike,0 disLike,0 dubleDisLike FROM likes " +
            "    WHERE  post_id= :postId  AND  like_value =2 " +
            "    GROUP BY like_value) " +
            "    UNION ALL " +
            "            (SELECT 0  llike,0 dublelLike,COUNT(*) disLike,0 dubleDisLike FROM likes " +
            "    WHERE  post_id= :postId AND  like_value=-1 " +
            "    GROUP BY like_value) " +
            "    UNION ALL " +
            "            (SELECT 0  llike,0 dublelLike,0 disLike,COUNT(*) dubleDisLike FROM likes " +
            "    WHERE  post_id= :postId  AND  like_value=-2 " +
            "    GROUP BY like_value) " +
            "            )a "
    )
     LikeDisLikeDTO  countOfLikesDislikes(@Param("postId") Long postId);

}
