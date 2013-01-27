package com.vgershman.whocalling.dto;

import android.util.Log;
import com.vgershman.whocalling.app.AppInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Вадим
 * Date: 16.01.13
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public class PhoneUserInfo {

    private String phone;
    private Map<String, Object> info = new HashMap<String, Object>();

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }

    public static PhoneUserInfo createFromJSONObject(JSONObject data) {
        PhoneUserInfo result = new PhoneUserInfo();
        try {
            JSONArray names = data.names();
            for (int i = 0; i < names.length(); i++) {
                String name = names.getString(i);
                if (name.equals("phone")) {
                    result.phone = data.getString(name);
                } else {
                    Object element = data.get(name);
                    Object toPut;
                    if (element instanceof JSONArray) {
                        toPut = new ArrayList<String>();
                        JSONArray array = (JSONArray) element;
                        for (int j = 0; j < array.length(); j++) {
                            ((List<String>) toPut).add(array.getString(j));
                        }


                    } else {

                        toPut = data.getString(name);
                    }
                    result.info.put(name, toPut);
                }
            }
        } catch (JSONException ex) {
            Log.e(AppInfo.SERVICE_NAME, ex.getMessage());
        }
        return result;
    }

}
