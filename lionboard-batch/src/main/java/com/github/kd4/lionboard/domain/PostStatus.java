package com.github.kd4.lionboard.domain;

/**
 * Created by daum on 16. 2. 22..
 */
public class PostStatus {
    private int postId;
    private int pastDays;
    private String postStatus;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getPastDays() {
        return pastDays;
    }

    public void setPastDays(int pastDays) {
        this.pastDays = pastDays;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }
}
