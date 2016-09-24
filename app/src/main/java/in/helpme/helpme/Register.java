package in.helpme.helpme;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by mvenkta on 9/24/2016.
 */

public class Register extends AppCompatActivity {

    final String registerurl = "http://54.200.231.130:3004/save";


    GPSTracker gps;
    Double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText uname = (EditText) findViewById(R.id.uname);
        final EditText pword = (EditText) findViewById(R.id.password);
        final EditText name = (EditText) findViewById(R.id.name);
        final EditText phone = (EditText) findViewById(R.id.phonenumber);
        final EditText sex = (EditText) findViewById(R.id.sex);
        final EditText role = (EditText) findViewById(R.id.role);
        final EditText age = (EditText) findViewById(R.id.age);


        Button register = (Button) findViewById(R.id.register);


        gps=new GPSTracker(Register.this);


        if(gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }else{

            gps.showSettingsAlert();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name=name.getText().toString();
                String Uname=uname.getText().toString();
                String Pword=pword.getText().toString();
                String Phone=phone.getText().toString();
                String Sex=sex.getText().toString();
                String Role=role.getText().toString();
                String Age=age.getText().toString();


                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(Register.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();


                final ConnectionDetector cd = new ConnectionDetector(Register.this);
                if (cd.isConnectingToInternet()) {


                    JSONObject jsonobject;
                    final JSONParser jParser1 = new JSONParser();
                    List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                    params1.add(new BasicNameValuePair("password", Pword));
                    params1.add(new BasicNameValuePair("username", Uname));
                    params1.add(new BasicNameValuePair("name", Name));
                    params1.add(new BasicNameValuePair("phoneNo", Phone));
                    params1.add(new BasicNameValuePair("sex", Sex));
                    params1.add(new BasicNameValuePair("role", Role));
                    params1.add(new BasicNameValuePair("age", Age));



                    jsonobject = jParser1.makeHttpRequest(registerurl, "POST", params1);

                    try {
                        if (jsonobject != null) {

                            String result = jsonobject.getString("success");

                            if (result.equals("true")) {

                                Intent i = new Intent(Register.this, Login.class);
                                startActivity(i);


                            }
                        }


                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }


            } else {

                Toast.makeText(Register.this, "No Internet Connection", Toast.LENGTH_LONG).show();
            }




        }
        });

    }

}
