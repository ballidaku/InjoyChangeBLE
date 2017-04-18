package com.ble.sharan.myUtilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brst-pc93 o0n 1/6/17.
 */

public class MyDatabase extends SQLiteOpenHelper
{

    String TAG = MyDatabase.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "stepsManager";

    // Contacts table name
    private static final String TABLE_STEP_RECORD = "steps_records";
    private static final String TABLE_SLEEP_RECORD = "sleep_records";
    private static final String TABLE_DAILY_GOAL_RECORD = "daily_goal_reords";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_STEPS = "steps";
    private static final String KEY_SLEEP_TIME = "sleep_time";// in millis
    private static final String KEY_UID = "uid";
    private static final String KEY_TIME_RAW_DATA = "time_raw_data";

    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_CALORIES = "calories";


    MyUtil myUtil = new MyUtil();

    public MyDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.e(TAG, " Hello ");


        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_STEP_RECORD + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"
                + KEY_STEPS + " TEXT," + KEY_UID + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);


        String CREATE_SLEEP_TABLE = "CREATE TABLE " + TABLE_SLEEP_RECORD + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"
                + KEY_SLEEP_TIME + " INTEGER," + KEY_UID + " TEXT," + KEY_TIME_RAW_DATA + " TEXT" + ")";
        db.execSQL(CREATE_SLEEP_TABLE);


        String CREATE_DAILY_GOAL_TABLE = "CREATE TABLE " + TABLE_DAILY_GOAL_RECORD + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"
                + KEY_STEPS + " TEXT," + KEY_DISTANCE + " TEXT,"
                + KEY_CALORIES + " TEXT," + KEY_SLEEP_TIME + " TEXT" + ")";
        db.execSQL(CREATE_DAILY_GOAL_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        Log.e(TAG, " Version " + oldVersion + "-----" + newVersion);

        if (newVersion > 1)
        {
            db.execSQL("ALTER TABLE " + TABLE_SLEEP_RECORD + " ADD COLUMN " + KEY_TIME_RAW_DATA + " TEXT ");
        }

        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEP_RECORD);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLEEP_RECORD);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY_GOAL_RECORD);

        // Create tables again
//        onCreate(db);
    }


    //**********************************************************************************************
    // STEP FUNCTIONALITY***************************************************************************
    //**********************************************************************************************


    public void addStepData(Context context, BeanRecords beanRecords)
    {

        int ID = getStepIdOnDate(context, beanRecords.getDate());

        if (ID == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_DATE, beanRecords.getDate()); // Date
            values.put(KEY_STEPS, beanRecords.getSteps()); // Steps
            values.put(KEY_UID, MySharedPreference.getInstance().getUID(context)); // Access Token

            // Inserting Row
            db.insert(TABLE_STEP_RECORD, null, values);
            db.close(); // Closing database connection

        }
        else
        {
            beanRecords.setID(ID);

            updateStepRecord(beanRecords);
        }
    }


    public int getStepIdOnDate(Context context, String date)
    {

        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STEP_RECORD, new String[]{KEY_ID, KEY_DATE, KEY_STEPS, KEY_UID}, KEY_DATE + "=? AND " + KEY_UID + "=?", new String[]{String.valueOf(date), MySharedPreference.getInstance().getUID(context)}, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }


        if (cursor != null && cursor.getCount() > 0)
        {
            id = Integer.parseInt(cursor.getString(0));

            cursor.close();
        }

        db.close();

        return id;

    }


    public int getTodaySteps(Context context)
    {
        int ID = getStepIdOnDate(context, myUtil.getTodaydate());

        // Log.e(TAG,"ID------"+ID+"-------Date--"+myUtil.getTodaydate());

        if (ID == 0)
        {
            return 0;
        }
        else
        {
            return Integer.parseInt(getStepRecord(ID).getSteps());
        }
    }


    // Getting single contact
    public BeanRecords getStepRecord(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STEP_RECORD, new String[]{KEY_ID, KEY_DATE, KEY_STEPS, KEY_UID}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }

        BeanRecords beanRecords = new BeanRecords(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));

        cursor.close();
        db.close();

        return beanRecords;
    }


    // Getting All Contacts
    public List<BeanRecords> getAllStepRecords(Context context)
    {
        List<BeanRecords> stepsList = new ArrayList<BeanRecords>();

//        String selectQuery = "SELECT  * FROM " + TABLE_STEP_RECORD ;

        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
        Cursor cursor = db.query(TABLE_STEP_RECORD, new String[]{KEY_ID, KEY_DATE, KEY_STEPS, KEY_UID}, KEY_UID + "=?", new String[]{MySharedPreference.getInstance().getUID(context)}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                BeanRecords beanRecords = new BeanRecords();
                beanRecords.setID(Integer.parseInt(cursor.getString(0)));
                beanRecords.setDate(cursor.getString(1));
                beanRecords.setSteps(cursor.getString(2));
//                beanRecords.setAccess_token(cursor.getString(3));

                // Adding contact to list
                stepsList.add(beanRecords);
            } while (cursor.moveToNext());
        }


        if (stepsList.size() > 1)
        {
            Collections.sort(stepsList, new Comparator<BeanRecords>()
            {
                public int compare(BeanRecords o1, BeanRecords o2)
                {
                    if (o1.getDate() == null || o2.getDate() == null)
                        return 0;
                    return convertToDate(o2.getDate()).compareTo(convertToDate(o1.getDate()));
                }
            });
        }


        // ADD date which is missing between two dates
        if (stepsList.size() > 1)
        {

            List<String> dates = myUtil.getDates(stepsList.get(stepsList.size() - 1).getDate(), stepsList.get(0).getDate());

            for (String date : dates)
            {
                boolean isDateInside = false;
                for (int i = 0; i < stepsList.size(); i++)
                {
                    if (stepsList.get(i).getDate().equals(date))
                    {
                        isDateInside = true;
                        break;
                    }
                }


                if (!isDateInside)
                {
                    BeanRecords beanRecords = new BeanRecords();
                    beanRecords.setID(0);
                    beanRecords.setDate(date);
                    beanRecords.setSteps("0");
//                beanRecords.setAccess_token(cursor.getString(3));

                    // Adding contact to list
                    stepsList.add(beanRecords);


//                    Log.e(TAG, "--MissingStepsDate---" + date);
                }
            }
        }

        if (stepsList.size() > 1)
        {
            Collections.sort(stepsList, new Comparator<BeanRecords>()
            {
                public int compare(BeanRecords o1, BeanRecords o2)
                {
                    if (o1.getDate() == null || o2.getDate() == null)
                        return 0;
                    return convertToDate(o2.getDate()).compareTo(convertToDate(o1.getDate()));
                }
            });
        }

        cursor.close();
        db.close();


        // return contact list
        return stepsList;
    }


    // Getting contacts Count
    public int getContactsCount()
    {
        String countQuery = "SELECT  * FROM " + TABLE_STEP_RECORD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        // return count
        return count;
    }


    // Updating single contact
    public void updateStepRecord(BeanRecords beanRecords)
    {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, beanRecords.getDate());
        values.put(KEY_STEPS, beanRecords.getSteps());

        // updating row
        db.update(TABLE_STEP_RECORD, values, KEY_ID + " = ?", new String[]{String.valueOf(beanRecords.getID())});

        db.close();
    }


    // Deleting single contact

    public void deleteContact(BeanRecords beanRecords)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STEP_RECORD, KEY_ID + " = ?", new String[]{String.valueOf(beanRecords.getID())});
        db.close();
    }


    //**********************************************************************************************
    //SLEEP FUNCTIONALITY***************************************************************************
    //**********************************************************************************************


    public void addSleepData(Context context, ArrayList<HashMap<String, Long>> list)
    {

//        Collections.reverse(list);

        for (int i = 0; i < list.size(); i++)
        {

            for (String date : list.get(i).keySet())
            {
                long MILLIS = list.get(i).get(date);


                int ID = getSleepIdOnDate(context, date);

                if (ID == 0)
                {
//                    SQLiteDatabase db = this.getWritableDatabase();
//
//                    ContentValues values = new ContentValues();
//                    values.put(KEY_DATE, date); // Date
//                    values.put(KEY_SLEEP_TIME, MILLIS);
//                    values.put(KEY_UID, MySharedPreference.getInstance().getAccessToken(context)); // Access Token
//
//                    // Inserting Row
//                    db.insert(TABLE_SLEEP_RECORD, null, values);
//                    db.close(); // Closing database connection

                    addSleepDataOnDate(context, date, MILLIS);

                }
                else
                {
//                    if( getSleepTime(ID) < MILLIS  )
//                    {
                    updateSleepData(ID, date, MILLIS);
//                    }
                }
            }

        }

    }

    public void addSleepData2(Context context, ArrayList<HashMap<String, Object>> list)
    {

//        Collections.reverse(list);

        for (int i = 0; i < list.size(); i++)
        {

            //for (String date : list.get(i).keySet())
            // {
            long MILLIS = Long.parseLong(list.get(i).get(MyConstant.TOTAL_MILLIS).toString());
            String date = list.get(i).get(MyConstant.DATE).toString();
            String timeRawData = list.get(i).get(MyConstant.TIME_RAW_DATA).toString();


            int ID = getSleepIdOnDate(context, date);

            if (ID == 0)
            {
                addSleepDataOnDate2(context, date, MILLIS, timeRawData);
            }
            else
            {
                updateSleepData2(ID, date, MILLIS, timeRawData);
            }
            //}

        }

    }


    public void addSleepDataOnDate(Context context, String date, long MILLIS)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date); // Date
        values.put(KEY_SLEEP_TIME, MILLIS);
        values.put(KEY_UID, MySharedPreference.getInstance().getUID(context)); // Access Token

        // Inserting Row
        db.insert(TABLE_SLEEP_RECORD, null, values);
        db.close(); // Closing database connection
    }

    public void addSleepDataOnDate2(Context context, String date, long MILLIS, String timeRawData)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date); // Date
        values.put(KEY_SLEEP_TIME, MILLIS);
        values.put(KEY_UID, MySharedPreference.getInstance().getUID(context)); // Access Token
        values.put(KEY_TIME_RAW_DATA, timeRawData);

        // Inserting Row
        db.insert(TABLE_SLEEP_RECORD, null, values);
        db.close(); // Closing database connection
    }


    public int getSleepIdOnDate(Context context, String date)
    {

        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SLEEP_RECORD, new String[]{KEY_ID, KEY_DATE, KEY_SLEEP_TIME, KEY_UID}, KEY_DATE + "=? AND " + KEY_UID + "=?", new String[]{String.valueOf(date), MySharedPreference.getInstance().getUID(context)}, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }


        if (cursor != null && cursor.getCount() > 0)
        {
            id = Integer.parseInt(cursor.getString(0));
            cursor.close();
        }

        db.close();

        return id;
    }

    public long getSleepMillisOnDate(String date)
    {
        long sleepMillis = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SLEEP_RECORD, new String[]{KEY_ID, KEY_DATE, KEY_SLEEP_TIME}, KEY_DATE + "=?", new String[]{String.valueOf(date)}, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }


        if (cursor != null && cursor.getCount() > 0)
        {
            sleepMillis = Long.parseLong(cursor.getString(2));
            cursor.close();
        }

        db.close();

        return sleepMillis;
    }

    public String getRawSleepDataOnDate(String date)
    {
        String  sleepRawData = "";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SLEEP_RECORD, new String[]{KEY_ID, KEY_DATE, KEY_SLEEP_TIME,KEY_TIME_RAW_DATA}, KEY_DATE + "=?", new String[]{String.valueOf(date)}, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }


        if (cursor != null && cursor.getCount() > 0)
        {
            sleepRawData = cursor.getString(3);
            cursor.close();
        }

        db.close();

        return sleepRawData;
    }




    public long getTodaySleepTime(Context context)
    {

        //Log.e(TAG, "TodaySleepDate---" + myUtil.getTodaydate());
        int ID = getSleepIdOnDate(context, myUtil.getTodaydate());

        //Log.e(TAG, "ID---" + ID);

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
    public void updateSleepData(int ID, String date, long MILLIS)
    {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        values.put(KEY_SLEEP_TIME, MILLIS);

        // updating row
        db.update(TABLE_SLEEP_RECORD, values, KEY_ID + " = ?", new String[]{String.valueOf(ID)});

        db.close();
    }

    // Updating sleep Data
    public void updateSleepData2(int ID, String date, long MILLIS,String timeRawData)
    {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        values.put(KEY_SLEEP_TIME, MILLIS);
        values.put(KEY_TIME_RAW_DATA, timeRawData);

        // updating row
        db.update(TABLE_SLEEP_RECORD, values, KEY_ID + " = ?", new String[]{String.valueOf(ID)});

        db.close();
    }


    // Updating sleep Data
    public void updateSleepMillisOnDate(String date, long MILLIS)
    {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SLEEP_TIME, MILLIS);

        // updating row
        db.update(TABLE_SLEEP_RECORD, values, KEY_DATE + " = ?", new String[]{String.valueOf(date)});

        db.close();
    }


    // Getting single Sleeep Time
    public long getSleepTime(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SLEEP_RECORD, new String[]{KEY_ID, KEY_DATE, KEY_SLEEP_TIME}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();


        long sleepTime = Long.parseLong(cursor.getString(2));

        cursor.close();
        db.close();

        return sleepTime;
    }


    // Getting All Sleep Data
    public ArrayList<HashMap<String, String>> getAllSleepData(Context context)
    {

        ArrayList<HashMap<String, String>> list = new ArrayList<>();


        // If there is no sleep data of Today date
        if (getSleepIdOnDate(context, myUtil.getTodaydate()) == 0)
        {
            addSleepDataOnDate(context, myUtil.getTodaydate(), 0);
        }


        SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor = db.query(TABLE_SLEEP_RECORD, new String[]{KEY_ID, KEY_DATE, KEY_SLEEP_TIME, KEY_UID}, KEY_UID + "=?", new String[]{MySharedPreference.getInstance().getUID(context)}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(MyConstant.ID, cursor.getString(0));
                map.put(MyConstant.DATE, cursor.getString(1));
                map.put(MyConstant.SLEEP, cursor.getString(2));
//                map.put(MyConstant.ACCESS_TOKEN, cursor.getString(3));

                list.add(map);

            } while (cursor.moveToNext());
        }

        if (list.size() > 0)
        {
            Collections.sort(list, new Comparator<HashMap<String, String>>()
            {
                public int compare(HashMap<String, String> o1, HashMap<String, String> o2)
                {
                    if (o1.get(MyConstant.DATE) == null || o2.get(MyConstant.DATE) == null)
                        return 0;
                    return convertToDate(o2.get(MyConstant.DATE)).compareTo(convertToDate(o1.get(MyConstant.DATE)));
                }
            });
        }


        // ADD date which is missing between two dates
        if (list.size() > 1)
        {

            List<String> dates = myUtil.getDates(list.get(list.size() - 1).get(MyConstant.DATE), list.get(0).get(MyConstant.DATE));

            for (String date : dates)
            {
                boolean isDateInside = false;
                for (int i = 0; i < list.size(); i++)
                {
                    if (list.get(i).get(MyConstant.DATE).equals(date))
                    {
                        isDateInside = true;
                        break;
                    }
                }


                if (!isDateInside)
                {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(MyConstant.ID, "0");
                    map.put(MyConstant.DATE, date);
                    map.put(MyConstant.SLEEP, "0");

                    list.add(map);


                    // Log.e(TAG, "--MissingDate---" + date);
                }
            }
        }


        if (list.size() > 1)
        {
            Collections.sort(list, new Comparator<HashMap<String, String>>()
            {
                public int compare(HashMap<String, String> o1, HashMap<String, String> o2)
                {
                    if (o1.get(MyConstant.DATE) == null || o2.get(MyConstant.DATE) == null)
                        return 0;
                    return convertToDate(o2.get(MyConstant.DATE)).compareTo(convertToDate(o1.get(MyConstant.DATE)));
                }
            });
        }

        cursor.close();
        db.close();

        return list;
    }


    //**********************************************************************************************
    //MY GOAL FUNCTIONALITY*************************************************************************
    //**********************************************************************************************

    // Add data first time in case to show graph, only added if there is no data
    public void addDailyGoalDataFirstTime(Context context)
    {

        if (getAllGoalData().size() == 0)
        {
            HashMap<String, String> map = new HashMap<>();
            map.put(MyConstant.STEPS, MySharedPreference.getInstance().getDailySteps(context));
            map.put(MyConstant.DISTANCE, MySharedPreference.getInstance().getDailyMiles(context)/*.replace("miles per day", "")*/.trim());
            map.put(MyConstant.CALORIES, MySharedPreference.getInstance().getDailyCalories(context)/*.replace("per day", "")*/.trim());
            map.put(MyConstant.SLEEP, MySharedPreference.getInstance().getDailySleep(context)/*.replace("hours per day", "")*/.trim());

            addDailyGoalData(map);
        }
    }


    public void addDailyGoalData(HashMap<String, String> map)
    {


        int ID = getDailyGoalIdOnDate(myUtil.getTodaydate());


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, myUtil.getTodaydate()); // Date
        values.put(KEY_STEPS, map.get(MyConstant.STEPS)); // Steps
        values.put(KEY_DISTANCE, map.get(MyConstant.DISTANCE)); // Distance
        values.put(KEY_CALORIES, map.get(MyConstant.CALORIES)); // Calories
        values.put(KEY_SLEEP_TIME, map.get(MyConstant.SLEEP)); // Sleep Time


//        Log.e(TAG, "----addDailyGoalData----ID---" + ID);

        if (ID == 0)
        {
            // Inserting Row
            db.insert(TABLE_DAILY_GOAL_RECORD, null, values);
        }
        else
        {
            // Updating Row
            db.update(TABLE_DAILY_GOAL_RECORD, values, KEY_ID + " = ?", new String[]{String.valueOf(ID)});
        }

        db.close(); // Closing database connection
    }


    public int getDailyGoalIdOnDate(String date)
    {

        // Log.e(TAG,"-----getDailyGoalIdOnDate------DATE-----"+date);

        int id = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DAILY_GOAL_RECORD, new String[]{KEY_ID}, KEY_DATE + " = ?", new String[]{String.valueOf(date)}, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }


        if (cursor != null && cursor.getCount() > 0)
        {
            id = Integer.parseInt(cursor.getString(0));
            cursor.close();
        }


        db.close();

        return id;

    }

    // Getting  Goal Data on date
    public HashMap<String, String> getGoalDataOnDate(String date)
    {
        HashMap<String, String> map = new HashMap<String, String>();


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_DAILY_GOAL_RECORD, new String[]{KEY_ID, KEY_DATE, KEY_STEPS, KEY_DISTANCE, KEY_CALORIES, KEY_SLEEP_TIME}, KEY_DATE + "=?", new String[]{date}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                map.put(MyConstant.ID, cursor.getString(0));
                map.put(MyConstant.DATE, cursor.getString(1));
                map.put(MyConstant.STEPS, cursor.getString(2));
                map.put(MyConstant.DISTANCE, cursor.getString(3));
                map.put(MyConstant.CALORIES, cursor.getString(4));
                map.put(MyConstant.SLEEP, cursor.getString(5));

                // Adding contact to list

            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();

        return map;

    }


    //get data of previous date if exists
    public HashMap<String, String> getGoalDataOnDateIfExistsOrNot(String date)
    {
        HashMap<String, String> map = new HashMap<>();

        String currentWeekDate = date;


        // Previous date of the oldest date if previous date equals to oldest date i.e we get
        String oldestDateInDatabase = getOldestDateInDailyGoalRecord();
        String previousDateOfOldestDate = myUtil.getPreviousDate(oldestDateInDatabase);

        //   Log.e(TAG, "----Oldest Date----" + getOldestDateInDailyGoalRecord()+"-----oldestDate----"+previousDateOfOldestDate+"-----currentWeekDate date-----"+currentWeekDate);


        if (myUtil.convertStringToDate(currentWeekDate).before(myUtil.convertStringToDate(oldestDateInDatabase)))
        {
            map.putAll(getGoalDataOnDate(oldestDateInDatabase));
            //  Log.e(TAG, "---------Database 1");


        }
        else
        {
            while (!previousDateOfOldestDate.equals(currentWeekDate))
            {

                if (getDailyGoalIdOnDate(currentWeekDate) == 0)
                {
                    currentWeekDate = myUtil.getPreviousDate(currentWeekDate);
                    // Log.e(TAG, "---------Database 2");
                }
                else
                {

                    //  Log.e(TAG, "---------Database DATA--------" + getGoalDataOnDate(currentWeekDate));
                    map.putAll(getGoalDataOnDate(currentWeekDate));
                    // Log.e(TAG, "---------Database 3");
                    break;
                }
            }

        }


        return map;

    }


    // Getting all Goal Data
    public ArrayList<HashMap<String, String>> getAllGoalData()
    {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_DAILY_GOAL_RECORD, new String[]{KEY_ID, KEY_DATE, KEY_STEPS, KEY_DISTANCE, KEY_CALORIES, KEY_SLEEP_TIME}, null, null, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {

                HashMap<String, String> map = new HashMap<String, String>();

                map.put(MyConstant.ID, cursor.getString(0));
                map.put(MyConstant.DATE, cursor.getString(1));
                map.put(MyConstant.STEPS, cursor.getString(2));
                map.put(MyConstant.DISTANCE, cursor.getString(3));
                map.put(MyConstant.CALORIES, cursor.getString(4));
                map.put(MyConstant.SLEEP, cursor.getString(5));

                list.add(map);

                // Adding contact to list

            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();

        return list;

    }


    // To get oldest date
    public String getOldestDateInDailyGoalRecord()
    {
        String oldestDate = "";

        ArrayList<HashMap<String, String>> list = getAllGoalData();

        if (list.size() > 0)
        {

            Collections.sort(list, new Comparator<HashMap<String, String>>()
            {
                public int compare(HashMap<String, String> o1, HashMap<String, String> o2)
                {
                    if (o1.get(MyConstant.DATE) == null || o2.get(MyConstant.DATE) == null)
                        return 0;

                    return convertToDate(o1.get(MyConstant.DATE)).compareTo(convertToDate((o2.get(MyConstant.DATE))));
                }
            });
        }

        if (list.size() > 0)
        {
            oldestDate = list.get(0).get(MyConstant.DATE);
        }

        return oldestDate;

    }

    // Convert String to Date
    public Date convertToDate(String date)
    {

        Date d = null;

        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try
        {
            d = formatter.parse(date);
        } catch (ParseException e)
        {

            e.printStackTrace();
        }

        return d;

    }


}