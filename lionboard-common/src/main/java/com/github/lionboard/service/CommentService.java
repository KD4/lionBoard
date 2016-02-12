package com.github.lionboard.service;

import com.github.lionboard.model.Comment;
import com.github.lionboard.model.CommentReport;

import java.util.List;

/**
 * Created by Lion.k on 16. 2. 3..
 */
public interface CommentService {
    void modifyComment(Comment comment);

    int getCmtReportCount(int cmtId);

    void reportComment(CommentReport commentReport);

    List<CommentReport> getCommentReports(int cmtId);

    void changeProcessStatusAboutReport(CommentReport commentReport);

    List<Comment> getCommentsByUserId(int userId);

    List<Comment> getComments();

    List<Comment> getCommentsByPostId(int postId, String sort);

    void insertRootComment(Comment comment);

    void insertReplyComment(Comment comment);

    void hardDeleteAllComments();

    void updateCmtStatusByCmtId(int cmtId, String cmtStatus);

    Comment getCommentByCmtId(int cmtId);

    void updateCmtStatusByPostId(int postId, String cmtStatus);

    Integer getLikeCount(int cmtId);

    int addLikeCount(int cmtId);

    int subtractLikeCount(int cmtId);

    Integer getHateCount(int cmtId);

    int addHateCount(int cmtId);

    int subtractHateCount(int cmtId);
}
