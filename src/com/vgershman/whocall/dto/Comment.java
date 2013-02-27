package com.vgershman.whocall.dto;

import android.util.Log;
import com.vgershman.whocall.app.AppInfo;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: vkosarev
 * Date: 26.02.13
 * Time: 10:35
 * To change this template use File | Settings | File Templates.
 */
public class Comment {
    private String name="";
    private String text="";
    private long time;
    private String avatar;
    private int liked=0;

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static Comment createFromJSON(JSONObject jsonObject) {
        Comment comment = new Comment();
        try {

            if (jsonObject.has("comment")) {
                comment.setText(jsonObject.getString("comment"));
            }
            if (jsonObject.has("date_insert")) {
                comment.setTime(jsonObject.getLong("date_insert"));
            }
            if (jsonObject.has("user_name")) {
                comment.setName(jsonObject.getString("user_name"));
            }
            if(jsonObject.has("liked")){
                comment.setLiked(jsonObject.getInt("liked"));
            }

        } catch (JSONException ex) {
            Log.e(AppInfo.APP_NAME, ex.getMessage());
        }
        return comment;
    }

}
