package com.github.lionboard.model;

import java.util.Date;

/**
 * Created by lion.k on 16. 1. 12..
 */


public class TempModel {
    private int id;
    private String name;
    private String title;
    private long created_at;

    public TempModel(int id, String name, String title){
        this.id = id;
        this.name = name;
        this.title = title;
        this.created_at = new Date().getTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
