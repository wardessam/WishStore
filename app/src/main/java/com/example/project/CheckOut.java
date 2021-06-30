package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.MessagingException;
import  javax.mail.Transport;
import javax.mail.internet.InternetAddress;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;
import static com.example.project.SignIn.*;
import static javax.mail.internet.InternetAddress.*;

public class CheckOut extends AppCompatActivity {
    TextView subtotal;
    TextView totalamount;
    String orderLocation;
    String sEmail,sPassword;
    public TextView location22;
    Button getGps, getGMap, placeOrder;
    SQLiteDBHelper db;
    float total = 0;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private AddressResultReceiver addressResultReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out);
        subtotal = (TextView) findViewById(R.id.subtotal);
        totalamount = (TextView) findViewById(R.id.totalamount);
        location22 = (TextView) findViewById(R.id.location);
        getGps = (Button) findViewById(R.id.searchgps);
        getGMap = (Button) findViewById(R.id.searchGMap);
        placeOrder = (Button) findViewById(R.id.PlaceOrder);
        db = new SQLiteDBHelper(this);
        //Toast.makeText(this,GoogleMap.locationByMaps,Toast.LENGTH_SHORT).show();
        for (int i = 0; i < ProductsAdapter.products.size(); i++) {
            String[] strings = ProductsAdapter.products.get(i).getPrice().split(" ");
            total += Float.parseFloat(strings[0]);
        }
        subtotal.setText(String.valueOf(total) + " L.E.");
        totalamount.setText(String.valueOf(total + 60) + " L.E.");

        getGMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CheckOut.this,MapsActivity.class);
                startActivity(i);
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                Order o = new Order(date, CustID,location22.getText().toString(),ProductsAdapter.products);
                db.MakeOrder(o);
                String OrderID = db.getOrdID(o);
                for(int i=0;i<o.getProducts().size();i++){
                  db.OrderDetails(OrderID,o.getProducts().get(i).getID(),o.getProducts().get(i).getQuantity());
                }
                sendMail();
                for(int i=0;i<ProductsAdapter.products.size();i++){
                      db.SetNewQuantitiesAfterEveryOrder(ProductsAdapter.products.get(i));
                    }
                ProductsAdapter.products.clear();
                startActivity(new Intent(CheckOut.this,FinalActivity.class));

            }
        });
        addressResultReceiver = new AddressResultReceiver(new Handler());
        getGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CheckOut.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
                } else {
                    getCurrentlocation();
                }
            }


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
       location22.setText(MapsActivity.locationByMaps);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                getCurrentlocation();
            }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentlocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(30000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(CheckOut.this).requestLocationUpdates(
                locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(CheckOut.this).removeLocationUpdates(this);
                        if (locationRequest != null && locationResult.getLocations().size() > 0) {
                            int latestlocationindex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(latestlocationindex).getLatitude();
                            double longtiude = locationResult.getLocations().get(latestlocationindex).getLongitude();
                           // location22.setText(String.format("Latitude: %s\nLongitude: %s", latitude, longtiude));
                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longtiude);
                            fetchAddressFromLatlong(location);
                        }
                    }
                }, Looper.getMainLooper());

    }

private void fetchAddressFromLatlong(Location location){
    Intent intent = new Intent(this,fetchAddress.class);
    intent.putExtra(Constants.RECEIVER,addressResultReceiver);
    intent.putExtra(Constants.LOCATION_DATA_EXTRA,location);
    startService(intent);
}
    private class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                location22.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            } else {
                Toast.makeText(CheckOut.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void sendMail(){
        sEmail= "WishStoreMail@gmail.com";
        sPassword = "WishStore9911";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sEmail,sPassword);
            }
        });

        try {

            Message message = new MimeMessage(session);
            //sender email
            message.setFrom(new InternetAddress(sEmail));
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(CustEmail));
            message.setSubject("WISH STORE ORDER");
            message.setText("Your Order is Submitted Successfully, it should be delivered within 7 days, " +
                    "Thanks for using Wish Store.");
            new sendMailClass().execute(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private class sendMailClass extends AsyncTask<Message,String,String> {
       // private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  progressDialog = ProgressDialog.show(CheckOut.this,"Please Wait","Confirming Order..",true);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
          //  progressDialog.dismiss();
        }
    }
}