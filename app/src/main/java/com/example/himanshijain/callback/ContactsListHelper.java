package com.example.himanshijain.callback;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Himanshi Jain on 2/28/2016.
 */
public class ContactsListHelper extends SQLiteOpenHelper {

    Context context;
    Activity activity;
    public static final String DATABASE_NAME = "contact_list";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_CONTACTS = "contacts";
    public static final String CONTACT_ENTRY_ID = "_id";
    public static final String CONTACT_NAME_TITLE = "title";
    public static final String CONTACT_NAME_PHONE_NUMBER = "edition";
    SharedPreferences sharedPreferences;

    public ContactsListHelper(Context context,Activity activity) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
        this.activity=activity;
    }

    public ContactsListHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sharedPreferences=context.getSharedPreferences("Prefs",context.MODE_PRIVATE);
        if(!sharedPreferences.getBoolean("table_created",false)){
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                    + CONTACT_ENTRY_ID + " INTEGER PRIMARY KEY," + CONTACT_NAME_TITLE + " TEXT,"
                    + CONTACT_NAME_PHONE_NUMBER + " TEXT"+ ")";
            sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
            Log.i("Table","Created "+CREATE_CONTACTS_TABLE);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(sqLiteDatabase);
    }

    public boolean addContact(Contact contact) {
        if(!isAdded(contact.phone_no)) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(CONTACT_ENTRY_ID, contact.id);
            values.put(CONTACT_NAME_TITLE, contact.name);
            values.put(CONTACT_NAME_PHONE_NUMBER, contact.phone_no);
            // Inserting Row
            db.insert(TABLE_CONTACTS, null, values);

            db.close(); // Closing database connection
            return true;
        }else{
            Toast.makeText(context,"Contact "+contact.name+" Already in the list",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void deleteContact(String book_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("delete", "preparing");
        db.delete(TABLE_CONTACTS, CONTACT_ENTRY_ID + " = ?",
                new String[]{String.valueOf(book_id)});
        Log.i("delete", "done");
    }

    public ArrayList getAllContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Contact> contacts=new ArrayList<>();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { CONTACT_ENTRY_ID,
                        CONTACT_NAME_TITLE, CONTACT_NAME_PHONE_NUMBER }, null,
                null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if(cursor.moveToFirst()) {
            Contact contact = new Contact(cursor.getString(cursor.getColumnIndex(CONTACT_ENTRY_ID)), cursor.getString(cursor.getColumnIndex(CONTACT_NAME_TITLE)),
                    cursor.getString(cursor.getColumnIndex(CONTACT_NAME_PHONE_NUMBER)));
            contacts.add(contact);
        }
//        Log.i("number", cursor.getString(cursor.getColumnIndex(CONTACT_NAME_PHONE_NUMBER)));


        while(cursor.moveToNext()){
            Contact contacti=new Contact(cursor.getString(cursor.getColumnIndex(CONTACT_ENTRY_ID)),cursor.getString(cursor.getColumnIndex(CONTACT_NAME_TITLE)),
                    cursor.getString(cursor.getColumnIndex(CONTACT_NAME_PHONE_NUMBER)));
            Log.i("number",cursor.getString(cursor.getColumnIndex(CONTACT_NAME_PHONE_NUMBER)));
            contacts.add(contacti);
        };
        return contacts;
    }

    public Contact getContact(String phone_no){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{CONTACT_NAME_TITLE,CONTACT_ENTRY_ID,CONTACT_NAME_PHONE_NUMBER},
                CONTACT_NAME_PHONE_NUMBER + "=?", new String[]{phone_no},
                null, null, null);
        Contact contact;
        if(cursor.moveToFirst()){
            contact = new Contact(cursor.getString(cursor.getColumnIndex(CONTACT_ENTRY_ID)), cursor.getString(cursor.getColumnIndex(CONTACT_NAME_TITLE)),
                    cursor.getString(cursor.getColumnIndex(CONTACT_NAME_PHONE_NUMBER)));
        }else{
            contact=new Contact();
        }
        return contact;
    }

    public boolean isAdded(String number){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("delete", "preparing");

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{CONTACT_NAME_PHONE_NUMBER}, CONTACT_NAME_PHONE_NUMBER+"=?",
                new String[]{String.valueOf(number)}, null, null, null, null);
        if(cursor.moveToFirst()){
            Log.i("isContact", "present " + cursor.getString(cursor.getColumnIndex(CONTACT_NAME_PHONE_NUMBER)));

            return true;
        }
        else
            return false;
    }
}
