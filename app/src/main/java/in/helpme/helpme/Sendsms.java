package in.helpme.helpme;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;



/**
 * Created by mvenkta on 9/24/2016.
 */

public class Sendsms extends Activity {

    GPSTracker gps;
    Double latitude,longitude;
    String Phone,Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        Phone = pref.getString("helpline", null);
        Token=pref.getString("token",null);
        gps=new GPSTracker(Sendsms.this);
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
