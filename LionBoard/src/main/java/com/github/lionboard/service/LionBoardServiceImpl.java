package com.github.lionboard.service;

import com.github.lionboard.error.InvalidCmtException;
import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.error.UploadFileToTenthException;
import com.github.lionboard.model.*;
import com.github.lionboard.repository.CommentRepository;
import com.github.lionboard.repository.PostFileRepository;
import com.github.lionboard.repository.PostRepository;
import com.github.lionboard.repository.UserRepository;
import com.github.lionboard.tenth2.ImageFileUploadForTenth2;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Created by lion.k on 16. 1. 20..
 */

@Service
public class LionBoardServiceImpl implements LionBoardService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostFileRepository postFileRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    ImageFileUploadForTenth2 imageFileUploadForTenth2;

    @Override
    public List<Post> getPosts(int offset, int limit) {
        Map<String,Integer> pageArgs = new HashMap<>();
        pageArgs.put("offset",offset);
        pageArgs.put("limit",limit);
        List<Post> posts = postRepository.findByPage(pageArgs);

        //ToDo: Post 객체에 이름을 어떻게 넣을 것인가 ? 일단은 SQL 문으로 !

        return posts;
    }

    @Override
    public void addPost(Post post) {
        try{
            if(post.getDepth() < 1){
                postRepository.insertPost(post);
                postRepository.insertPostStatus(post);
            }else{
                if((post.getPostNum()-1) % 1000 < 2){
                    throw new InvalidPostException("because the number of reply exceed limit, you can't write the reply.");
                }

                // 답글의 PostNum(부모글의 -1)와 부모글의 이전글 사이의 글들의 PostNum값을 -1 해서 답글이 들어갈 자리를 만듦.
                Map<String,Integer> range = new HashMap<>();
                range.put("upperLimit",post.getPostNum());
                int lowerLimit = (post.getPostNum()-1) / 1000 * 1000 + 1;
                range.put("lowerLimit", lowerLimit);
                postRepository.updatePostNumForInsertRow(range);

                postRepository.insertPost(post);
                postRepository.insertPostStatus(post);
            }
        }catch (RuntimeException re){
            throw new InvalidPostException(re.getMessage());
        }
    }

    @Override
    public void deleteAllPosts() {
        postRepository.deleteAll();
    }

    @Override
    public Post getPostByPostId(int postId) {
        Post selectedPost = postRepository.findPostByPostId(postId);
        if(selectedPost == null){
            throw new InvalidPostException();
        }
        return selectedPost;
    }

    @Override
    public void changePostStatusByPostId(int postId, String postStatus) {
        Map<String,Object> postStatusArgs = new HashMap<>();
        postStatusArgs.put("postId",postId);
        postStatusArgs.put("postStatus", postStatus);
        postRepository.updatePostStatus(postStatusArgs);
    }

    @Override
    public void addPostFile(PostFile postFile) {
        postFileRepository.insertPostFile(postFile);
    }

    @Override
    public List<PostFile> getPostFilesByPostId(int postId) {
        List<PostFile> postFiles = postFileRepository.findFilesByPostId(postId);
        if(postFiles == null){
            throw new InvalidPostException();
        }
        return postFiles;
    }

    @Override
    public void modifyPost(Post post) {
        postRepository.updatePost(post);
    }

    @Override
    public void changePostStatusToDelete(int postId) {
        postRepository.updatePostStatusToDelete(postId);
        changeCmtStatusByPostId(postId,"D");

    }

    @Override
    public int getPostReportCount(int postId) {

        return postRepository.getReportCount(postId);
    }

    @Override
    public void reportPost(PostReport postReport) {
        try {
            postRepository.insertReport(postReport);
        }catch (RuntimeException re){
            throw new InvalidPostException("can't report."+re.getMessage());
        }
    }

    @Override
    public List<PostReport> getPostReports(int postId) {
        try{
            return postRepository.findReportByPostId(postId);
        }catch (RuntimeException re){
            throw new InvalidPostException();
        }
    }

    @Override
    public void changeProcessStatusFromPost(PostReport postReport) {
        try{
            postRepository.updateProcessStatus(postReport);
        }catch (RuntimeException re){
            throw new InvalidPostException();
        }
    }

    @Override
    public void modifyComment(Comment comment) {
        commentRepository.updateComment(comment);

    }

    @Override
    public int getCmtReportCount(int cmtId) {
        return commentRepository.getReportCount(cmtId);
    }

    @Override
    public void reportComment(CommentReport commentReport) {
        try {
            commentRepository.insertReport(commentReport);
        }catch (RuntimeException re){
            throw new InvalidPostException("can't report."+re.getMessage());
        }
    }

    @Override
    public List<CommentReport> getCommentReports(int cmtId) {
        try{
            return commentRepository.findReportByCmtId(cmtId);
        }catch (RuntimeException re){
            throw new InvalidPostException();
        }
    }

    @Override
    public void changeProcessStatusFromCmt(CommentReport commentReport) {
        try{
            commentRepository.updateProcessStatus(commentReport);
        }catch (RuntimeException re){
            throw new InvalidPostException();
        }
    }

    @Override
    public List<Pagination> getPagination(int offset) {
//        offset param을 이용해서 현재 페이지 넘버를 계산합니다.
        int currentPage = offset/20 + 1;

//        페이징은 5 페이지씩 그룹핑을 합니다.이전 그룹 페이지 offset을 담는 변수를 선언.
        int previousPage;

        // 현재 페이지가 5보다 크면 이전 페이지를 계산해야하고, 그렇지 않다면 이전 페이지는 첫번째 페이지가 됩니다.
        if(currentPage > 5) {
            previousPage = currentPage / 5 * 5;
        }else {
            previousPage = 1;
        }

        // 다음 그룹 페이지 offset을 계산해서 olderpage 변수에 담습니다.
        int olderPage = previousPage + 5;

        // 현재 게시물을 표시하는 페이지가 총 5개가 안된다면, maxPage가 최종페이지가 됩니다.
        int maxPage = postRepository.countPost() / 20 + 1;
        if(maxPage<olderPage){
            olderPage = maxPage;
        }

        //페이징 범위(prev - older)까지를 정하고 각 페이징 넘버와 offset을 pagination 모델에 담습니다.
        List<Pagination> paginations = new ArrayList<Pagination>();
        for(int i = previousPage;i<=olderPage;i++){
            Pagination pagination = new Pagination();
            pagination.setPage(i);
            pagination.setOffset(i*20);
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
    public List<Post> getPostsByUserId(int userId) {
        return postRepository.findPostsByUserId(userId);
    }

    @Override
    public List<Comment> getCommentsByUserId(int userId) {
        return commentRepository.findCommentsByUserId(userId);

    }



    @Override
    public List<Comment> getComments() {
        return commentRepository.findComments();
    }

    @Override
    public List<Comment> getCommentsByPostId(int postId) {
        return commentRepository.findCommentsByPostId(postId);
    }


    @Override
    public void addComment(Comment comment) {

        //없는 게시글에 코멘트를 달려고 할 때, 예외처리.
        if(postRepository.findPostByPostId(comment.getPostId()) == null){
            throw new InvalidPostException("post is nonexistent. check post information.");
        }


        if(comment.getDepth() < 1){
            commentRepository.insertComment(comment);
            commentRepository.insertCommentStatus(comment);
        }else{
            Map<String,Integer> range = new HashMap<>();
            range.put("upperLimit",comment.getCmtNum());
            int lowerLimit = (comment.getCmtNum()) / 1000 * 1000 +1;
            range.put("lowerLimit", lowerLimit);
            commentRepository.updateCmtNumForInsertRow(range);
            commentRepository.insertComment(comment);
            commentRepository.insertCommentStatus(comment);
        }
        postRepository.addCmtCount(comment.getPostId());
    }

    @Override
    public void deleteAllComments() {
        commentRepository.deleteAll();
    }

    @Override
    public void changeCmtStatusByCmtId(int cmtId, String cmtStatus) {
        Map<String,Object> cmtStatusArgs = new HashMap<>();
        cmtStatusArgs.put("cmtId",cmtId);
        cmtStatusArgs.put("cmtStatus", cmtStatus);
        commentRepository.updateCmtStatusByCmtId(cmtStatusArgs);
    }

    @Override
    public Comment getCommentByCmtId(int cmtId) {
        try {
            Comment selectedCmt = commentRepository.findCommentByCmtId(cmtId);
            if (selectedCmt == null) {
                throw new InvalidCmtException();
            }
            return selectedCmt;
        }catch (RuntimeException re){
            throw new InvalidCmtException(re.getMessage());
        }
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public void addUser(User user){
        try {
            if (user.getIsOAuth().equals("F")) {
                userRepository.insertUser(user);
            } else {
                //Todo: OAuth logic
            }
        }catch(Exception sq){
            throw new InvalidUserException("User email or Identity is already existed. (detail : "+sq.getMessage()+")");
        }
    }

    @Override
    public User getUserByUserId(int userId) {
        User selectedUser = userRepository.findUserByUserId(userId);
        if(selectedUser == null){
            throw new InvalidUserException();
        }
        return selectedUser;

    }

    @Override
    public User getUserByIdentity(String identity) {
        User selectedUser = userRepository.findUserByIdentity(identity);
        if(selectedUser == null){
            throw new InvalidUserException();
        }
        return selectedUser;
    }

    @Override
    public User getUserByName(String name) {
        User selectedUser = userRepository.findUserByName(name);
        if(selectedUser == null){
            throw new InvalidUserException();
        }
        return selectedUser;
    }

    @Override
    public String uploadProfile(int userId, MultipartFile uploadFile) {
        try {

            //프로필 작명 규칙 : lionboard_profile_{userId}.jpg
            String fileName = "lionboard_profile_"+String.valueOf(userId)+".jpg";

            String uploadUrl = insertFileOnTenthServer(uploadFile.getBytes(), fileName);

            return uploadUrl;
        } catch (Exception e) {

            //todo logging.
            System.out.println("Thenth2 이미지 업로드 실패.");
            e.printStackTrace();
            throw new InvalidUserException("fail to upload profile image. but To sign up is succeeded.");
        }
    }

    private String insertFileOnTenthServer(byte[] imageFileBytes, String fileName) throws Exception {
        try {
            imageFileUploadForTenth2.init();
            return imageFileUploadForTenth2.create(imageFileBytes, fileName);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void updateProfileInfoOnUser(int userId, String uploadedUrl) {
        User user = new User();
        user.setId(userId);
        user.setProfileUrl(uploadedUrl);
        try {
            userRepository.updateProfileInfo(user);
        }catch (RuntimeException e){
            //todo:logging
            System.out.println(e.getStackTrace());
            throw new InvalidUserException(e.getMessage());
        }
    }

    @Override
    public PostFile addFileToTenth(int postId, MultipartFile uploadFile) {

        List<PostFile> postFiles = new ArrayList<PostFile>();
        PostFile postFile = new PostFile();

        try {

            //첨부파일 작명 규칙 : lionboard_post_File_{postId}_{originalFileName}
            String fileName = "lionboard_post_File_"+postId+"_"+uploadFile.getOriginalFilename();
            String uploadUrl = insertFileOnTenthServer(uploadFile.getBytes(), fileName);
            postFile.setPostId(postId);
            postFile.setFileUrl(uploadUrl);
            postFile.setFileName(uploadFile.getOriginalFilename());
            postFiles.add(postFile);
        } catch (Exception e) {

            //todo logging.
            System.out.println("Thenth2 업로드 실패.");
            e.printStackTrace();
            throw new InvalidPostException("fail to upload files.");
        }

        return postFile;
    }

    @Override
    public void addPostFile(int postId, PostFile postFile) {
        postFile.setPostId(postId);
        postFile.setFileName(postFile.getFileName());
        postFile.setFileUrl(postFile.getFileUrl());
        System.out.println("uploaded file named" + postFile.getFileName() + " url is " + postFile.getFileUrl() + " to " + postId);
        postFileRepository.insertPostFile(postFile);
    }

    @Override
    public void addPostWithFile(Post post) {
        try {
            //Post 기본 정보 등록
            addPost(post);
            // 등록된 Post Id 반환.
            int postId = post.getPostId();
            //Id와 uploadfile 정보를 이용해서 tenth서버에 파일 업로드 후
            PostFile postFile = addFileToTenth(post.getPostId(), post.getUploadFile());
            System.out.println("uploaded file named" + postFile.getFileName() + " url is " + postFile.getFileUrl() + " to " + postId);
            postFileRepository.insertPostFile(postFile);
        }catch (RuntimeException re){
            throw new InvalidPostException(re.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            throw new UploadFileToTenthException("Tenth 서버에 파일을 업로드할 수 없습니다."+e.getMessage());
        }
    }


    //특정 포스트의 상태가 변경되면 해당 포스트의 코멘트도 상태가 같이 변경됨.
    @Override
    public void changeCmtStatusByPostId(int postId, String status) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setCmtStatus(status);
        commentRepository.updateCmtStatusByPostId(comment);
    }

    @Override
    public Post getReplyPostByPostId(int postId) {
        Post selectedPost = postRepository.findPostByPostId(postId);
        if(selectedPost == null){
            throw new InvalidPostException();
        }
        //답글은 부모글의 depth + 1에 해당하는 depth를 가짐.
        selectedPost.setDepth(selectedPost.getDepth()+1);
        //답글은 부모글의 PostNum - 1에 해당하는 PostNum를 가짐.
        selectedPost.setPostNum(selectedPost.getPostNum()-1);

        return selectedPost;
    }


    @Override
    public void modifyUser(User user) {
        userRepository.updateUser(user);
    }

    @Override
    public void removeUserById(int userId) {
        userRepository.deleteUserById(userId);
    }

    @Override
    public void changeUserStatusToLeave(int userId) {
        userRepository.updateUserStatusToLeave(userId);

    }




    @Override
    public int getPostLike(int postId) {
        Integer likeCount = postRepository.getLikeCount(postId);
        if(likeCount == null){
            throw new InvalidPostException();
        }
        return likeCount;
    }

    @Override
    public void addPostLike(int postId) {
        int rows = postRepository.addLikeCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public void subtractPostLike(int postId) {
        int rows = postRepository.subtractLikeCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public int getPostHate(int postId) {
        Integer hateCount = postRepository.getHateCount(postId);
        if(hateCount == null){
            throw new InvalidPostException();
        }
        return hateCount;
    }

    @Override
    public void addPostHate(int postId) {
        int rows = postRepository.addHateCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public void subtractPostHate(int postId) {
        int rows = postRepository.subtractHateCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public int getPostView(int postId) {
        Integer hateCount = postRepository.getViewCount(postId);
        if(hateCount == null){
            throw new InvalidPostException();
        }
        return hateCount;
    }

    @Override
    public void addPostView(int postId) {
        int rows = postRepository.addViewCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public void subtractPostView(int postId) {
        int rows = postRepository.subtractViewCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public int getCmtLike(int cmtId) {
        Integer likeCount = commentRepository.getLikeCount(cmtId);
        if(likeCount == null){
            throw new InvalidCmtException();
        }
        return likeCount;
    }

    @Override
    public void addCmtLike(int cmtId) {
        int rows = commentRepository.addLikeCount(cmtId);
        if(rows == 0){
            throw new InvalidCmtException();
        }
    }

    @Override
    public void subtractCmtLike(int cmtId) {
        int rows = commentRepository.subtractLikeCount(cmtId);
        if(rows == 0){
            throw new InvalidCmtException();
        }
    }

    @Override
    public int getCmtHate(int cmtId) {
        Integer likeCount = commentRepository.getHateCount(cmtId);
        if(likeCount == null){
            throw new InvalidCmtException();
        }
        return likeCount;
    }

    @Override
    public void addCmtHate(int cmtId) {
        int rows = commentRepository.addHateCount(cmtId);
        if(rows == 0){
            throw new InvalidCmtException();
        }
    }

    @Override
    public void subtractCmtHate(int cmtId) {
        int rows = commentRepository.subtractHateCount(cmtId);
        if(rows == 0){
            throw new InvalidCmtException();
        }
    }

}
