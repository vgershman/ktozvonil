package com.vgershman.whocall.connection;

import android.os.AsyncTask;
import android.util.Log;
import com.vgershman.whocall.app.AppInfo;
import com.vgershman.whocall.dto.NotFoundInfo;
import com.vgershman.whocall.dto.PhoneUserInfo;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 06.01.13
 * Time: 0:15
 * To change this template use File | Settings | File Templates.
 */
public class RequestGetTask extends AsyncTask{
    boolean fail=false;
    PhoneUserInfo result=null;
    String url;
    RequestGetCallback requestGetCallback;
    NotFoundInfo notFoundInfo = null;

    public RequestGetTask(String url, RequestGetCallback requestGetCallback) {
        this.url=url;
        this.requestGetCallback = requestGetCallback;
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
            } else {
                notFoundInfo = NotFoundInfo.createFromJson(jsonObject.getJSONObject("data"));
            }

        } catch (IOException ex){
            Log.e(AppInfo.APP_NAME, ex.getMessage());
            fail=true;
            return null;

        } catch (JSONException ex){
            Log.e(AppInfo.APP_NAME, ex.getMessage());
            fail=true;
            return null;
        } catch (URISyntaxException e) {
            Log.e(AppInfo.APP_NAME, e.getMessage());
            fail=true;
            return null;
        }

        return null;

    }

    @Override
    protected void onPostExecute(Object o) {
        if(!fail){
            if(result!=null){
                requestGetCallback.onInfoFound(result);
            }else{
                requestGetCallback.onNotFound(notFoundInfo);
            }
        }else {
            requestGetCallback.onFailure();
        }
    }
}
