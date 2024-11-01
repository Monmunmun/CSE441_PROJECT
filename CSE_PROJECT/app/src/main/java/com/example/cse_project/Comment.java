package com.example.cse_project;

public class Comment {
    private String bookKey;
    private String userId;
    private String content;
    private long timestamp;


    public Comment() {
    }


    public Comment(String bookKey, String userId, String content, long timestamp) {
        this.bookKey = bookKey;
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
    }


    public String getBookKey() {
        return bookKey;
    }

    public void setBookKey(String bookKey) {
        this.bookKey = bookKey;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
