package com.github.lionboard.repository;

import com.github.lionboard.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by lion.k on 16. 1. 21..
 */

@Repository
public interface CommentRepository {
    List<Comment> findCommentsByPostId(int postId);

    void insertComment(Comment comment);

    void insertCommentStatus(Comment comment);

    List<Comment> findComments();

    void deleteAll();

    void updateCmtStatusByCmtId(Map<String, Object> cmtStatusArgs);

    Comment findCommentByCmtId(int cmtId);

}
