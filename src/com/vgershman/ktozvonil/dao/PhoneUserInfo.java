package com.vgershman.ktozvonil.dao;

import android.content.pm.ApplicationInfo;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.vgershman.ktozvonil.app.*;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 06.01.13
 * Time: 16:09
 * To change this template use File | Settings | File Templates.
 */
public class PhoneUserInfo {

     String name;
     String []type;
     String location;
     String image;
     String point;
     String url;
     String phone;
     String worktime;
     String desc;
     String url_donor;
     String url_from;
     String operator;
     String region;
     String operator_site;
     String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWorktime() {
        return worktime;
    }

    public void setWorktime(String worktime) {
        this.worktime = worktime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl_donor() {
        return url_donor;
    }

    public void setUrl_donor(String url_donor) {
        this.url_donor = url_donor;
    }

    public String getUrl_from() {
        return url_from;
    }

    public void setUrl_from(String url_from) {
        this.url_from = url_from;
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

    public String getOperator_site() {
        return operator_site;
    }

    public void setOperator_site(String operator_site) {
        this.operator_site = operator_site;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static PhoneUserInfo createFromJSONObject(JSONObject data){
        PhoneUserInfo result = new PhoneUserInfo();
        try {

            for (Field field:PhoneUserInfo.class.getDeclaredFields()){
                 String fieldname = field.getName();
                 if(data.has(fieldname)){
                    if(fieldname.equals("type")){
                        result.setType((String [])data.get("type"));
                    }
                    field.set(result,data.getString(fieldname));
                 }
            }
        }catch (IllegalAccessException er){
            Log.e("KodTelefona", er.getMessage());

        }catch (JSONException ex){
            Log.e("KodTElefona", ex.getMessage());
        }
        return result;
    }
}
