package com.vgershman.ktozvonil.dao;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 06.01.13
 * Time: 16:09
 * To change this template use File | Settings | File Templates.
 */
public class PhoneUserInfo {

    private String phone;
    private String name;
    private String email;
    private String operator;
    private String region;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public static PhoneUserInfo createFromJSONObject(JSONObject data){
        PhoneUserInfo result = new PhoneUserInfo();
        try {

            if(data.has("phone")){
                result.setPhone(data.getString("phone"));
            }
            if(data.has("name")){
                result.setName(data.getString("name")) ;
            }
            if(data.has("email")){
                result.setEmail(data.getString("email")) ;
            }
            if(data.has("operator")){
                result.setOperator(data.getString("operator")) ;
            }
            if(data.has("region")){
                result.setRegion(data.getString("region")) ;
            }

        }catch (JSONException ex){
            Log.e("KtoZvonil", ex.getMessage());
        }
        return result;
    }
}
