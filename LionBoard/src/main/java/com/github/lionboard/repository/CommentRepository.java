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

    void updateCmtNumForInsertRow(Map<String, Integer> range);

    Integer getLikeCount(int cmtId);

    int addLikeCount(int cmtId);

    int subtractLikeCount(int cmtId);

    Integer getHateCount(int cmtId);

    int addHateCount(int cmtId);

    int subtractHateCount(int cmtId);
}
