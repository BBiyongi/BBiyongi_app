package com.kmualpha.bbiyongi_app.notifications;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Notification implements Serializable {
    String type;
    int img_url;
    Date date;
    String pos;
    String link;
    String cam_id;
    boolean checked;

    public Notification(String type, int img_url, Date date, String pos, String link, String cam_id, boolean checked) {
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
    }

    public String getType() { return this.type; }
    public int getImg_url() {
        return this.img_url;
    }
    public Date getDate() {
        return this.date;
    }
    public String getPos() { return this.pos; }
    public String getLink() { return this.link; }
    public String getCam_id() { return this.cam_id; }
    public boolean getChecked() { return this.checked; }

    @Override
    public String toString() {
        return "type: " + type + ", date: " + date + ", pos: " + pos + ", link: " + link + ", camId: " + cam_id + ", checked: " + checked;
    }

}
