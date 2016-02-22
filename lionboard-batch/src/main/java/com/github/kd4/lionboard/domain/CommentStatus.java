package com.github.kd4.lionboard.domain;

/**
 * Created by daum on 16. 2. 22..
 */
public class CommentStatus {
    private int cmtId;
    private int pastDays;

    private String cmtStatus;

    public String getCmtStatus() {
        return cmtStatus;
    }

    public void setCmtStatus(String cmtStatus) {
        this.cmtStatus = cmtStatus;
    }

    public int getCmtId() {
        return cmtId;
    }

    public void setCmtId(int cmtId) {
        this.cmtId = cmtId;
    }

    public int getPastDays() {
        return pastDays;
    }

    public void setPastDays(int pastDays) {
        this.pastDays = pastDays;
    }


}
