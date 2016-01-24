package com.github.lionboard.model;

import org.joda.time.DateTime;

/**
 * Created by lion.k on 16. 1. 20..
 */
public class Post {
    private int postId;
    private int userId;
    private String userName;
    private String title;
    private String contents;
    private int depth;
    private int postNum;
    private int viewCount;
    private int likeCount;
    private int hateCount;
    private int cmtCount;
    private String existFiles;
    private DateTime createAt;
    private String postStatus;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getPostNum() {
        return postNum;
    }

    public void setPostNum(int postNum) {
        this.postNum = postNum;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getHateCount() {
        return hateCount;
    }

    public void setHateCount(int hateCount) {
        this.hateCount = hateCount;
    }

    public int getCmtCount() {
        return cmtCount;
    }

    public void setCmtCount(int cmtCount) {
        this.cmtCount = cmtCount;
    }


    public DateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(DateTime createAt) {
        this.createAt = createAt;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getExistFiles() {
        return existFiles;
    }

    public void setExistFiles(String existFiles) {
        this.existFiles = existFiles;
    }
}
