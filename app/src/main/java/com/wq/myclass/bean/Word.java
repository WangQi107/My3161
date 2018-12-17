package com.wq.myclass.bean;

public class Word {
    private int id;
    private String name;
    private String time;
    private String content;
    private int header;

    public Word() {
    }

    public Word(String name, String time, String content, int header) {
        this.name = name;
        this.time = time;
        this.content = content;
        this.header = header;
    }

    public Word(int id, String name, String time, String content, int header) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.content = content;
        this.header = header;
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

    public int getHeader() {
        return header;
    }

    public void setHeader(int header) {
        this.header = header;
    }
}
