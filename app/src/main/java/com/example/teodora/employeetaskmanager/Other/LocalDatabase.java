package com.example.teodora.employeetaskmanager.Other;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.teodora.employeetaskmanager.Models.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class LocalDatabase extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "firebaseManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "Contacts";

    // Contacts table columns names
    private static final String KEY_CONTACT_EMAIL = "contactEmail";
    private static final String KEY_CONTACT_NAME = "contactName";
    private static final String KEY_CONTACT_PHONE = "contactPhone";

    private static final String CREATE_CONTACTS_TABLE = "CREATE TABLE Contacts " +
            "(contactEmail TEXT NOT NULL, contactName TEXT NOT NULL, contactPhone TEXT)";

    public LocalDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("Database operations","Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
        Log.d("Database operations","Table contacts created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        Log.d("Database operations","Table contacts dropped");

        // Create tables again
        onCreate(sqLiteDatabase);
        Log.d("Database operations","Database created again!");

    }

    // ------------------------ "TABLE_CONTACTS" methods ----------------//

    // Adding new contact
    public void addContact(ContactModel contactModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_EMAIL, contactModel.getEmail());
        values.put(KEY_CONTACT_NAME, contactModel.getName());
        values.put(KEY_CONTACT_PHONE, contactModel.getMobilePhone());

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }


    // Getting single contact
    public ContactModel getContact(String contactEmail) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {KEY_CONTACT_NAME, KEY_CONTACT_EMAIL, KEY_CONTACT_PHONE}, KEY_CONTACT_EMAIL + "=?",
                new String[] { contactEmail }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        ContactModel contact = new ContactModel(cursor.getString(0),
                cursor.getString(2), cursor.getString(1));
        cursor.close();
        // return contact
        return contact;
    }

    // Getting All Contacts
    public List<ContactModel> getAllContacts() {
        List<ContactModel> contactList = new ArrayList<ContactModel>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor!=null){
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactModel contactModel = new ContactModel();
                    contactModel.setEmail(cursor.getString(0));
                    contactModel.setName(cursor.getString(1));
                    contactModel.setMobilePhone(cursor.getString(2));
                    // Adding contact to list
                    contactList.add(contactModel);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }

        // return contact list
        return contactList;
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single contact
    public int updateContact(ContactModel contactModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_EMAIL, contactModel.getEmail());
        values.put(KEY_CONTACT_NAME, contactModel.getName());
        values.put(KEY_CONTACT_PHONE, contactModel.getMobilePhone());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_CONTACT_EMAIL + " = ?",
                new String[] { contactModel.getEmail() });
    }


    // Deleting single contact
    public void deleteContact(ContactModel contactModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_CONTACT_EMAIL + " = ?",
                new String[] { contactModel.getEmail() });
        db.close();
    }

    // Delete all contacts
    public void deleteContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.close();
    }


}
