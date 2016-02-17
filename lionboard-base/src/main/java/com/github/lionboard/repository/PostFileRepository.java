package com.github.lionboard.repository;

import com.github.lionboard.model.PostFile;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lion.k on 16. 1. 21..
 */

@Repository
public interface PostFileRepository {
    void insertPostFile(PostFile postFile);

    List<PostFile> findFilesByPostId(int postId);

    void updateStatusByFileId(PostFile postFile);
}
