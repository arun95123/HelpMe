package in.helpme.helpme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class emergency extends AppCompatActivity {
    GPSTracker gps;
    Double latitude,longitude;
    final String emerurl = "http://54.200.231.130:3004/emergency";
    String Phone;
    private String Token;
    TextView mTextView;
    Button cancel;
    static int poll=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        mTextView=(TextView) findViewById(R.id.timer);
        cancel  =(Button)findViewById(R.id.cancel);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poll=1;
            }
        });
        SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        gps=new GPSTracker(emergency.this);
        Phone = pref.getString("helpline", null);
        Token=pref.getString("token",null);
        final ConnectionDetector cd = new ConnectionDetector(emergency.this);
        if (cd.isConnectingToInternet()) {
            JSONObject jsonobject;
            final JSONParser jParser1 = new JSONParser();
            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            if(gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            }else{

                gps.showSettingsAlert();
            }

            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(emergency.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }


            //Get the current locality and send it
            String address = addresses.get(0).getAddressLine(0);


            String restoken=pref.getString("token",null);

            params1.add(new BasicNameValuePair("token", restoken));
            params1.add(new BasicNameValuePair("lat", ""+latitude));
            params1.add(new BasicNameValuePair("long", ""+longitude));
            params1.add(new BasicNameValuePair("home", "taramani"));
            params1.add(new BasicNameValuePair("net", "true"));

            jsonobject = jParser1.makeHttpRequest(emerurl, "POST", params1);

            try {
                if (jsonobject != null) {

                    String result = jsonobject.getString("success");

                    if (result.equals("true")) {

                        Toast.makeText(emergency.this,"Success",Toast.LENGTH_LONG).show();
                    }

                }
            }catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }else{

            if(gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            }else{

                gps.showSettingsAlert();
            }

            SmsManager smsManager = SmsManager.getDefault();
            String message= "100," + Token + "," +  latitude + "," + longitude;
            smsManager.sendTextMessage(Phone, null, message, null, null);


        }





    }



}
