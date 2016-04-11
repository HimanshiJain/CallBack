package com.example.himanshijain.callback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Himanshi Jain on 2/27/2016.
 */
public class IncomingCallReceiver extends BroadcastReceiver {
    static String incomingNumber,name;
    ContactsListHelper contactsListHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_RINGING)) {

            // Phone number
            Log.i("call","hungup");
            contactsListHelper=new ContactsListHelper(context);
            incomingNumber= intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.i("number",incomingNumber);
            if(incomingNumber!=null && contactsListHelper.isAdded(incomingNumber))

            {


               Contact contact=contactsListHelper.getContact(incomingNumber);
//                                 PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//                                 PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                                         "MyWakelockTag");
//                                 wakeLock.acquire();
                Intent callIntent = new Intent(context,MainActivity.class);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK  );
                callIntent.putExtra("number", incomingNumber);
                callIntent.putExtra("name",contact.name);
                //callIntent.setData(Uri.parse("tel:1127933851"));
                context.startActivity(callIntent);

//                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//
//                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                        WindowManager.LayoutParams.MATCH_PARENT,
//                        WindowManager.LayoutParams.MATCH_PARENT,
//                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
//                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
//                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                        PixelFormat.TRANSPARENT);
//
//                params.height = 50;
//                params.width = 50;
//                params.format = PixelFormat.TRANSLUCENT;
//
//                params.gravity = Gravity.CENTER;
//
//                LinearLayout ly = new LinearLayout(context);
//                ly.setBackgroundColor(Color.RED);
//                ly.setOrientation(LinearLayout.VERTICAL);
//
//                wm.addView(ly, params);

            }

            // Ringing state
            // This code will execute when the phone has an incoming call
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_IDLE)
                || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_OFFHOOK)) {

            // This code will execute when the call is answered or disconnected
            incomingNumber= intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.i("call","hungup");



        }

    }
}
