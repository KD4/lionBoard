package com.github.lionboard.service;

import com.github.lionboard.error.InvalidCmtException;
import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.error.UploadFileToTenthException;
import com.github.lionboard.model.*;
import com.github.lionboard.security.SecurityUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * Created by lion.k on 16. 1. 20..
 * LionBoard Service gateway.
 * 예외처리.
 */

@Service
public class LionBoardServiceImpl implements LionBoardService {


    @Autowired
    AttachmentService attachmentService;

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Override
    public List<Post> getPosts(int offset, int limit, String sort) {
        List<Post> posts = postService.getPosts(offset, limit, sort);
        return posts;
    }

    @Override
    public List<Post> getPostsByUserId(int userId) {
        try{
            List<Post> posts = postService.getPostsByUserId(userId);
            return posts;
        }catch (RuntimeException re){
//            userId 정보가 도메인에 어긋나면 예외가 발생함.
            throw new InvalidUserException("사용자가 작성한 게시글이 없거나 반환할 수 없습니다. "+re.getMessage());
        }

    }

    @Override
    public void addPost(Post post) {

        //파라미터로 받은 post의 depth를 판단해서 루트글인지, 답글인지 확인함.
        if(post.getDepth() < 1){
            postService.insertRootPost(post);
        }else{
            postService.insertReplyPost(post);
        }
    }

    @Override
    public void deleteAllPosts() {
        postService.hardDeleteAllPosts();
    }

    @Override
    public Post getPostByPostId(int postId) {
        Post selectedPost = postService.getPostByPostId(postId);
        if(selectedPost == null){
            throw new InvalidPostException();
        }
        return selectedPost;
    }

    @Override
    public void changePostStatusByPostId(int postId, String postStatus) {
        postService.updatePostStatusByPostId(postId, postStatus);
    }


    @Override
    public List<PostFile> getPostFilesByPostId(int postId) {
        List<PostFile> postFiles = postService.getPostFilesByPostId(postId);
        if(postFiles == null){
            throw new InvalidPostException();
        }
        return postFiles;
    }

    @Override
    public void modifyPost(Post post) {

        postService.modifyPost(post);

    }

    @Override
    public void changePostStatusToDelete(int postId) {

            postService.updatePostStatusByPostId(postId,"D");
            //ToDo commentService로 바꾸기
            changeCmtStatusByPostId(postId, "D");
    }

    @Override
    public int getPostReportCount(int postId) {

        return postService.getReportCount(postId);

    }

    @Override
    public void reportPost(PostReport postReport) {

        postService.reportPost(postReport);

    }

    @Override
    public List<PostReport> getPostReports(int postId) {

        return postService.getReportsByPostId(postId);

    }

    @Override
    public void changeProcessStatusFromPost(PostReport postReport) {

        postService.changeProcessStatusAboutReport(postReport);

    }

    @Override
    public void modifyComment(Comment comment) {
        //댓글 수정하는 기능은 테스트에서만 사용됨.

        commentService.modifyComment(comment);

    }

    @Override
    public int getCmtReportCount(int cmtId) {

        return commentService.getCmtReportCount(cmtId);

    }


    @Override
    public void reportComment(CommentReport commentReport) {

        commentService.reportComment(commentReport);

    }

    @Override
    public List<CommentReport> getCommentReports(int cmtId) {

        return commentService.getCommentReports(cmtId);

    }

    @Override
    public void changeProcessStatusFromCmt(CommentReport commentReport) {

        commentService.changeProcessStatusAboutReport(commentReport);

    }

    @Override
    public List<Pagination> getPagination(int offset,String sort, String source) {
//        offset param을 이용해서 현재 페이지 넘버를 계산합니다.
        int currentPage = offset/15 + 1;

//        페이징은 5 페이지씩 그룹핑을 합니다.이전 그룹 페이지 번호를 담는 변수를 선언.
        int previousPage;

        // 현재 페이지가 5보다 크면 이전 페이지를 계산해야하고, 그렇지 않다면 이전 페이지는 첫번째 페이지가 됩니다.
        if(currentPage > 5) {
            previousPage = currentPage / 5 * 5;
        }else {
            previousPage = 1;
        }

        // 다음 그룹 페이지 페이지 번호를 계산해서 olderpage 변수에 담습니다.
        int olderPage = previousPage + 5;

        // 현재 게시물을 표시하는 페이지가 총 5개가 안된다면, maxPage가 최종페이지가 됩니다.
        int maxPage = 0;
        if(source.equals("posts")){
            maxPage = postService.countPosts() / 15 + 1;
        }else if(source.equals("adminUsers")){
            maxPage = userService.countUsers() / 15 + 1;
        }else if(source.equals("adminPosts")){
            maxPage = postService.countAllPosts() / 15 + 1;
        }else if(source.equals("adminComments")){
            maxPage = commentService.countAllComments() / 15 + 1;
        }else if(source.equals("adminPostReports")){
            maxPage = postService.countReports() / 15 + 1;
        }


        if(maxPage<olderPage){
            olderPage = maxPage;
        }

        //페이징 범위(prev - older)까지를 정하고 각 페이징 넘버와 offset을 pagination 모델에 담습니다.
        List<Pagination> paginations = new ArrayList<Pagination>();
        for(int i = previousPage;i<=olderPage;i++){
            Pagination pagination = new Pagination();
            pagination.setPage(i);
            pagination.setOffset((i - 1) * 15);
            pagination.setSort(sort);
            if(i==currentPage){
                pagination.setIsCurrent(true);
            }else{
                pagination.setIsCurrent(false);
            }
            paginations.add(pagination);
        }
        return paginations;
    }



    @Override
    public List<Comment> getCommentsByUserId(int userId) {

        return commentService.getCommentsByUserId(userId);

    }



    @Override
    public List<Comment> getComments() {
        return commentService.getComments();
    }

    @Override
    public List<Comment> getCommentsByPostId(int postId, String sort) {

        return commentService.getCommentsByPostId(postId, sort);

    }


    @Override
    public void addComment(Comment comment) {
        //없는 게시글에 코멘트를 달려고 할 때, 예외처리.
        if(postService.getPostByPostId(comment.getPostId()) == null){
            throw new InvalidPostException("등록하고자 하는 Comment의 Post 정보가 올바르지 않습니다. 서버로그를 확인해주세요.");
        }


        if (comment.getDepth() < 1) {
            commentService.insertRootComment(comment);
        } else {
            commentService.insertReplyComment(comment);
        }


        postService.addCmtCount(comment.getPostId());

    }

    @Override
    public void deleteAllComments() {
        commentService.hardDeleteAllComments();

    }

    @Override
    public void changeCmtStatusByCmtId(int cmtId, String cmtStatus) {
        commentService.updateCmtStatusByCmtId(cmtId, cmtStatus);
    }

    @Override
    public Comment getCommentByCmtId(int cmtId) {

        Comment selectedCmt = commentService.getCommentByCmtId(cmtId);
        if (selectedCmt == null) {
            throw new InvalidCmtException(cmtId+"에 해당하는 덧글이 존재하지 않습니다.");
        }
        return selectedCmt;

    }


    @Override
    public void deleteAllUsers() {
        userService.hardDeleteAllUsers();
    }

    @Override
    public void addUser(User user){

        if (user.getIsOAuth().equals("F")) {
            userService.insertNormalUser(user);
        } else {
            userService.insertOAuthUser(user);
        }
    }

    @Override
    public User getUserByUserId(int userId) {
        User selectedUser = userService.getUserByUserId(userId);
        if(selectedUser == null){
            throw new InvalidUserException("등록된 유저 정보가 없거나 탈퇴한 회원입니다.");
        }
        return selectedUser;

    }

    @Override
    public User getUserByIdentity(String identity) {
        User selectedUser = userService.getUserByIdentity(identity);
        if(selectedUser == null){
            throw new InvalidUserException("등록된 유저 정보가 없거나 탈퇴한 회원입니다.");
        }
        return selectedUser;
    }

    @Override
    public User getUserByName(String name) {
        User selectedUser = userService.getUserByName(name);
        if(selectedUser == null){
            throw new InvalidUserException();
        }
        return selectedUser;
    }

    @Override
    public String uploadProfile(int userId, MultipartFile uploadFile) throws Exception {

        //프로필 작명 규칙 : lionboard_profile_{userId}.jpg
        DateTime dateTime = DateTime.now();
        ;
        String fileName = "lionboard_profile_"+dateTime.getMillis()+"_"+String.valueOf(userId)+".jpg";

        return attachmentService.uploadFile(uploadFile.getInputStream(), fileName);

    }


    @Override
    public void updateProfileInfoOnUser(int userId, String uploadedUrl) {

        userService.updateUserProfile(userId, uploadedUrl);

    }

    //첨부파일을 등록하는 로직. insertFileOnTenthServer메소드를 이용함.
    @Override
    public String addFileToServer(int postId, MultipartFile uploadFile) throws Exception {


            //첨부파일 작명 규칙 : lionboard_post_File_{postId}_{originalFileName}
            String fileName = "lionboard_post_File_"+postId+"_"+uploadFile.getOriginalFilename();
            return attachmentService.uploadFile(uploadFile.getInputStream(), fileName);


    }


    @Override
    public void addPostWithFile(Post post) throws Exception {
        //Post 기본 정보 등록
        addPost(post);
        // 등록된 Post Id 반환.
        int postId = post.getPostId();

        PostFile postFile = new PostFile();
        //Id와 파일 정보를 이용해서 서버에 파일 업로드 후, 파일 정보를 디비에 저장.
        String uploadUrl = addFileToServer(post.getPostId(), post.getUploadFile());
        postFile.setPostId(postId);
        postFile.setFileUrl(uploadUrl);
        postFile.setFileName(post.getUploadFile().getOriginalFilename());

        postService.addPostFile(postFile);
    }


    @Override
    public void addFileOnPost(Post post) throws Exception {
        //게시글 수정 화면에서 파일만 업로드할 때, 수행되는 로직.
        PostFile postFile = new PostFile();
        //Id와 파일 정보를 이용해서 서버에 파일 업로드 후, 파일 정보를 디비에 저장.
        String uploadUrl = addFileToServer(post.getPostId(), post.getUploadFile());
        postFile.setPostId(post.getPostId());
        postFile.setFileUrl(uploadUrl);
        postFile.setFileName(post.getUploadFile().getOriginalFilename());
        postService.addPostFile(postFile);
    }

    //특정 포스트의 상태가 변경되면 해당 포스트의 코멘트도 상태가 같이 변경됨.
    @Override
    public void changeCmtStatusByPostId(int postId, String cmtStatus) {
        commentService.updateCmtStatusByPostId(postId, cmtStatus);
    }

    @Override
    public Post createBasePostForReply(int postId) {
        Post selectedPost = postService.getPostByPostId(postId);
        if(selectedPost == null){
            throw new InvalidPostException();
        }
        //답글은 부모글의 depth + 1에 해당하는 depth를 가짐.
        selectedPost.setDepth(selectedPost.getDepth() + 1);
        //답글은 부모글의 PostNum - 1에 해당하는 PostNum를 가짐.
        selectedPost.setPostNum(selectedPost.getPostNum() - 1);

        return selectedPost;
    }

    @Override
    public void changeFileStatusToDelete(int fileId) {

        postService.updateFileStatusByFileId(fileId, "D");

    }

    @Override
    public void addPostFile(PostFile postFile) {
        postService.addPostFile(postFile);
    }

    @Override
    public User existUserByIdentity(String identity) {
        return userService.getUserByIdentity(identity);
    }

    @Override
    public void securityLogin(User user) {
        SecurityUtil.logInUser(user);
    }

    @Override
    public Post getParentPost(int postId) {
        Post parentPost = postService.getParentPost(postId);
        if(!parentPost.getPostStatus().equals("S")){
            throw new InvalidPostException("부모글은 삭제되었거나 숨김 처리되었습니다.");
        }
        return postService.getParentPost(postId);
    }

    @Override
    public List<Post> getStickyPosts(int unit) {
        return postService.getStickyPosts(unit);
    }

    @Override
    public List<User> getAllUsers(int offset, int limit, String sort) {
        return userService.getAllUsers(offset, limit, sort);
    }

    @Override
    public void modifyUserStatus(User user) {
        userService.updateUserStatusByUserId(user.getId(),user.getUserStatus());
    }

    @Override
    public void modifyUserRole(User user) {
        userService.updateUserRole(user);
    }

    @Override
    public List<User> searchUserWithQuery(String query) {

        if(isInteger(query)){
            return userService.searchUserWithQuery(query);
        }else{
            query = "%"+query+"%";
            return userService.searchUserWithQuery(query);
        }

    }

    @Override
    public List<Post> getAllPosts(int offset, int limit, String sort) {
        return postService.getAllPosts(offset, limit, sort);
    }

    @Override
    public List<Post> searchPostWithQuery(String query) {

        if(isInteger(query)){
            return postService.searchPostWithQuery(query);
        }else{
            query = "%"+query+"%";
            return postService.searchPostWithQuery(query);
        }

    }

    @Override
    public List<Comment> getAllComments(int offset, int limit, String sort) {

        return commentService.getAllComments(offset, limit, sort);
    }

    @Override
    public List<Comment> searchCmtWithQuery(String query) {
        if(isInteger(query)){
            return commentService.searchCmtWithQuery(query);
        }else{
            query = "%"+query+"%";
            return commentService.searchCmtWithQuery(query);
        }

    }

    @Override
    public List<PostReport> getAllPostReports(int offset, int limit, String sort) {
        return postService.getAllReports(offset, limit, sort);
    }

    @Override
    public List<PostReport> searchPostReportsWithQuery(String query) {
        if(isInteger(query)){
            return postService.searchPostReportsWithQuery(query);
        }else{
            query = "%"+query+"%";
            return postService.searchPostReportsWithQuery(query);
        }
    }

    @Override
    public List<CommentReport> getAllCmtReports(int offset, int limit, String sort) {
        return commentService.getAllReports(offset,limit,sort);
    }

    @Override
    public List<CommentReport> searchCmtReportsWithQuery(String query) {
        if(isInteger(query)){
            return commentService.searchReportWithQuery(query);
        }else{
            query = "%"+query+"%";
            return commentService.searchReportWithQuery(query);
        }

    }

    @Override
    public Post getPostByPostIdForAdmin(int postId) {
        return postService.getPostByPostIdForAdmin(postId);
    }

    @Override
    public Post getStickyPost(int postId) {
        return postService.getStickyPost(postId);
    }

    @Override
    public void setStickyPost(int postId) {
        postService.setSticky(postId);
    }

    @Override
    public void setOffStickyPost(int postId) {
        postService.setOffStickyPost(postId);
    }


    @Override
    public void modifyUser(User user) {

        userService.updateUser(user);

    }

    @Override
    public void removeUserById(int userId) {

        userService.hardDeleteUserById(userId);

    }

    @Override
    public void changeUserStatusToLeave(int userId) {
        userService.updateUserStatusByUserId(userId, "L");

    }




    @Override
    public int getPostLike(int postId) {
        Integer likeCount = postService.getLikeCount(postId);
        if(likeCount == null){
            throw new InvalidPostException();
        }
        return likeCount;
    }

    @Override
    public void addPostLike(int postId) {
        int rows = postService.addLikeCount(postId);

        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public void subtractPostLike(int postId) {
        int rows = postService.subtractLikeCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public int getPostHate(int postId) {
        Integer hateCount = postService.getHateCount(postId);
        if(hateCount == null){
            throw new InvalidPostException();
        }
        return hateCount;
    }

    @Override
    public void addPostHate(int postId) {
        int rows = postService.addHateCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public void subtractPostHate(int postId) {
        int rows = postService.subtractHateCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public int getPostView(int postId) {
        Integer hateCount = postService.getViewCount(postId);
        if(hateCount == null){
            throw new InvalidPostException();
        }
        return hateCount;
    }

    @Override
    public void addPostView(int postId) {
        int rows = postService.addViewCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public void subtractPostView(int postId) {
        int rows = postService.subtractViewCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public int getCmtLike(int cmtId) {
        Integer likeCount = commentService.getLikeCount(cmtId);
        if(likeCount == null){
            throw new InvalidCmtException();
        }
        return likeCount;
    }

    @Override
    public void addCmtLike(int cmtId) {
        int rows = commentService.addLikeCount(cmtId);
        if(rows == 0){
            throw new InvalidCmtException();
        }
    }

    @Override
    public void subtractCmtLike(int cmtId) {
        int rows = commentService.subtractLikeCount(cmtId);
        if(rows == 0){
            throw new InvalidCmtException();
        }
    }

    @Override
    public int getCmtHate(int cmtId) {
        Integer likeCount = commentService.getHateCount(cmtId);
        if(likeCount == null){
            throw new InvalidCmtException();
        }
        return likeCount;
    }

    @Override
    public void addCmtHate(int cmtId) {
        int rows = commentService.addHateCount(cmtId);
        if(rows == 0){
            throw new InvalidCmtException();
        }
    }

    @Override
    public void subtractCmtHate(int cmtId) {
        int rows = commentService.subtractHateCount(cmtId);
        if(rows == 0){
            throw new InvalidCmtException();
        }
    }


    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}
