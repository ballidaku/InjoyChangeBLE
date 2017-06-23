package com.ble.sharan.myUtilities;

import android.content.Context;

import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.mainScreen.fragments.mainFragments.Today;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by brst-pc93 on 3/22/17.
 */

public class ManupulateSleepdata
{

    Context context;

    MyUtil myUtil=new MyUtil();

    public void gettingSleepData(Context context, String remaining, MyDatabase myDatabase)
    {

        this.context=context;

//   String remaining = "0000000000110105001800A00023000F002500080101000901020004010F000C0110001D01120018011300140136000F0137000C0200000B0204000902050011020B00070212000902190007021A000A0220000F02270018022A0005022B0007022D000B0305000103080001030D000B0318000C03190045031A0001031E0014033300180339001C04030002040400020405000B040800100411000B0418000B041B000F041C002D041D000B0000000000110105051D002C0523001A05250004053000060533000B0604001B06070019060D001C061C000F061D0008061F000106320050FFFFFFFFFFFFFFFFFFFFFFFF";
//        String remaining = "0000000000110105161D000C161E001A161F0040162000220000000000110106011C000C01330011013400100135000D0000000000110106020B001002160011031A00010336000A04070050000000000011010605070044052C001205320023053700190601000606080007060E000506110001061A0002061B001106220008062800020708002D0714000A071F002B073700080738000B07390017000000000011010B1611000416370014000000000011010C16090008160F000117170008000000000011010D1108000411110050000000000011010D1203007412040001120500021206000212080002120A0002120B0003120C0001120E0001120F000212100002122800141229004E122C0002123000091235000112360001130000011302000213030001130500071311000113140001131A0001132B0001132C0001132E000113300001133A0002140A0050000000000011010E093400040A080050000000000011010F0410007C04260008042F000104390011051E0018051F001E060A0022060B0004060D0031060E000C061800150619004D061A0001062E0001070700180708000F072200140726000E080D0010080F00070810000108110002081200100813000608170006081800240819000108250015082700020828001408290012082A004000000000001101100217000C022E002E022F00120230003100000000001101100312000C03290034032A001E032B000A0000000000110110052A0040062A00170630001506380014071D0010072200050723001E072B000207390005080B0006081A0002081B001F081C000309080019092C00180930000A09310050FFFFFFFFFFFFFFFFFFFFFFFF2130783030303030303237";

//        String remaining ="000000000011010D1203007412040001120500021206000212080002120A0002120B0003120C0001120E0001120F000212100002122800141229004E122C0002123000091235000112360001130000011302000213030001130500071311000113140001131A0001132B0001132C0001132E000113300001133A0002140A0050";


        ArrayList<HashMap<String, Long>> list = new ArrayList<>();

        while (remaining.startsWith("0000000000"))
        {
            if (remaining.substring(0, 10).equals("0000000000"))
            {
                String realData = remaining.substring(10, remaining.length());

                // MyUtil.myLog(TAG,"Reamining Total String  "+ realData);


                String date = "";
                String startTime = "";
                String endTime = "";

                int totalBytes;

                date = hTD(realData.substring(0, 2)) + "-" + hTD(realData.substring(2, 4)) + "-" + hTD(realData.substring(4, 6));
                startTime = hTD(realData.substring(6, 8)) + ":" + hTD(realData.substring(8, 10));


                //MyUtil.myLog(TAG, "My Bytes  "+hTD(realData.substring(10, 12))+"    "+hTD(realData.substring(12, 14)));
                // MyUtil.myLog(TAG, "My Bytes  "+hTD("010C"));

//                totalBytes = Integer.parseInt(realData.substring(10, 12), 16) + Integer.parseInt(realData.substring(12, 14), 16);
                totalBytes = Integer.parseInt(realData.substring(10, 14), 16);

                // MyUtil.myLog(TAG,"Date   "+date+"  StartTime  "+startTime+"   totalBytes   "+totalBytes);

                int last = totalBytes * 2;
                String remainingLast = realData.substring(14, 14 + last);

                //  MyUtil.myLog("remainingLast", "" + remainingLast);

                int f = last + 24;

                //MyUtil.myLog("fffffffffffff", "" + f + "--------" + remaining.substring(0, f));

                int count = 0;

//                MyUtil.myLog("Last",""+last);

                for (int i = 0; i < last; i += 2)
                {
                    count++;

                    String str = remainingLast.substring(i, i + 2);

                    // MyUtil.myLog("Inside", "" + count + "---" + str);

                    int value = Integer.parseInt(str, 16);


                    if (count == totalBytes - 3)
                    {
                        endTime = String.valueOf(value);
                    }
                    else if (count == totalBytes - 2)
                    {
                        endTime = endTime + ":" + String.valueOf(value);
                    }
                }

                // MyUtil.myLog("endTime", endTime);

//                MyUtil.myLog("remaining", "" + remaining);
//                MyUtil.myLog("String to be cut", remaining.substring(0, f));


                remaining = remaining.replaceFirst(remaining.substring(0, f), "");

//                MyUtil.myLog("After cut", remaining);


                try
                {

                    SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm", Locale.US);
                    Date Date1 = myFormat.parse(startTime);
                    Date Date2 = myFormat.parse(endTime);
                    long mills = Date2.getTime() - Date1.getTime();

//                    totalTime += mills;
//                    MyUtil.myLog("Date1", "" + Date1.getTime());
//                    MyUtil.myLog("Date2", "" + Date2.getTime());
//                    int Hours = (int) (mills / (1000 * 60 * 60));
//                    int Mins = (int) (mills / (1000 * 60)) % 60;

//                    String diff = Hours + ":" + Mins; // updated value every1 second


                    MyUtil.myLog("Final", "" + parseDateToddMMyyyy(date) + "--------------" + mills + "-----" + myUtil.convertMillisToHrMins(mills) + "----StartTime----=" + startTime + "-----EndTime----=" + endTime);


                    boolean isStored = false;

                    for (int k = 0; k < list.size(); k++)
                    {


                        if (list.get(k).containsKey(parseDateToddMMyyyy(date)))
                        {
                            isStored = true;
                            long totalMillis = list.get(k).get(parseDateToddMMyyyy(date)) + mills;

                            HashMap<String, Long> map = new HashMap<>();
                            map.put(parseDateToddMMyyyy(date), totalMillis);

                            list.set(k, map);

                            break;
                        }
                    }


                    if (!isStored)
                    {
                        HashMap<String, Long> map = new HashMap<>();
                        map.put(parseDateToddMMyyyy(date),mills);
                        list.add(map);
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        MyUtil.myLog("List-----", "" + list);

        if (list.size() > 0)
        {
            myDatabase.addSleepData(context, list);


            //To set sleep time to band
            String todayDate = myUtil.getTodaydate();

            boolean isSleepRecordAvailable = false;

            for (int i = 0; i < list.size(); i++)
            {
                for (String listDate : list.get(i).keySet())
                {
                    if (listDate.equals(todayDate))
                    {
                        isSleepRecordAvailable = true;
//                        MyUtil.myLog(TAG,"----Date----"+todayDate+"-----"+list.get(i).get(listDate));
                        processingSleeptimeForSetting(list.get(i).get(listDate));

                        break;
                    }
                }
            }

            if (!isSleepRecordAvailable)
            {
                processingSleeptimeForSetting((long) 0);
            }

        }
        else
        {
            processingSleeptimeForSetting((long) 0);
        }

       // MyUtil.myLog("Hello","----BALLIDAKu----");
        if (((MainActivityNew)context).fragment instanceof Today)
        {
            ((Today) ((MainActivityNew)context).fragment).sleepTime();
        }

    }

    public void processingSleeptimeForSetting(Long millis)
    {
        String time = myUtil.convertMillisToHrMins(millis).replace(":", "");
//        MyUtil.myLog(TAG,"----Time----"+time);
        String commandSleepTime = "setslptm" + time;
        //  MyUtil.myLog(TAG,"----commandSleepTime----"+time);
        ((MainActivityNew)context).commandToBLE(commandSleepTime);


        //******************************************************************************************
        // Notification
        //******************************************************************************************

        myUtil.sleepHrToRemainingHr(context, myUtil.convertMillisToHrMins(millis));

        //******************************************************************************************
        //******************************************************************************************

    }


    public String parseDateToddMMyyyy(String time)
    {
        String inputPattern = "yy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try
        {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return str;
    }


    public String hTD(String hex)
    {
        return String.valueOf(Integer.parseInt(hex, 16));
    }
}
