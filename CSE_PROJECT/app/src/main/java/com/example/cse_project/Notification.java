package com.example.cse_project;

public class Notification {
    private String notiKey;
    private String notificationName;
    private String noticeDescription;


    public Notification() {
    }

    // Constructor
    public Notification(String notiKey, String notificationName, String noticeDescription) {
        this.notiKey = notiKey;
        this.notificationName = notificationName;
        this.noticeDescription = noticeDescription;
    }

    // Getter và Setter cho notiKey
    public String getNotiKey() {
        return notiKey;
    }

    public void setNotiKey(String notiKey) {
        this.notiKey = notiKey;
    }

    // Getter và Setter cho notificationName
    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    // Getter và Setter cho noticeDescription
    public String getNoticeDescription() {
        return noticeDescription;
    }

    public void setNoticeDescription(String noticeDescription) {
        this.noticeDescription = noticeDescription;
    }
}

