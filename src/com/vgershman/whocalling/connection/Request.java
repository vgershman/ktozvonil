package com.vgershman.whocalling.connection;

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

    public static void getInfoByNumber(String number, RequestGetCallback requestGetCallback){
        new RequestGetTask(HOST + number, requestGetCallback).execute();

    }
    public static void postInfo(Map<String,String> data, Map<String, String> additional, RequestPostCallback requestPostCallback){

        List<NameValuePair> params= new ArrayList<NameValuePair>();
        for(String dataField:data.keySet()){
            params.add(new BasicNameValuePair("data["+dataField+"]",data.get(dataField)));
        }
        for(String addField:additional.keySet()){
            params.add(new BasicNameValuePair("data["+addField+"]",additional.get(addField)));
        }


        new RequestPostTask(HOST + "add",params, requestPostCallback).execute();

    }
}
