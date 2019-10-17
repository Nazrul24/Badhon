package kazinazrul.csedu24.badhon_knih;

import android.util.EventLogTags;

import java.util.HashMap;

public class Posts {
    public String uid, date, time, description, postimage, profileimage, fullname;

    public Posts ()
    {

    }

    public Posts(String uid, String date, String time, String description, String postimage, String profileimage, String fullname) {
        this.uid = uid;
        this.date = date;
        this.time = time;
        this.description = description;
        this.postimage = postimage;
        this.profileimage = profileimage;
        this.fullname = fullname;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUid() {
        return uid;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getPostimage() {
        return postimage;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public String getFullname() {
        return fullname;
    }
}
