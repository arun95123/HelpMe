package in.helpme.helpme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvenkta on 9/24/2016.
 */



public class Notification extends Activity {

    final String accepturl = "http://54.200.231.130:3004/emergency/resolved";
    static int going=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        Intent myIntent = getIntent();
        //String name = myIntent.getStringExtra("name");
        //String phno = myIntent.getStringExtra("phone");
        //final String eid = myIntent.getStringExtra("emergency_id");
        final String lati = myIntent.getStringExtra("lat");
        final String longi = myIntent.getStringExtra("long");

        TextView disname=(TextView)findViewById(R.id.disname);
        TextView disphone=(TextView)findViewById(R.id.disphone);
        TextView dissex=(TextView)findViewById(R.id.dissex);
        TextView disage=(TextView)findViewById(R.id.disage);
        Button accept=(Button)findViewById(R.id.accept);
        TextView disemer=(TextView)findViewById(R.id.emer);
        Button reject=(Button)findViewById(R.id.reject);
        Button location=(Button)findViewById(R.id.location);

        disname.setText("  Name: " + MainActivity.name);
        disphone.setText("  Phone No: " + MainActivity.phone);
        dissex.setText("  Sex: " + MainActivity.s);
        disage.setText("  Age: " + MainActivity.age);
        disemer.setText("  Emergency Contact: " + MainActivity.emergencycontact);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                String tok=pref.getString("token",null);
                going=1;

                JSONObject jsonobject;
                final JSONParser jParser1 = new JSONParser();
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("token", tok));
                params1.add(new BasicNameValuePair("emergency_id", MainActivity.emergency_id));


                jsonobject = jParser1.makeHttpRequest(accepturl, "POST", params1);
                Intent i=new Intent(Notification.this,Maps.class);
                i.putExtra("lat",MainActivity.lat);
                i.putExtra("lon",MainActivity.lon);
                startActivity(i);




            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Notification.this,Maps.class);
                i.putExtra("lat",lati);
                i.putExtra("lon",longi);
                startActivity(i);

            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Notification.this,MainActivity.class);
                startActivity(i);

            }
        });

    }
}
