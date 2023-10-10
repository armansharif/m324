package com.pa.modules.news.model;

import com.pa.modules.user.model.Users;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Post post;

    private short likeCount;

}
