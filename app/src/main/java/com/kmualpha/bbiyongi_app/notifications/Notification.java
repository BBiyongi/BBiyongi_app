package com.kmualpha.bbiyongi_app.notifications;

import java.util.Date;

public class Notification {
    int img_url;
    Date date;

    public Notification(int img_url, Date date) {
        this.img_url = img_url;
        this.date = date;
    }

    public int getImg_url() {
        return this.img_url;
    }
    public Date getDate() {
        return this.date;
    }
}
