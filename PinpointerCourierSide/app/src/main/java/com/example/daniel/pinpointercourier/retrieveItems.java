package com.example.daniel.pinpointercourier;

/**
 * Created by Daniel on 1/25/17.
 */

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.PinpointerCodesDO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class retrieveItems extends AsyncTask<String,Void,String> {

    GoogleMap mMap;

    public retrieveItems(GoogleMap map){
        mMap=map;
    }

    @Override
    protected String doInBackground(String... strings) {
        final DynamoDBMapper dynamoDBMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();

        PinpointerCodesDO pin= dynamoDBMapper.load(PinpointerCodesDO.class, strings[0]);
        AmazonClientException lastException = null;

        String list = pin.getCoordinatelist();
        return list;
    }

    @Override
    protected void onPostExecute(String s) {
        String string = s.substring(1);
        String[] coordinates = string.split("\\|");
        Log.d("First",coordinates[0]);
        ArrayList<LatLng> list = new ArrayList<>();
        for(String c:coordinates){
            double lat = Double.parseDouble(c.substring(0,c.indexOf(',')));
            double lon = Double.parseDouble(c.substring(c.indexOf(',')+1));
            list.add(new LatLng(lat,lon));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(list.get(0),18f));
        mMap.addPolyline(new PolylineOptions()
        .addAll(list)
        .width(10)
        .color(Color.RED));
    }

}
