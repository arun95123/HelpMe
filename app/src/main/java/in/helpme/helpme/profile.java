package in.helpme.helpme;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by mvenkta on 9/25/2016.
 */

public class profile extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView name=(TextView)findViewById(R.id.name);
        TextView phone=(TextView)findViewById(R.id.phoneno);
        TextView sex=(TextView)findViewById(R.id.sex);
        TextView role=(TextView)findViewById(R.id.role);
        TextView age=(TextView)findViewById(R.id.age);

        name.setText(MainActivity.n);
        phone.setText(MainActivity.ph);
        sex.setText(MainActivity.sex);
        role.setText(MainActivity.r);
        age.setText(MainActivity.a);





    }
}
