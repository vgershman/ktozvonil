package com.vgershman.ktozvonil.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import com.google.android.maps.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 18.11.12
 * Time: 21:04
 * To change this template use File | Settings | File Templates.
 */
public class ReverseGeoCoding extends AsyncTask {

    private enum RESULT{SUCCESS,FAILURE}

    public ReverseGeoCoding(Context context,GeoPoint geoPoint,ReverseGeoCodingListener listener) {
        this.listener=listener;
        this.geoPoint = geoPoint;
        this.mContext=context;
    }

    private ReverseGeoCodingListener listener;
    private Context mContext;
    private GeoPoint geoPoint;
    private String textResult;

    @Override
    protected Object doInBackground(Object... objects) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        RESULT result=RESULT.FAILURE;

        List<Address> addresses = null;
        try {
            // Call the synchronous getFromLocation() method by passing in the lat/long values.
            addresses = geocoder.getFromLocation(geoPoint.getLatitudeE6()/1E6,geoPoint.getLongitudeE6()/1E6, 1);
        } catch (IOException e) {
            e.printStackTrace();
            // Update UI field with the exception.

        }
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);

            String addressText = String.format("%s, %s, %s",
                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                    address.getLocality(),
                    address.getCountryName());
            result=RESULT.SUCCESS;
            textResult=addressText;

        }

        return result;
    }

    @Override
    protected void onPostExecute(Object o) {
        if(o==RESULT.FAILURE){
            listener.OnFailure();
        }else{
            listener.OnSuccess(textResult);
        }
    }
}