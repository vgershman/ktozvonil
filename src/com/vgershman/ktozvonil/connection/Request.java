package com.vgershman.ktozvonil.connection;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 06.01.13
 * Time: 0:10
 * To change this template use File | Settings | File Templates.
 */
public class Request {

    private static final String HOST = "http://www.kodtelefona.ru/api/phone/";

    public static void getInfoByNumber(String number, RequestCallback requestCallback){
        new RequestTask(HOST + number,requestCallback).execute();

    }

}
