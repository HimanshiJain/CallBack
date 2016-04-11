package com.example.himanshijain.callback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;

/**
 * Created by Himanshi Jain on 2/27/2016.
 */
public class HangUpReceiver extends BroadcastReceiver {
    static String incomingNumber;
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_RINGING)) {

            // Phone number
            incomingNumber= intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            // Ringing state
            // This code will execute when the phone has an incoming call
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_IDLE)
                || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_OFFHOOK)) {

            // This code will execute when the call is answered or disconnected
            incomingNumber= intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if(incomingNumber!=null && incomingNumber.equals("+911127933851"))

            {



//                                 PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//                                 PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                                         "MyWakelockTag");
//                                 wakeLock.acquire();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    callIntent.setData(Uri.parse("tel:1127933851"));
                    context.startActivity(callIntent);



                }

        }


    }

}
