package in.helpme.helpme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static in.helpme.helpme.Login.Token;

/**
 * Created by mvenkta on 9/24/2016.
 */

public class IncomingSms extends BroadcastReceiver {

    GPSTracker gps;
    Double latitude,longitude;

    final String emerurl = "http://54.200.231.130:3004/emergency";

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        gps=new GPSTracker(context);

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();



                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);

                    if(senderNum.equals("+919003124651")){
                        final ConnectionDetector cd = new ConnectionDetector(context);
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
                            geocoder = new Geocoder(context, Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            //Get the current locality and send it
                            String address = addresses.get(0).getAddressLine(0);

                            SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                            String restoken=pref.getString("token",null);

                            params1.add(new BasicNameValuePair("token", restoken));
                            params1.add(new BasicNameValuePair("lat", ""+latitude));
                            params1.add(new BasicNameValuePair("long", ""+longitude));
                            params1.add(new BasicNameValuePair("home", "taramani"));

                            jsonobject = jParser1.makeHttpRequest(emerurl, "POST", params1);

                            try {
                                if (jsonobject != null) {

                                    String result = jsonobject.getString("success");

                                    if (result.equals("true")) {

                                        Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
                                    }

                                }
                            }catch (Exception e) {
                                Log.e("Error", e.getMessage());
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(context,"elsed",Toast.LENGTH_SHORT).show();
                            Intent j = new Intent();
                            j.setClass(context, Sendsms.class);
                            j.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(j);



                        }



                    }
                    else if(message.contains("100")){

                        String text[]=message.split(",");
                        JSONObject jsonobject;
                        final JSONParser jParser1 = new JSONParser();
                        List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                        if(gps.canGetLocation()) {

                            latitude = Double.valueOf(text[2]);
                            longitude = Double.valueOf(text[3]);
                        }else{

                            gps.showSettingsAlert();
                        }

                        Geocoder geocoder;
                        List<Address> addresses = null;
                        geocoder = new Geocoder(context, Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        //Get the current locality and send it
                        String address = addresses.get(0).getAddressLine(0);


                        params1.add(new BasicNameValuePair("token", text[1]));
                        params1.add(new BasicNameValuePair("lat", ""+text[2]));
                        params1.add(new BasicNameValuePair("long", ""+text[3]));
                        params1.add(new BasicNameValuePair("home", "taramani"));

                        jsonobject = jParser1.makeHttpRequest(emerurl, "POST", params1);

                        try {
                            if (jsonobject != null) {

                                String result = jsonobject.getString("success");

                                if (result.equals("true")) {

                                    Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
                                }

                            }
                        }catch (Exception e) {
                            Log.e("Error", e.getMessage());
                            e.printStackTrace();
                        }

                    }




                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}