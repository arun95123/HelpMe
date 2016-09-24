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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        Intent myIntent = getIntent();
        String name = myIntent.getStringExtra("name");
        String phno = myIntent.getStringExtra("phone");
        final String eid = myIntent.getStringExtra("emergency_id");
        final String lati = myIntent.getStringExtra("lat");
        final String longi = myIntent.getStringExtra("long");

        TextView disname=(TextView)findViewById(R.id.disname);
        TextView disphone=(TextView)findViewById(R.id.disphone);
        Button accept=(Button)findViewById(R.id.accept);
        Button reject=(Button)findViewById(R.id.reject);
        Button location=(Button)findViewById(R.id.location);

        disname.setText(name);
        disphone.setText(phno);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                String tok=pref.getString("token",null);

                JSONObject jsonobject;
                final JSONParser jParser1 = new JSONParser();
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("token", tok));
                params1.add(new BasicNameValuePair("emergency_id", eid));


                jsonobject = jParser1.makeHttpRequest(accepturl, "POST", params1);




            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
