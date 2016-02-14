package com.github.lionboard.service;

import com.github.lionboard.model.Comment;
import com.github.lionboard.model.CommentReport;
import com.github.lionboard.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lion.k on 16. 2. 3..
 */

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;


    /**
     * 특정 Comment를 수정합니다.
     * -for test case-
     * @param comment
     */
    @Override
    public void modifyComment(Comment comment) {
        commentRepository.updateComment(comment);
    }

    /**
     * 특정 Comment가 신고당한 횟수를 반환합니다.
     * -for test case-
     * @param cmtId
     */
    @Override
    public int getCmtReportCount(int cmtId) {
        return commentRepository.getReportCount(cmtId);
    }

    /**
     * 특정 Comment에 대한 신고 정보를 PostReport 테이블에 저장합니다.
     *
     * @param commentReport
     */
    @Override
    public void reportComment(CommentReport commentReport) {
        commentRepository.insertReport(commentReport);
    }

    /**
     * 특정 Comment에 대한 신고 정보를 반환합니다.
     *
     * @param cmtId
     */
    @Override
    public List<CommentReport> getCommentReports(int cmtId) {
        return commentRepository.findReportByCmtId(cmtId);
    }

    /**
     * 특정 Comment에 대한 신고 처리 상태를 변경합니다.
     *
     * @param commentReport
     */
    @Override
    public void changeProcessStatusAboutReport(CommentReport commentReport) {
        commentRepository.updateProcessStatus(commentReport);
    }

    /**
     * 특정 사용자가 작성한 comment 목록을 반환합니다.
     *
     * @param userId
     * @return  List<Comment>
     */
    @Override
    public List<Comment> getCommentsByUserId(int userId) {
        return commentRepository.findCommentsByUserId(userId);
    }


    /**
     * comment 목록을 전부 반환합니다.
     * - for test case -
     * @return  List<Comment>
     */
    @Override
    public List<Comment> getComments() {
        return commentRepository.findComments();
    }

    /**
     * 특정 Post의 Comment 목록을 반환합니다.
     *
     * @param postId
     * @param sort
     * @return List<Comment>
     */
    @Override
    public List<Comment> getCommentsByPostId(int postId, String sort) {
        Map<String,Object> cmtParam = new HashMap<>();
        cmtParam.put("postId",postId);
        cmtParam.put("sort",sort);
        return commentRepository.findCommentsByPostId(cmtParam);
    }

    /**
     * Root Comment를 추가합니다.
     *
     * @param comment
     * @return  List<Comment>
     */
    @Override
    public void insertRootComment(Comment comment) {
        commentRepository.insertComment(comment);
        commentRepository.insertCommentStatus(comment);
    }

    /**
     * Reply Post를 추가합니다.
     *
     * @param comment
     * @return  List<Comment>
     */
    @Override
    public void insertReplyComment(Comment comment) {
        Map<String,Integer> range = new HashMap<>();
        range.put("upperLimit",comment.getCmtNum());
        int lowerLimit = (comment.getCmtNum()) / 1000 * 1000 +1;
        range.put("lowerLimit", lowerLimit);
        commentRepository.updateCmtNumForInsertRow(range);
        commentRepository.insertComment(comment);
        commentRepository.insertCommentStatus(comment);
    }

    /**
     * 모든 Comment를 delete 합니다.
     * -for test case-
     */
    @Override
    public void hardDeleteAllComments() {
        commentRepository.deleteAll();
    }

    /**
     * 특정 Comment의 상태를 변경합니다.
     *
     * @param cmtId
     * @param cmtStatus
     */
    @Override
    public void updateCmtStatusByCmtId(int cmtId, String cmtStatus) {
        Comment comment = new Comment();
        comment.setCmtId(cmtId);
        comment.setCmtStatus(cmtStatus);
        commentRepository.updateCmtStatusByCmtId(comment);
    }

    /**
     * Cmt Id에 해당하는 Comment를 반환합니다.
     * -for test case-
     * @param cmtId
     * @return Comment
     */
    @Override
    public Comment getCommentByCmtId(int cmtId) {
        return commentRepository.findCommentByCmtId(cmtId);
    }


    /**
     * 특정 Post의 Comment 목록들 Status를 변경합니다.
     *
     * @param postId
     * @param cmtStatus
     */
    @Override
    public void updateCmtStatusByPostId(int postId, String cmtStatus) {

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setCmtStatus(cmtStatus);
        commentRepository.updateCmtStatusByPostId(comment);
    }

    /**
     * 특정 Comment의 좋아요 수를 반환합니다.
     *
     * @param cmtId
     */
    @Override
    public Integer getLikeCount(int cmtId) {
        return commentRepository.getLikeCount(cmtId);
    }

    /**
     * 특정 Comment의 좋아요 수를 하나 올립니다.
     *
     * @param cmtId
     */
    @Override
    public int addLikeCount(int cmtId) {
        return commentRepository.addLikeCount(cmtId);
    }

    /**
     * 특정 Comment의 좋아요 수를 하나 내립니다.
     *
     * @param cmtId
     */
    @Override
    public int subtractLikeCount(int cmtId) {
        return commentRepository.subtractLikeCount(cmtId);
    }

    /**
     * 특정 Comment의 싫어요 수를 반환합니다..
     *
     * @param cmtId
     */
    @Override
    public Integer getHateCount(int cmtId) {
        return commentRepository.getHateCount(cmtId);
    }

    /**
     * 특정 Comment의 싫어요 수를 하나 올립니다.
     *
     * @param cmtId
     */
    @Override
    public int addHateCount(int cmtId) {
        return commentRepository.addHateCount(cmtId);
    }

    /**
     * 특정 Comment의 싫어요 수를 하나 내립니다.
     *
     * @param cmtId
     */
    @Override
    public int subtractHateCount(int cmtId) {
        return commentRepository.subtractHateCount(cmtId);
    }


    /**
     * 모든 댓글 목록을 반환합니다.
     *
     * @param offset
     * @param limit
     * @param sort
     */
    @Override
    public List<Comment> getAllComments(int offset, int limit, String sort) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("offset",offset);
        params.put("limit",limit);
        params.put("sort", sort);

        return commentRepository.findAll(params);

    }


    /**
     * 모든 댓글 개수를 반환합니다.
     *
     */
    @Override
    public int countAllComments() {
        return commentRepository.countAll();
    }

    /**
     * 쿼리를 이용해서 검색 후 댓글 목록을 반환합니다.
     *
     * @param query
     */
    @Override
    public List<Comment> searchCmtWithQuery(String query) {
        return commentRepository.findCommentsByQuery(query);
    }

    /**
     * 모든 댓글 목록을 반환합니다.
     *
     * @param offset
     * @param limit
     * @param sort
     */
    @Override
    public List<CommentReport> getAllReports(int offset, int limit, String sort) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("offset",offset);
        params.put("limit",limit);
        params.put("sort", sort);

        return commentRepository.findAllReports(params);
    }

    @Override
    public List<CommentReport> searchReportWithQuery(String query) {
        return commentRepository.findReportByQuery(query);
    }
}
