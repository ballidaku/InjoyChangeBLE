package com.ble.sharan.myUtilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brst-pc93 on 1/6/17.
 */

public class MyDatabase extends SQLiteOpenHelper
{

    String TAG = MyDatabase.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "stepsManager";

    // Contacts table name
    private static final String TABLE_STEP_RECORD = "steps_records";
    private static final String TABLE_SLEEP_RECORD = "sleep_records";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_STEPS = "steps";
    private static final String KEY_SLEEP_TIME = "sleep_time";


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


        String CREATE_SLEEP_TABLE = "CREATE TABLE " + TABLE_SLEEP_RECORD + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"
                + KEY_SLEEP_TIME + " INTEGER" + ")";
        db.execSQL(CREATE_SLEEP_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEP_RECORD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLEEP_RECORD);

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
        int ID = getIdOnDate(myUtil.getTodaydate());
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
        // return contactcontact
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


    //**********************************************************************************************
    //SLEEP FUNCTIONALITY***************************************************************************
    //**********************************************************************************************


    public void addSleepData(ArrayList<HashMap<String, Long>> list)
    {

        Collections.reverse(list);

        for (int i = 0; i < list.size(); i++)
        {

            for (String date : list.get(i).keySet())
            {
                long MILLIS = list.get(i).get(date);


                int ID = getSleepIdOnDate(date);

                if (ID == 0)
                {
                    SQLiteDatabase db = this.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(KEY_DATE, date); // Date
                    values.put(KEY_SLEEP_TIME, MILLIS);

                    // Inserting Row
                    db.insert(TABLE_SLEEP_RECORD, null, values);
                    db.close(); // Closing database connection

                }
                else
                {
                    if( getSleepTime(ID) < MILLIS  )
                    {
                        updateSleepData(ID,date,MILLIS);
                    }
                }
            }

        }

    }


    public int getSleepIdOnDate(String date)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SLEEP_RECORD, new String[]{
                        KEY_ID,
                        KEY_DATE, KEY_SLEEP_TIME
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

    public long getSleepMillisOnDate(String date)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SLEEP_RECORD, new String[]{
                        KEY_ID,
                        KEY_DATE, KEY_SLEEP_TIME
                }, KEY_DATE + "=?",
                new String[]{String.valueOf(date)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();


        if (cursor.getCount() > 0)
        {
            return Long.parseLong(cursor.getString(2));
        }
        else
        {
            return 0;
        }
    }


    public long getTodaySleepTime()
    {

//        Log.e(TAG, "TodayDate---" + myUtil.getTodaydate());
        int ID = getSleepIdOnDate(myUtil.getTodaydate());

//        Log.e(TAG, "ID---" + ID);

        if (ID == 0)
        {
            return 0;
        }
        else
        {
            return getSleepTime(ID);
        }
    }


    // Updating sleep Data
    public int updateSleepData(int ID, String date, long MILLIS)
    {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        values.put(KEY_SLEEP_TIME, MILLIS);

        // updating row
        return db.update(TABLE_SLEEP_RECORD, values, KEY_ID + " = ?",
                new String[]{String.valueOf(ID)});
    }


    // Updating sleep Data
    public int updateSleepMillisOnDate(String date, long MILLIS)
    {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SLEEP_TIME, MILLIS);

        // updating row
        return db.update(TABLE_SLEEP_RECORD, values, KEY_DATE + " = ?",
                new String[]{String.valueOf(date)});
    }


    // Getting single Sleeep Time
    public long getSleepTime(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SLEEP_RECORD, new String[]{
                        KEY_ID,
                        KEY_DATE, KEY_SLEEP_TIME
                }, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();


//        BeanRecords beanRecords = new BeanRecords(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2));
        // return contact
        return Long.parseLong(cursor.getString(2));
    }


    // Getting All Contacts
    public ArrayList<HashMap<String, String>> getAllSleepData()
    {

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SLEEP_RECORD;

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

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(MyConstant.ID, cursor.getString(0));
                map.put(MyConstant.DATE, cursor.getString(1));
                map.put(MyConstant.SLEEP, cursor.getString(2));

                list.add(map);

            } while (cursor.moveToNext());
        }

        // return contact list
        return list;
    }

}