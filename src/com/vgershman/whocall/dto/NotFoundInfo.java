package com.vgershman.whocall.dto;

import android.util.Log;
import com.vgershman.whocall.app.AppInfo;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Вадим
 * Date: 31.01.13
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
public class NotFoundInfo {

    private String phone;
    private String operator;
    private String region;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public static NotFoundInfo createFromJson(JSONObject jsonObject){
        NotFoundInfo notFoundInfo = new NotFoundInfo();
        try {
            if(jsonObject.has("operator")){
                notFoundInfo.setOperator(jsonObject.getString("operator"));
            }
            if(jsonObject.has("o_region")){
                notFoundInfo.setRegion(jsonObject.getString("o_region"));
            }
            if(jsonObject.has("count")){
                notFoundInfo.setCount(jsonObject.getInt("count"));
            }
        }catch (JSONException ex){
            Log.e(AppInfo.APP_NAME,ex.getMessage());
        }
        return notFoundInfo;
    }
}
