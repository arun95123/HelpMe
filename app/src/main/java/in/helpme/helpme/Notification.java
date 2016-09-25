package in.helpme.helpme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static in.helpme.helpme.Login.Phone;
import static in.helpme.helpme.Login.Token;

/**
 * Created by mvenkta on 9/24/2016.
 */



public class Notification extends Activity {

    final String accepturl = "http://54.200.231.130:3004/emergency/resolved";
    final String times="https://maps.googleapis.com/maps/api/directions/json";
    static int going=0;
    String apisource="12.9845664,80.2467252",apidest="13.006803,80.2449123";
    String t;
    TextView distime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        new getTime().execute();

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
        distime=(TextView)findViewById(R.id.timetoreach);
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


    private class getTime extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
           /* nDialog = new ProgressDialog(getActivity());
            nDialog.setTitle("Fetching data from server");
            nDialog.setMessage("Please Wait..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();*/
            //  Toast.makeText(getActivity().getApplicationContext(),"fetching results",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(String... args){


            JSONObject jsonobject;
            final JSONParser jParser2 = new JSONParser();
            List<NameValuePair> params2 = new ArrayList<NameValuePair>();




            params2.add(new BasicNameValuePair("origin",apisource ));
            params2.add(new BasicNameValuePair("destination",apidest ));
            params2.add(new BasicNameValuePair("key","AIzaSyCkJLY-KGzinfEEFaSexGA6ofkBsaLZyNE" ));

            jsonobject = jParser2.makeHttpRequest(times, "GET", params2);

            try{
                if(jsonobject!=null){

                    JSONArray jsonArray=jsonobject.getJSONArray("routes");

                    JSONObject temp = jsonArray.getJSONObject(0);
                    JSONObject temp2=temp.getJSONArray("legs").getJSONObject(0);
                    JSONObject temp3=temp2.getJSONObject("duration");
                     t=temp3.getString("text");
                    return true;
                }
            }catch (JSONException e){

                Log.e("Error", e.getMessage());
                e.printStackTrace();

            }




            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){
            // nDialog.dismiss();
            if(th){

                distime.setText(t);
            }else{


            }


        }
    }


}
