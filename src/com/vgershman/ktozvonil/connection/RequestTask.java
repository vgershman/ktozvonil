package com.vgershman.ktozvonil.connection;

import android.os.AsyncTask;
import android.util.Log;
import com.vgershman.ktozvonil.dao.PhoneUserInfo;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 06.01.13
 * Time: 0:15
 * To change this template use File | Settings | File Templates.
 */
public class RequestTask extends AsyncTask{
    boolean fail=false;
    PhoneUserInfo result=null;
    String url;
    RequestCallback requestCallback;

    public RequestTask(String url, RequestCallback requestCallback) {
        this.url=url;
        this.requestCallback=requestCallback;
    }

    @Override
    protected Object doInBackground(Object... objects) {

        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            String line;
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            String page = sb.toString();
            JSONObject jsonObject= new JSONObject(page);

            if(!jsonObject.has("msg")){
                result = PhoneUserInfo.createFromJSONObject(jsonObject.getJSONObject("data"));
            }

        } catch (Exception ex){
            Log.e("KtoZvonil", ex.getMessage());
            fail=true;
            return null;
        }

        return null;

    }

    @Override
    protected void onPostExecute(Object o) {
        if(!fail){
            if(result!=null){
                requestCallback.onInfoFound(result);
            }else{
                requestCallback.onNotFound();
            }
        }else {
            requestCallback.onFailure();
        }
    }
}
