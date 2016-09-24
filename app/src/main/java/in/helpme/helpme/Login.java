package in.helpme.helpme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import org.apache.http.NameValuePair;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvenkta on 9/24/2016.
 */

public class Login extends AppCompatActivity {

    static String Password = "";
    static String Token="";
    static String Username = "";
    static String Name = "";
    static String Phone = "";
    static String Sex = "";
    static String Role = "";
    static String Age = "";
    static String Helpline = "";
    final String loginurl = "http://54.200.231.130:3004/login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText uname = (EditText) findViewById(R.id.uname);
        final EditText pword = (EditText) findViewById(R.id.password);
        TextView register = (TextView) findViewById(R.id.register);
        Button login = (Button) findViewById(R.id.login);


        SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String islogged = pref.getString("isloggedin", null);
        if (islogged != null && islogged.equals("true")) {
            Username = pref.getString("username", null);
            Password = pref.getString("password", null);
            Name = pref.getString("name", null);
            Phone = pref.getString("phone", null);
            Sex = pref.getString("sex", null);
            Role = pref.getString("role", null);
            Age = pref.getString("age", null);
            Token=pref.getString("token",null);

            Helpline=pref.getString("helpline",null);
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);

        }

        register.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = uname.getText().toString();
                String password = pword.getText().toString();

                final ConnectionDetector cd = new ConnectionDetector(Login.this);
                if (cd.isConnectingToInternet()) {


                    JSONObject jsonobject;
                    final JSONParser jParser1 = new JSONParser();
                    List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                    params1.add(new BasicNameValuePair("password", password));
                    params1.add(new BasicNameValuePair("username", username));


                    jsonobject = jParser1.makeHttpRequest(loginurl, "POST", params1);

                    try {
                        if (jsonobject != null) {

                            String result = jsonobject.getString("success");

                            if (result.equals("true")) {

                                SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                Username = jsonobject.getString("username");
                                Name = jsonobject.getString("name");
                                Phone = jsonobject.getString("phoneNo");
                                Sex = jsonobject.getString("sex");
                                Role = jsonobject.getString("role");
                                Age = jsonobject.getString("age");
                                Token=jsonobject.getString("token");
                                Helpline=jsonobject.getString("helpline");

                                editor.putString("username", Username);
                                editor.putString("name", Name);
                                editor.putString("phone", Phone);
                                editor.putString("sex", Sex);
                                editor.putString("role", Role);
                                editor.putString("age", Age);
                                editor.putString("token", Token);
                                editor.putString("helpline", Helpline);

                                editor.putString("isloggedin", "true");
                                editor.commit();

                                Intent i = new Intent(Login.this, MainActivity.class);
                                startActivity(i);




                            }

                        }


                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }


                } else {

                    Toast.makeText(Login.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
