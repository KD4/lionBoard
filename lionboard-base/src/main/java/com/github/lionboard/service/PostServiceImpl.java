package com.github.lionboard.service;

import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.model.Post;
import com.github.lionboard.model.PostFile;
import com.github.lionboard.model.PostReport;
import com.github.lionboard.repository.PostFileRepository;
import com.github.lionboard.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lion.k on 16. 2. 3..
 */

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostFileRepository postFileRepository;

    /**
     * offset부터 limit만큼의 Post 리스트를 반환합니다.
     *
     * @param offset
     * @param limit
     * @param sort
     * @return List<Post>
     */
    @Override
    public List<Post> getPosts(int offset, int limit, String sort) {

        Map<String, Object> pageArgs = new HashMap<>();
        pageArgs.put("offset", offset);
        pageArgs.put("limit", limit);
        pageArgs.put("sort", sort);
        return postRepository.findByPage(pageArgs);
    }

    /**
     * root 게시글을 등록합니다.
     *
     * @param post
     */
    @Override
    public void insertRootPost(Post post) {
        postRepository.insertPost(post);
        postRepository.insertPostStatus(post);
    }

    /**
     * 답글을 등록합니다.
     *
     * @param post
     */
    @Override
    public void insertReplyPost(Post post) {
        if ((post.getPostNum() - 1) % 1000 < 2) {
            throw new InvalidPostException("because the number of reply exceed limit, you can't write the reply.");
        }
        // 답글의 PostNum(부모글의 -1)와 부모글의 이전글 사이의 글들의 PostNum값을 -1 해서 답글이 들어갈 자리를 만듦.
        Map<String, Integer> range = new HashMap<>();
        range.put("upperLimit", post.getPostNum());
        int lowerLimit = (post.getPostNum() - 1) / 1000 * 1000 + 1;
        range.put("lowerLimit", lowerLimit);
        postRepository.updatePostNumForInsertRow(range);

        postRepository.insertPost(post);
        postRepository.insertPostStatus(post);
    }


    /**
     * DB에 등록된 모든 게시글을 Delete 합니다.
     */
    @Override
    public void hardDeleteAllPosts() {
        postRepository.deleteAll();
    }

    /**
     * postId로 특정 Post의 정보를 반환합니다.
     *
     * @param postId
     * @return Post
     */
    @Override
    public Post getPostByPostId(int postId) {
        Post selectedPost = postRepository.findPostByPostId(postId);
        return selectedPost;
    }

    /**
     * postId에 해당하는 Post의 postStatus를 변경합니다.
     *
     * @param postId
     * @param postStatus
     */
    @Override
    public void updatePostStatusByPostId(int postId, String postStatus) {
        Post post = new Post();
        post.setPostId(postId);
        post.setPostStatus(postStatus);
        postRepository.updatePostStatus(post);
    }

    /**
     * PostFile 모델을 POST_FIlES_TB에 삽입합니다.
     *
     * @param postFile
     */
    @Override
    public void addPostFile(PostFile postFile) {
        postFileRepository.insertPostFile(postFile);

    }

    /**
     * 특정 Post의 첨부파일 목록을 반환합니다.
     *
     * @param postId
     * @return List<PostFile>
     */
    @Override
    public List<PostFile> getPostFilesByPostId(int postId) {
        return postFileRepository.findFilesByPostId(postId);
    }

    /**
     * 특정 Post 정보를 변경합니다.
     *
     * @param post
     * @return List<PostFile>
     */
    @Override
    public void modifyPost(Post post) {
        postRepository.updatePost(post);
    }


    /**
     * 특정 Post의 피신고 횟수를 반환합니다.
     * -for test case-
     *
     * @param postId
     * @return List<PostFile>
     */
    @Override
    public int getReportCount(int postId) {
        return postRepository.getReportCount(postId);
    }


    /**
     * 특정 Post에 대한 신고 정보를 PostReport 테이블에 저장합니다.
     *
     * @param postReport
     */
    @Override
    public void reportPost(PostReport postReport) {
        postRepository.insertReport(postReport);
    }

    /**
     * 특정 Post에 대한 신고 정보를 반환합니다.
     *
     * @param postId
     */
    @Override
    public List<PostReport> getReportsByPostId(int postId) {
        return postRepository.findReportByPostId(postId);
    }

    /**
     * 신고 처리 상태를 변경합니다.
     *
     * @param postReport
     */
    @Override
    public void changeProcessStatusAboutReport(PostReport postReport) {
        postRepository.updateProcessStatus(postReport);
    }

    /**
     * 특정 사용자가 작성한 게시글 목록을 반환합니다.
     *
     * @param userId
     */
    @Override
    public List<Post> getPostsByUserId(int userId) {
        return postRepository.findPostsByUserId(userId);
    }


    /**
     * 'S'상태의 게시글 숫자를 반환합니다.
     */
    @Override
    public int countPosts() {
        return postRepository.countPost();
    }

    /**
     * 특정 Post의 comment count를 1 증가시킵니다.
     *
     * @param postId
     */
    @Override
    public void addCmtCount(int postId) {
        postRepository.addCmtCount(postId);
    }

    /**
     * 특정 Post File의 Status를 변경합니다.
     *
     * @param fileId
     * @param fileStatus
     */
    @Override
    public void updateFileStatusByFileId(int fileId, String fileStatus) {
        PostFile postFile = new PostFile();
        postFile.setFileId(fileId);
        postFile.setFileStatus(fileStatus);
        postFileRepository.updateStatusByFileId(postFile);

    }

    /**
     * 특정 Post의 좋아요 수를 반환합니다.
     *
     * @param postId
     */
    @Override
    public Integer getLikeCount(int postId) {
        return postRepository.getLikeCount(postId);
    }

    /**
     * 특정 Post의 좋아요 수를 하나 올립니다.
     *
     * @param postId
     */
    @Override
    public int addLikeCount(int postId) {
        return postRepository.addLikeCount(postId);
    }

    /**
     * 특정 Post의 좋아요 수를 하나 내립니다.
     *
     * @param postId
     */
    @Override
    public int subtractLikeCount(int postId) {
        return postRepository.subtractLikeCount(postId);
    }

    /**
     * 특정 Post의 싫어요 수를 반환합니다.
     *
     * @param postId
     */
    @Override
    public Integer getHateCount(int postId) {
        return postRepository.getHateCount(postId);
    }

    /**
     * 특정 Post의 싫어요 수를 하나 올립니다.
     *
     * @param postId
     */
    @Override
    public int addHateCount(int postId) {
        return postRepository.addHateCount(postId);
    }

    /**
     * 특정 Post의 싫어요 수를 하나 내립니다.
     *
     * @param postId
     */
    @Override
    public int subtractHateCount(int postId) {
        return postRepository.subtractHateCount(postId);
    }

    /**
     * 특정 Post의 조회수 수를 반환합니다.
     *
     * @param postId
     */
    @Override
    public Integer getViewCount(int postId) {
        return postRepository.getViewCount(postId);
    }

    /**
     * 특정 Post의 조회수 수를 하나 올립니다.
     *
     * @param postId
     */
    @Override
    public int addViewCount(int postId) {
        return postRepository.addViewCount(postId);
    }

    /**
     * 특정 Post의 조회수 수를 하나 내립니다.
     *
     * @param postId
     */
    @Override
    public int subtractViewCount(int postId) {
        return postRepository.subtractViewCount(postId);
    }


    /**
     * 특정 Post의 부모글을 찾습니다.
     *
     * @param postId
     */
    @Override
    public Post getParentPost(int postId) {

        Post currentPost = postRepository.findPostByPostId(postId);
        int parentDepth = currentPost.getDepth() - 1;
        Post tempParent = new Post();
        tempParent.setDepth(parentDepth);
        tempParent.setPostNum(currentPost.getPostNum());
        Post parentPost = postRepository.findParentPost(tempParent);

        return parentPost;
    }


    /**
     * 고정 게시물은 최신순으로 unit 가져옵니다.
     *
     * @param unit
     */
    @Override
    public List<Post> getStickyPosts(int unit) {
        return postRepository.findStickyPosts(unit);
    }


    /**
     * 모든 게시글 목록을 반환합니다.
     *
     * @param offset
     * @param limit
     * @param sort
     */
    @Override
    public List<Post> getAllPosts(int offset, int limit, String sort) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("limit", limit);
        params.put("sort", sort);

        return postRepository.findAll(params);
    }


    /**
     * 모든 게시글 갯수를 반환합니다.
     */
    @Override
    public int countAllPosts() {
        return postRepository.countAllPosts();
    }


    /**
     * query를 이용해서 게시글를 검색합니다.
     *
     * @param query
     */
    @Override
    public List<Post> searchPostWithQuery(String query) {
        return postRepository.getPostsByQuery(query);
    }


    /**
     * 모든 신고글 목록을 반환합니다.
     *
     * @param offset
     * @param limit
     * @param sort
     */
    @Override
    public List<PostReport> getAllReports(int offset, int limit, String sort) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("limit", limit);
        params.put("sort", sort);
        return postRepository.findAllReports(params);
    }


    /**
     * 모든 신고 횟수를 반환합니다.
     */
    @Override
    public int countReports() {
        return postRepository.countReports();
    }

    /**
     * 쿼리값을 이용해서 신고글을 검색합니다.
     */
    @Override
    public List<PostReport> searchPostReportsWithQuery(String query) {
        return postRepository.findReportByQuery(query);
    }


    /**
     * 상태 값의 상관없이 게시글 객체를 가져옵니다.
     *
     * @param postId
     */
    @Override
    public Post getPostByPostIdForAdmin(int postId) {
        return postRepository.findPostForAdmin(postId);
    }


    /**
     * 특정 게시글이 고정 게시물인지 확인합니다.
     *
     * @param postId
     */
    @Override
    public Post getStickyPost(int postId) {
        return postRepository.findStickyPost(postId);
    }

    /**
     * 특정 게시물을 고정 게시물로 추가합니다.
     *
     * @param postId
     */
    @Override
    public void setSticky(int postId) {
        postRepository.insertStickyPost(postId);
    }

    /**
     * 고정 게시물 목록에서 특정 게시물을 삭제합니다.
     *
     * @param postId
     */
    @Override
    public void setOffStickyPost(int postId) {
        postRepository.deleteStickyPost(postId);
    }


}
