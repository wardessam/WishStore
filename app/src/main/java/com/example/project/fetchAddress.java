package com.example.project;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class fetchAddress extends IntentService {
    private ResultReceiver resultReceiver;

    public fetchAddress(){
        super("fetchAddress");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
         if(intent!=null){
             String errorMsg = "";
             resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
             Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
             if(location ==null){
                 return;
             }
             Geocoder geocoder = new Geocoder(this, Locale.getDefault());
             List<Address>addresses = null;
             try{
              addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
             } catch (Exception e){
               errorMsg = e.getMessage();
             }
             if(addresses==null ||addresses.isEmpty()){
                 deliverAddressToReceiver(Constants.FAILURE_RESULT,errorMsg);
             }
             else{
                 Address address = addresses.get(0);
                 ArrayList<String> AddressFragments = new ArrayList<>();
                 for(int i=0;i<= address.getMaxAddressLineIndex();i++){
                     AddressFragments.add(address.getAddressLine(i));
                 }
                 deliverAddressToReceiver(Constants.SUCCESS_RESULT, TextUtils.join(
                         Objects.requireNonNull(System.getProperty("line.separator")),AddressFragments
                 ));
             }
         }
    }
    private void deliverAddressToReceiver(int resultCode, String addressMessage){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, addressMessage);
        resultReceiver.send(resultCode,bundle);


    }
}
