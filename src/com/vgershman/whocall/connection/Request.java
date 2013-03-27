package com.vgershman.whocall.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.vgershman.whocall.app.AppInfo;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 06.01.13
 * Time: 0:10
 * To change this template use File | Settings | File Templates.
 */
public class Request {

    private static final String HOST = "http://www.kodtelefona.ru/api/phone/";
    private static final String HOST2 = "http://www.kodtelefona.ru/api/2/phone/";

    public static void getCommentsByNumber(String number, CommentCallback commentCallback){
        new CommentGetTask(HOST + "comment_"+number,commentCallback).execute();
    }

    public static void getInfoByNumber(String number, RequestGetCallback requestGetCallback) {
        new RequestGetTask(HOST2 + number, requestGetCallback).execute();
    }

    public static void getInfoByNumber(String number, String from, RequestGetCallback requestGetCallback) {
        new RequestGetTask(HOST2 + number+"&from="+from, requestGetCallback).execute();
    }

    public static void postInfo(Map<String, String> data, RequestPostCallback requestPostCallback) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String dataField : data.keySet()) {
            params.add(new BasicNameValuePair("data[" + dataField + "]", data.get(dataField)));
        }

        new RequestPostTask(HOST + "add", params, requestPostCallback).execute();

    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
