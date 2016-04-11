package com.example.himanshijain.callback;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class PhoneNumbers extends AppCompatActivity  implements View.OnClickListener{
    FloatingActionButton add;
    final int PICK_CONTACT=1;
    SharedPreferences pref;
    ContactsListHelper contactsListHelper;
    ArrayList<Contact> contacts;
    RecyclerView recyclerView;
    ContactsAdapter contactsAdapter;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_numbers);
        pref=getSharedPreferences("Prefs", MODE_PRIVATE);
        if(!pref.getBoolean("table_created",false)) {
            contactsListHelper=new ContactsListHelper(getApplicationContext());
            pref.edit().putBoolean("table_created", true);
            pref.edit().commit();
        }
        add=(FloatingActionButton)findViewById(R.id.add);
        add.setOnClickListener(this);
        contacts=new ArrayList<>();
        contacts=contactsListHelper.getAllContacts();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        contactsAdapter=new ContactsAdapter(getApplicationContext(),contacts);
        recyclerView.setAdapter(contactsAdapter);
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), "myimage");
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Callback");
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.Text_Icons));
        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(getApplicationContext(), R.color.Primary));
        collapsingToolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(getApplicationContext(), R.color.PrimaryDark));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_phone_numbers, menu);
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
        if(view==add){
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c
                                .getString(c
                                        .getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone = c
                                .getString(c
                                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver()
                                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                            null,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                    + " = " + id, null, null);
                            phones.moveToFirst();
                            String cNumber = phones
                                    .getString(phones
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            String nameContact = c
                                    .getString(c
                                            .getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                            Toast.makeText(getApplicationContext(),""+nameContact+" "+cNumber,Toast.LENGTH_SHORT).show();
                            Log.i("taken", cNumber);
                            String number=cNumber.replaceAll("\\s", "");
                            number=number.trim();
                            if(number.charAt(0)=='0')
                                number="+91"+number.substring(1);
                            else if(number.charAt(0)!='+')
                                number = "+91" + number;
                            Log.i("added", number);
                            if(contactsListHelper.addContact(new Contact(id, nameContact, number))){
                                contacts.add(new Contact(id, nameContact, number));
                                Log.i("number", id + " " + nameContact + " " + number);
                                contactsAdapter.notifyDataSetChanged();
                            }
                            phones.close();
                            c.close();
                        }
                    }
                }
                break;
        }
    }
}
