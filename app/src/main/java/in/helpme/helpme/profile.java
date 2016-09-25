package in.helpme.helpme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by mvenkta on 9/25/2016.
 */

public class profile extends Activity {
int call=0;
    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView name=(TextView)findViewById(R.id.name);
        final TextView phone=(TextView)findViewById(R.id.phoneno);
        TextView sex=(TextView)findViewById(R.id.sex);
        TextView role=(TextView)findViewById(R.id.role);
        TextView age=(TextView)findViewById(R.id.age);
        Button bt =(Button)findViewById(R.id.can);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           call=1;

                ((TextView)findViewById(R.id.lable)).setText("Call to helper canceled");
            }


        });

        name.setText("  Name: " +MainActivity.n);
        phone.setText("  Phone Numer: "+MainActivity.ph);
        sex.setText("  Sex: "  + MainActivity.sex);
        role.setText("  Role: " + MainActivity.r);
        age.setText("  Age: " + MainActivity.a);


        thread=  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        wait(5000);

                        Intent phoneIntent = new Intent(Intent.ACTION_CALL);

                        phoneIntent.setData(Uri.parse("tel:" + MainActivity.ph));
                        try{
                            if(call==0)
                            startActivity(phoneIntent);
                            else
                                call=0;

                        }

                        catch (android.content.ActivityNotFoundException ex){
                            Toast.makeText(getApplicationContext(),"Placing call to helper",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                catch(InterruptedException ex){
                }

                // TODO
            }
        };

        thread.start();




    }
}
