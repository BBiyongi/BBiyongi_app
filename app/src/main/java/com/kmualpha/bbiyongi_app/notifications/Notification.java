package com.kmualpha.bbiyongi_app.notifications;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Notification implements Serializable {
    String type;
    int img_url;
    String date;
    String pos;
    String link;
    String cam_id;
    boolean checked;
    String aed;

    public Notification(String type, int img_url, String date, String pos, String link, String cam_id, boolean checked, String aed) {
        if (Objects.equals(type, "attack") || Objects.equals(type, "arrest"))
            this.type = type;
        else
            this.type = "attack";
        this.img_url = img_url;
        this.date = date;
        this.pos = pos;
        this.link = link;
        this.cam_id = cam_id;
        this.checked = checked;
        this.aed = aed;
    }

    public String getType() { return this.type; }
    public int getImg_url() { return this.img_url; }
    public String getDate() { return this.date; }
    public String getStringDate() {
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        String hour = date.substring(9, 11);
        String minute = date.substring(11, 13);
        return String.format("%s년 %s월 %s일 %s:%s", year, month, day, hour, minute);
    }
    public String getPos() { return this.pos; }
    public String getLink() { return this.link; }
    public String getCam_id() { return this.cam_id; }
    public boolean getChecked() { return this.checked; }
    public String getAed() { return this.aed; }

    @Override
    public String toString() {
        return "type: " + type + ", date: " + date + ", pos: " + pos + ", link: " + link + ", camId: " + cam_id + ", checked: " + checked + ", AED: " + aed;
    }

}
