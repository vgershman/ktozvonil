package com.vgershman.whocall.connection;

import android.os.AsyncTask;
import android.util.Log;
import com.vgershman.whocall.app.AppInfo;
import com.vgershman.whocall.dto.Comment;
import com.vgershman.whocall.dto.NotFoundInfo;
import com.vgershman.whocall.dto.PhoneUserInfo;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 26.02.13
 * Time: 22:00
 * To change this template use File | Settings | File Templates.
 */
public class CommentGetTask extends AsyncTask {
    boolean fail=false;
    String url;
    CommentCallback commentCallback;
    List<Comment> result;

    public CommentGetTask(String url, CommentCallback commentCallback) {
        this.url=url;
        this.commentCallback = commentCallback;
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

            if(jsonObject.has("data")){
                jsonObject = jsonObject.getJSONObject("data");
                result = new ArrayList<Comment>();

                JSONArray array = jsonObject.getJSONArray("comment");
                for(int i = 0;i<array.length();i++){
                    Comment comment = Comment.createFromJSON(array.getJSONObject(i));
                    result.add(comment);
                }


            }else {
                result = null;
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
                commentCallback.onFound(result);
            }else{
                commentCallback.onNotFound();
            }
        }else {
            commentCallback.onError();
        }
    }
}

