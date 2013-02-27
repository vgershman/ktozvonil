package com.vgershman.whocall.dto;

import android.text.format.DateUtils;

/**
 * Created with IntelliJ IDEA.
 * User: vkosarev
 * Date: 23.02.13
 * Time: 1:23
 * To change this template use File | Settings | File Templates.
 */
public class Call {
    private String number;
    private String name;
    private long time;
    private boolean isOut;

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTimeString() {
        return "";//String.valueOf(DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), 6000));
    }

    public boolean hasName() {
        if (name != null && !name.equals("")) {
            return true;
        } else {
            return false;
        }
    }
}
