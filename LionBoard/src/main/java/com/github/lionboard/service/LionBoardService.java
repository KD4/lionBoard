package com.github.lionboard.service;

import com.github.lionboard.model.Post;

import java.util.List;

/**
 * Created by daum on 16. 1. 20..
 */
public interface LionBoardService {
    List<Post> getPosts(int offset, int limit);
}
