package com.example.himanshijain.callback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by Himanshi Jain on 2/23/2016.
 */
public class CallReceiver extends BroadcastReceiver {

    public static String TAG="PhoneStateReceiver";
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            Log.d(TAG,"PhoneStateReceiver**Call State=" + state);

            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                Log.d(TAG,"PhoneStateReceiver**Idle");
            } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                // Incoming call
                final String incomingNumber =
                        intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d(TAG,"PhoneStateReceiver**Incoming call " + incomingNumber);
                Handler handler = new Handler();
                 handler.postDelayed(new Runnable() {
                     public void run() {
                         if(incomingNumber.equals("+911127933851"))

                         {

                             if (!killCall(context)) { // Using the method defined earlier
                                 Log.d(TAG, "PhoneStateReceiver **Unable to kill incoming call");
                             } else {
                                 Log.d(TAG, "dialing screen");
//                                 PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//                                 PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                                         "MyWakelockTag");
//                                 wakeLock.acquire();
                                 Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                 callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                 callIntent.setData(Uri.parse("tel:1127933851"));
                                 context.startActivity(callIntent);
//
//
// lease();


                             }
                         }

                     }
                 }

                     ,10000);


                 }else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    Log.d(TAG,"PhoneStateReceiver **Offhook");
            }
        } else if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            // Outgoing call
            String outgoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d(TAG, "PhoneStateReceiver **Outgoing call " + outgoingNumber);

            //setResultData(null); // Kills the outgoing call

        } else {
            Log.d(TAG,"PhoneStateReceiver **unexpected intent.action=" + intent.getAction());
        }
    }

    public boolean killCall(Context context) {
        try {
            // Get the boring old TelephonyManager
            Log.d(TAG,"PhoneStateReceiver **entered kill call" );
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            // Get the getITelephony() method
            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");
            Log.d(TAG,"PhoneStateReceiver **set accesssible" );
            // Ignore that the method is supposed to be private
            methodGetITelephony.setAccessible(true);

            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);
            Log.d(TAG,"PhoneStateReceiver **invoked" );
            // Get the endCall method from ITelephony
            Class telephonyInterfaceClass =
                    Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
            Log.d(TAG,"PhoneStateReceiver **end call method" );
            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);
            Log.d(TAG, "PhoneStateReceiver **end call called");

//            Intent callIntent = new Intent(Intent.ACTION_CALL);
//            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            callIntent.setData(Uri.parse("tel:9212442861"));
//            context.startActivity(callIntent);

        } catch (Exception ex) { // Many things can go wrong with reflection calls
            Log.d(TAG, "PhoneStateReceiver ** exception" + ex.toString());
            return false;
        }
        return true;
    }
}
