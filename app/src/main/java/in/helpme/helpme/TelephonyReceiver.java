package in.helpme.helpme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by adithya on 24/9/16.
 */
public class TelephonyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent intent) {
// TODO Auto-generated method stub
        try {
            if (intent != null && intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
//Toast.makeText(context, "Outgoign call", 1000).show();
                String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

                AudioManager audioManager = (AudioManager)arg0.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setSpeakerphoneOn(true);
            } else {
//get the phone state
                String newPhoneState = intent.hasExtra(TelephonyManager.EXTRA_STATE) ? intent.getStringExtra(TelephonyManager.EXTRA_STATE) : null;
                Bundle bundle = intent.getExtras();

                if (newPhoneState != null && newPhoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//read the incoming call number
                    String phoneNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                   // Log.i("PHONE RECEIVER", "Telephone is now ringing " + phoneNumber);

                    Toast.makeText(arg0,"call from" + phoneNumber,Toast.LENGTH_SHORT).show();

                    final String LOG_TAG = "TelephonyAnswer";

                    TelephonyManager tm = (TelephonyManager) arg0
                            .getSystemService(Context.TELEPHONY_SERVICE);

                    try {
                        if (tm == null) {
                            // this will be easier for debugging later on
                            throw new NullPointerException("tm == null");
                        }

                        // do reflection magic
                        tm.getClass().getMethod("answerRingingCall").invoke(tm);
                    } catch (Exception e) {
                        // we catch it all as the following things could happen:
                        // NoSuchMethodException, if the answerRingingCall() is missing
                        // SecurityException, if the security manager is not happy
                        // IllegalAccessException, if the method is not accessible
                        // IllegalArgumentException, if the method expected other arguments
                        // InvocationTargetException, if the method threw itself
                        // NullPointerException, if something was a null value along the way
                        // ExceptionInInitializerError, if initialization failed
                        // something more crazy, if anything else breaks

                        // TODO decide how to handle this state
                        // you probably want to set some failure state/go to fallback
                        Log.e(LOG_TAG, "Unable to use the Telephony Manager directly.", e);
                    }

                } else if (newPhoneState != null && newPhoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//Once the call ends, phone will become idle
                    Log.i("PHONE RECEIVER", "Telephone is now idle");
                } else if (newPhoneState != null && newPhoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
//Once you receive call, phone is busy
                    Log.i("PHONE RECEIVER", "Telephone is now busy");
                }

            }

        } catch (Exception ee) {
            Log.i("Telephony receiver", ee.getMessage());
        }
    }
}