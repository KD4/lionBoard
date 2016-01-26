package com.github.lionboard.model;

import org.joda.time.DateTime;

/**
 * Created by daum on 16. 1. 26..
 */
public class CommentReport {
    private int id;
    private int cmtId;
    private int reporterId;
    private String reason;
    private DateTime reportedAt;
    private String processStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReporterId() {
        return reporterId;
    }

    public void setReporterId(int reporterId) {
        this.reporterId = reporterId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DateTime getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(DateTime reportedAt) {
        this.reportedAt = reportedAt;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public int getCmtId() {
        return cmtId;
    }

    public void setCmtId(int cmtId) {
        this.cmtId = cmtId;
    }


}
