package com.example.himanshijain.callback;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity extends Activity implements View.OnClickListener {
Button callBack,hangUp,receive;
    String incomingNumber,contact_name;
    ActionBar bar;
    TextView contact_n;
    public static String TAG="PhoneStateReceiver";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null) {
            incomingNumber=bundle.getString("number");
            contact_name=bundle.getString("name");
            Log.i("number",incomingNumber);
            Log.i("name",contact_name);

        }

        WindowManager.LayoutParams params = getWindow().getAttributes();
//        getWindow().addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
        params.x = -10;
        params.height = 1400;
        params.width = 600;
        params.y = 1;
        callBack=(Button)findViewById(R.id.call_back);
        callBack.setOnClickListener(this);
        hangUp=(Button)findViewById(R.id.button);
        hangUp.setOnClickListener(this);
        receive=(Button)findViewById(R.id.receive);
        receive.setOnClickListener(this);
        this.getWindow().setAttributes(params);
        contact_n=(TextView)findViewById(R.id.contact_name);
        contact_n.setText(contact_name);
//        bar=getActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0288D1")));
//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT |
//                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
//                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//        params.x = -100;
//        params.height = 100;
//        params.width = 500;
//        params.y=-50;
//        params.format = PixelFormat.TRANSLUCENT;
//        //final Context ct =c;
//
//        params.gravity = Gravity.TOP;
//        params.setTitle("Testing");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view==callBack){
            String  finalNumber;
            killCall(getApplicationContext());
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            if(incomingNumber.charAt(0)=='0')
                finalNumber=incomingNumber.substring(1);
            else if(incomingNumber.charAt(0)=='+')
                finalNumber=incomingNumber.substring(3);
            else
            finalNumber=incomingNumber;
            Log.i("öncallbacknumber",""+finalNumber+" "+incomingNumber);
            callIntent.setData(Uri.parse("tel:"+incomingNumber
            ));
            startActivity(callIntent);
            Toast.makeText(getApplicationContext(),"Callbacked",Toast.LENGTH_SHORT).show();
            finish();
        }
        else if(view==hangUp){
            killCall(getApplicationContext());
            Toast.makeText(getApplicationContext(),"HangedUp",Toast.LENGTH_SHORT).show();
            finish();
        }else if(view==receive){
            Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
            i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP,
                    KeyEvent.KEYCODE_HEADSETHOOK));
            sendOrderedBroadcast(i, null);
        }
    }

    public boolean killCall(Context context) {
        try {
            // Get the boring old TelephonyManager
            Log.d(TAG, "PhoneStateReceiver **entered kill call");
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
