package com.github.lionboard.model;

/**
 * Created by daum on 16. 1. 27..
 */
public class Pagination {
    private int page;
    private int offset;
    private boolean isCurrent;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }
}
