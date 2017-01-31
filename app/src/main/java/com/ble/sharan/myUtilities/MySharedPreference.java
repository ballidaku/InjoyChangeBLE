package com.ble.sharan.myUtilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by brst-pc93 on 12/20/16.
 */

public class MySharedPreference
{
    public final String PreferenceName = "MyPreference";

    public MySharedPreference()
    {
    }

    public static MySharedPreference instance = null;

    public static MySharedPreference getInstance()
    {
        if (instance == null)
        {
            instance = new MySharedPreference();
        }
        return instance;
    }

    public SharedPreferences getPreference(Context context)
    {
        return context.getSharedPreferences(PreferenceName, Activity.MODE_PRIVATE);
    }

    public void saveDeviceAddress(Context context, String deviceAddress)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.DEVICE_ADDRESS, deviceAddress);
        editor.apply();
    }


    public String getDeviceAddress(Context context)
    {
        return getPreference(context).getString(MyConstant.DEVICE_ADDRESS, "");
    }


    public void saveIsConnectedNow(Context context, boolean isConnected)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putBoolean(MyConstant.IS_CONNECTED, isConnected);
        editor.apply();
    }

    public boolean getIsConnectedNow(Context context)
    {
        return getPreference(context).getBoolean(MyConstant.IS_CONNECTED, false);
    }



    //**********************************************************************************************
    // Clear Connection Data
    //**********************************************************************************************

    public void clearConnectionData(Context context)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.remove(MyConstant.IS_CONNECTED);
        editor.remove(MyConstant.DEVICE_ADDRESS);
        editor.apply();
    }

    //**********************************************************************************************
    //**********************************************************************************************







    public void saveIsManualDisconnected(Context context, boolean isTrue)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putBoolean(MyConstant.IS_MANUAL_DISCONNECTED, isTrue);
        editor.apply();
    }

    public boolean getIsManualDisconnected(Context context)
    {
        return getPreference(context).getBoolean(MyConstant.IS_MANUAL_DISCONNECTED, false);
    }


    //**********************************************************************************************
    // ALARM
    //**********************************************************************************************


    public void saveAlarm(Context context,String alarmKey, String alarm)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();

        editor.putString(alarmKey, alarm);

        editor.apply();

        setFalseIsAlarmActivated(context,alarmKey);

    }

    // For switch
    public void setFalseIsAlarmActivated(Context context,String alarmKey)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();

        if(MyConstant.ALARM_FIRST.equals(alarmKey))
        {
            editor.putBoolean(MyConstant.IS_ALARM_FIRST_ACTIVATED, false);
        }
        else if(MyConstant.ALARM_SECOND.equals(alarmKey))
        {
            editor.putBoolean(MyConstant.IS_ALARM_SECOND_ACTIVATED, false);
        }
        else if(MyConstant.ALARM_THIRD.equals(alarmKey))
        {
            editor.putBoolean(MyConstant.IS_ALARM_THIRD_ACTIVATED, false);
        }

        editor.apply();
    }


    // For switch
    public void setTrueIsAlarmActivated(Context context,String alarmKey)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();

        if(MyConstant.ALARM_FIRST.equals(alarmKey))
        {
            editor.putBoolean(MyConstant.IS_ALARM_FIRST_ACTIVATED, true);
        }
        else if(MyConstant.ALARM_SECOND.equals(alarmKey))
        {
            editor.putBoolean(MyConstant.IS_ALARM_SECOND_ACTIVATED, true);
        }
        else if(MyConstant.ALARM_THIRD.equals(alarmKey))
        {
            editor.putBoolean(MyConstant.IS_ALARM_THIRD_ACTIVATED, true);
        }

        editor.apply();
    }


    public boolean getIsAlarmActivated(Context context, String alarmNo)
    {
       return getPreference(context).getBoolean(alarmNo,false);
    }


    public void deleteAlarm(Context context, String alarm)
    {
        setFalseIsAlarmActivated(context,alarm);

    }

    public HashMap<String,String> getAllAlarm(Context context)
    {
        HashMap<String,String> map=new HashMap<>();

        map.put(MyConstant.ALARM_FIRST,getPreference(context).getString(MyConstant.ALARM_FIRST, ""));
        map.put(MyConstant.ALARM_SECOND,getPreference(context).getString(MyConstant.ALARM_SECOND, ""));
        map.put(MyConstant.ALARM_THIRD,getPreference(context).getString(MyConstant.ALARM_THIRD, ""));

       return map;
    }


    //**********************************************************************************************
    // Pofile
    //**********************************************************************************************


    public void saveName(Context context, String name)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.NAME, name);
        editor.apply();
    }

    public String getName(Context context)
    {
        return getPreference(context).getString(MyConstant.NAME, "");
    }

    public void saveHeight(Context context, String height)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.HEIGHT, height);
        editor.apply();
    }

    public String getHeight(Context context)
    {
        return getPreference(context).getString(MyConstant.HEIGHT, "60")+" In";
    }


    public void saveWeight(Context context, String weight)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.WEIGHT, weight);
        editor.apply();
    }

    public String getWeight(Context context)
    {
        return getPreference(context).getString(MyConstant.WEIGHT, "132.277")+" "+getWeightUnit(context);
    }

    public void saveStride(Context context, String stride)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.STRIDE, stride);
        editor.apply();
    }

    public String getStride(Context context)
    {
        String abc=new DecimalFormat("##.##").format(Double.parseDouble(getPreference(context).getString(MyConstant.STRIDE, "17.7165")));
        return abc+" "+getStrideUnit(context);
    }

    public void saveGender(Context context, String gender)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.GENDER, gender);
        editor.apply();
    }

    public String getGender(Context context)
    {
        return getPreference(context).getString(MyConstant.GENDER, "");
    }


    public void savePhoto(Context context, String photo)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.PHOTO, photo);
        editor.apply();
    }

    public String getPhoto(Context context)
    {
        return getPreference(context).getString(MyConstant.PHOTO, "");
    }

    //**********************************************************************************************
    // Set Goals
    //**********************************************************************************************


    // DAILY STEPS
    public void setDailySteps(Context context, String steps)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.STEPS, steps);
        editor.apply();
    }

    public String getDailySteps(Context context)
    {
        return getPreference(context).getString(MyConstant.STEPS,"5000");
    }
    //**********************************************************************************************

    // DAILY MILES
    public void setDailyMiles(Context context, String miles)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.MILES, miles);
        editor.apply();
    }

    public String getDailyMiles(Context context)
    {
        return getPreference(context).getString(MyConstant.MILES,"2.5")+ " per day";
    }
    //**********************************************************************************************


    // DAILY CALORIES
    public void setDailyCalories(Context context, String calories)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.CALORIES, calories);
        editor.apply();
    }

    public String getDailyCalories(Context context)
    {
        return getPreference(context).getString(MyConstant.CALORIES,"1000")+" per day";
    }
    //**********************************************************************************************

    // DAILY SLEEP
    public void setDailySleep(Context context, String sleep)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.SLEEP, sleep);
        editor.apply();
    }

    public String getDailySleep(Context context)
    {
        return getPreference(context).getString(MyConstant.SLEEP,"9")+" hour per day";

    }




   public void removeGoalKeys(Context context)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.remove(MyConstant.STEPS);
        editor.remove(MyConstant.MILES);
        editor.remove(MyConstant.CALORIES);
        editor.remove(MyConstant.SLEEP);
        editor.apply();
    }
    //**********************************************************************************************

    //**********************************************************************************************
    //**********************************************************************************************
    //**********************************************************************************************



    // DISATNCE UNIT MILES KM
    public void saveDistanceUnit(Context context, String distanceUnit)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.MILES_KM, distanceUnit);
        editor.apply();
    }

    public String getDistanceUnit(Context context)
    {
        return getPreference(context).getString(MyConstant.MILES_KM,MyConstant.KM);

    }


    public void saveWeightUnit(Context context, String weightUnit)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.LBS_KG, weightUnit);
        editor.apply();
    }

    public String getWeightUnit(Context context)
    {
        return getPreference(context).getString(MyConstant.LBS_KG, MyConstant.LBS);
    }


    public void saveStrideUnit(Context context, String strideUnit)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.IN_CM, strideUnit);
        editor.apply();
    }

    public String getStrideUnit(Context context)
    {
        return getPreference(context).getString(MyConstant.IN_CM, MyConstant.IN);
    }




    //**********************************************************************************************
    // User Details
    //**********************************************************************************************

    public void saveAccessToken(Context context, String accessToken)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public String getAccessToken(Context context)
    {
        return getPreference(context).getString(MyConstant.ACCESS_TOKEN, "");
    }

}
