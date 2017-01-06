package com.ble.sharan.myUtilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brst-pc93 on 1/6/17.
 */

public class MyDatabase extends SQLiteOpenHelper
{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "stepsManager";

    // Contacts table name
    private static final String TABLE_STEP_RECORD = "steps_records";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_STEPS = "steps";


    MyUtil myUtil = new MyUtil();

    public MyDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_STEP_RECORD + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"
                + KEY_STEPS + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEP_RECORD);

        // Create tables again
        onCreate(db);
    }


    public void addData(BeanRecords beanRecords)
    {

        int ID = getIdOnDate(beanRecords.getDate());

        if (ID == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_DATE, beanRecords.getDate()); // Date
            values.put(KEY_STEPS, beanRecords.getSteps()); // Steps

            // Inserting Row
            db.insert(TABLE_STEP_RECORD, null, values);
            db.close(); // Closing database connection

        }
        else
        {
            beanRecords.setID(ID);

            updateContact(beanRecords);
        }
    }


    public int getIdOnDate(String date)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STEP_RECORD, new String[]{
                        KEY_ID,
                        KEY_DATE, KEY_STEPS
                }, KEY_DATE + "=?",
                new String[]{String.valueOf(date)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();


        if (cursor.getCount() > 0)
        {
            return Integer.parseInt(cursor.getString(0));
        }
        else
        {
            return 0;
        }
    }

    public int getTodaySteps()
    {
        int ID=getIdOnDate(myUtil.getTodaydate());
        if (ID == 0)
        {
            return 0;
        }
        else
        {
           return Integer.parseInt(getRecord(ID).getSteps());
        }
    }


    // Getting single contact
    public BeanRecords getRecord(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STEP_RECORD, new String[]{
                        KEY_ID,
                        KEY_DATE, KEY_STEPS
                }, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        BeanRecords beanRecords = new BeanRecords(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return beanRecords;
    }


    // Getting All Contacts
    public List<BeanRecords> getAllContacts()
    {
        List<BeanRecords> contactList = new ArrayList<BeanRecords>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STEP_RECORD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                BeanRecords beanRecords = new BeanRecords();
                beanRecords.setID(Integer.parseInt(cursor.getString(0)));
                beanRecords.setDate(cursor.getString(1));
                beanRecords.setSteps(cursor.getString(2));
                // Adding contact to list
                contactList.add(beanRecords);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    // Getting contacts Count
    public int getContactsCount()
    {
        String countQuery = "SELECT  * FROM " + TABLE_STEP_RECORD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    // Updating single contact
    public int updateContact(BeanRecords beanRecords)
    {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, beanRecords.getDate());
        values.put(KEY_STEPS, beanRecords.getSteps());

        // updating row
        return db.update(TABLE_STEP_RECORD, values, KEY_ID + " = ?",
                new String[]{String.valueOf(beanRecords.getID())});
    }


    // Deleting single contact

    public void deleteContact(BeanRecords beanRecords)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STEP_RECORD, KEY_ID + " = ?",
                new String[]{String.valueOf(beanRecords.getID())});
        db.close();
    }

}