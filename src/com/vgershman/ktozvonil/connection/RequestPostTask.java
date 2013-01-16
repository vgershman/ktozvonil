package com.vgershman.ktozvonil.connection;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 06.01.13
 * Time: 0:15
 * To change this template use File | Settings | File Templates.
 */
public class RequestPostTask extends AsyncTask{

    List<NameValuePair> params;
    boolean fail=false;
    PhoneUserInfo result=null;
    String url;
    RequestPostCallback requestPostCallback;

    public RequestPostTask(String url, List<NameValuePair> params,RequestPostCallback requestPostCallback) {
        this.url=url;
        this.requestPostCallback = requestPostCallback;
        this.params=params;
    }

    @Override
    protected Object doInBackground(Object... objects) {

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            request.setURI(new URI(url));


            request.setEntity(new UrlEncodedFormEntity(params));


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
                //requestGetCallback.onInfoFound(result);
            }else{
                //requestGetCallback.onNotFound();
            }
        }else {
            //requestGetCallback.onFailure();
        }
    }
}
