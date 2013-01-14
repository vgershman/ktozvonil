package com.vgershman.ktozvonil.connection;

import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 06.01.13
 * Time: 0:10
 * To change this template use File | Settings | File Templates.
 */
public class Request {

    private static final String HOST = "http://www.kodtelefona.ru/api/phone/";

    public static void getInfoByNumber(String number, RequestGetCallback requestGetCallback){
        new RequestGetTask(HOST + number, requestGetCallback).execute();

    }
    public static void postInfo(String phone,String name, String description, String image, String email, RequestPostCallback requestPostCallback){
       /* JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        try {

            data.put("phone",phone);
            data.put("name",name);
            data.put("description",description);
            data.put("image",image);
            data.put("email",email);
            JSONObject additional = new JSONObject();
            additional.put("type","test");
            params.put("data",data);
            params.put("additional",additional);

        }catch (JSONException ex){
            Log.e("KtoZvonil",ex.getMessage());
            requestPostCallback.onFailure();
        }
*/
        List<NameValuePair> params= new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("data[phone]",phone));
        params.add(new BasicNameValuePair("data[name]",name));
        params.add(new BasicNameValuePair("data[description]",description));
        params.add(new BasicNameValuePair("data[email]",email));
        params.add(new BasicNameValuePair("data[image]",image));
        params.add(new BasicNameValuePair("additional[type]","test"));

        //additional.put("location",location);
        //additional.put("point",point);
        //additional.put("url",url);
        //additional.put("worktime",worktime);
        //additional.put("from",from);
        //additional.put("operator",operator);
        //additional.put("region",region);


        new RequestPostTask(HOST + "add",params, requestPostCallback).execute();

    }
}
