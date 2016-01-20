package com.github.lionboard.repository.mybatis;
import com.github.lionboard.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Lion.k on 16. 1. 20..
 */

@Repository
public interface PostRepository {
    List<Post> findByPage(Map<String, Integer> pageArgs);
}
