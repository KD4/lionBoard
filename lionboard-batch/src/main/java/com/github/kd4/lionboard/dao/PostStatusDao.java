package com.github.kd4.lionboard.dao;


import com.github.kd4.lionboard.domain.PostStatus;

import java.util.List;

/**
 * Created by daum on 16. 2. 22..
 */
public interface PostStatusDao {
    List<PostStatus> getAll();

    void update(PostStatus postStatus);
}
