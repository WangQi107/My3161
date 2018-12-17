package com.wq.myclass.bean;

public class Notice {
    private int id;
    private String title;
    private String name;
    private String time;
    private String content;

    public Notice() {
    }

    public Notice(String title, String name, String time, String content) {
        this.title = title;
        this.name = name;
        this.time = time;
        this.content = content;
    }

    public Notice(int id, String title, String name, String time, String content) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.time = time;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
