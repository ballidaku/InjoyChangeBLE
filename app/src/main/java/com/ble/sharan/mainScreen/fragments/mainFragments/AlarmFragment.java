package com.ble.sharan.mainScreen.fragments.mainFragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by brst-pc93 on 12/26/16.
 */

public class AlarmFragment extends Fragment implements View.OnClickListener, /*DatePickerDialog.OnDateSetListener, */TimePickerDialog.OnTimeSetListener
{

    String TAG = AlarmFragment.class.getSimpleName();


    Context context;

    View view;

    TextView txtv_alarmFirst;
    TextView txtv_alarmSecond;
    TextView txtv_alarmThird;

    TextView txtv_firstAmPm;
    TextView txtv_secondAmPm;
    TextView txtv_thirdAmPm;


    Switch switch_firstOnOff;
    Switch switch_secondOnOff;
    Switch switch_thirdOnOff;


    String ALARM_NUMBER = "";


    boolean startWork = false;

    Drawable TOP_ICON_CHECKED;
    Drawable TOP_ICON_UNCHECKED;


    EditText edtv_first_monday;
    EditText edtv_first_tuesday;
    EditText edtv_first_wednesday;
    EditText edtv_first_thursday;
    EditText edtv_first_friday;
    EditText edtv_first_saturday;
    EditText edtv_first_sunday;


    EditText edtv_second_monday;
    EditText edtv_second_tuesday;
    EditText edtv_second_wednesday;
    EditText edtv_second_thursday;
    EditText edtv_second_friday;
    EditText edtv_second_saturday;
    EditText edtv_second_sunday;


    EditText edtv_third_monday;
    EditText edtv_third_tuesday;
    EditText edtv_third_wednesday;
    EditText edtv_third_thursday;
    EditText edtv_third_friday;
    EditText edtv_third_saturday;
    EditText edtv_third_sunday;


    EditText[] editTextFirst;
    EditText[] editTextSecond;
    EditText[] editTextThird;

    String alarm1Command = "0000000";
    String alarm2Command = "0000000";
    String alarm3Command = "0000000";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_alarm, container, false);


            TOP_ICON_CHECKED = getContext().getResources().getDrawable(R.mipmap.ic_check_blue);
            TOP_ICON_UNCHECKED = getContext().getResources().getDrawable(R.mipmap.ic_uncheck_blue);

            setUpIds();

            ((MainActivityNew) context).setDateToBLE();

            getDataAndShow();


        }


        alarm1Command = MySharedPreference.getInstance().getAlarmFirstCommand(context);
        alarm2Command = MySharedPreference.getInstance().getAlarmSecondCommand(context);
        alarm3Command = MySharedPreference.getInstance().getAlarmThirdCommand(context);
        setCheckedByCommand();


        return view;
    }


    public String convertTOHEX(String command)
    {
        int decimal = Integer.parseInt(command, 2);
        String hexStr = Integer.toString(decimal, 16);


        StringBuilder stringBuilder = new StringBuilder(hexStr);

        if (stringBuilder.length() == 1)
        {
            hexStr = "0" + hexStr;
        }

        //Log.e("HelloBalli", hexStr);

        return hexStr;
    }


    @Override
    public void onResume()
    {
        super.onResume();

        startWork = true;

    }


    private void setUpIds()
    {
        (txtv_alarmFirst = (TextView) view.findViewById(R.id.txtv_alarmFirst)).setOnClickListener(this);
        (txtv_alarmSecond = (TextView) view.findViewById(R.id.txtv_alarmSecond)).setOnClickListener(this);
        (txtv_alarmThird = (TextView) view.findViewById(R.id.txtv_alarmThird)).setOnClickListener(this);


        txtv_firstAmPm = (TextView) view.findViewById(R.id.txtv_firstAmPm);
        txtv_secondAmPm = (TextView) view.findViewById(R.id.txtv_secondAmPm);
        txtv_thirdAmPm = (TextView) view.findViewById(R.id.txtv_thirdAmPm);


        switch_firstOnOff = (Switch) view.findViewById(R.id.switch_firstOnOff);
        switch_secondOnOff = (Switch) view.findViewById(R.id.switch_secondOnOff);
        switch_thirdOnOff = (Switch) view.findViewById(R.id.switch_thirdOnOff);

        switch_firstOnOff.setOnCheckedChangeListener(new SwitchListener(switch_firstOnOff));
        switch_secondOnOff.setOnCheckedChangeListener(new SwitchListener(switch_secondOnOff));
        switch_thirdOnOff.setOnCheckedChangeListener(new SwitchListener(switch_thirdOnOff));


        edtv_first_monday = (EditText) view.findViewById(R.id.edtv_first_monday);
        edtv_first_tuesday = (EditText) view.findViewById(R.id.edtv_first_tuesday);
        edtv_first_wednesday = (EditText) view.findViewById(R.id.edtv_first_wednesday);
        edtv_first_thursday = (EditText) view.findViewById(R.id.edtv_first_thursday);
        edtv_first_friday = (EditText) view.findViewById(R.id.edtv_first_friday);
        edtv_first_saturday = (EditText) view.findViewById(R.id.edtv_first_saturday);
        edtv_first_sunday = (EditText) view.findViewById(R.id.edtv_first_sunday);


        edtv_second_monday = (EditText) view.findViewById(R.id.edtv_second_monday);
        edtv_second_tuesday = (EditText) view.findViewById(R.id.edtv_second_tuesday);
        edtv_second_wednesday = (EditText) view.findViewById(R.id.edtv_second_wednesday);
        edtv_second_thursday = (EditText) view.findViewById(R.id.edtv_second_thursday);
        edtv_second_friday = (EditText) view.findViewById(R.id.edtv_second_friday);
        edtv_second_saturday = (EditText) view.findViewById(R.id.edtv_second_saturday);
        edtv_second_sunday = (EditText) view.findViewById(R.id.edtv_second_sunday);


        edtv_third_monday = (EditText) view.findViewById(R.id.edtv_third_monday);
        edtv_third_tuesday = (EditText) view.findViewById(R.id.edtv_third_tuesday);
        edtv_third_wednesday = (EditText) view.findViewById(R.id.edtv_third_wednesday);
        edtv_third_thursday = (EditText) view.findViewById(R.id.edtv_third_thursday);
        edtv_third_friday = (EditText) view.findViewById(R.id.edtv_third_friday);
        edtv_third_saturday = (EditText) view.findViewById(R.id.edtv_third_saturday);
        edtv_third_sunday = (EditText) view.findViewById(R.id.edtv_third_sunday);


        editTextFirst = new EditText[]{edtv_first_saturday, edtv_first_friday, edtv_first_thursday, edtv_first_wednesday, edtv_first_tuesday, edtv_first_monday, edtv_first_sunday};
        editTextSecond = new EditText[]{edtv_second_saturday, edtv_second_friday, edtv_second_thursday, edtv_second_wednesday, edtv_second_tuesday, edtv_second_monday, edtv_second_sunday};
        editTextThird = new EditText[]{edtv_third_saturday, edtv_third_friday, edtv_third_thursday, edtv_third_wednesday, edtv_third_tuesday, edtv_third_monday, edtv_third_sunday};

    }

    public void setListenerAndImage(EditText editText)
    {
        editText.setOnTouchListener(new MyTouchListerner(editText));
        editText.setCompoundDrawablesWithIntrinsicBounds(null, TOP_ICON_UNCHECKED, null, null);
    }


    private void setCheckedByCommand()
    {
//        Log.e(TAG, "alarm1Command   " + alarm1Command);
        StringBuilder stringBuilder1 = new StringBuilder(alarm1Command);
        StringBuilder stringBuilder2 = new StringBuilder(alarm2Command);
        StringBuilder stringBuilder3 = new StringBuilder(alarm3Command);

        for (int i = 0; i < 7; i++)
        {
            //Set Listener and icons
            setListenerAndImage(editTextFirst[i]);
            setListenerAndImage(editTextSecond[i]);
            setListenerAndImage(editTextThird[i]);


            if (String.valueOf(stringBuilder1.charAt(i)).equals("1"))
            {
                editTextFirst[i].setCompoundDrawablesWithIntrinsicBounds(null, TOP_ICON_CHECKED, null, null);
            }

            if (String.valueOf(stringBuilder2.charAt(i)).equals("1"))
            {
                editTextSecond[i].setCompoundDrawablesWithIntrinsicBounds(null, TOP_ICON_CHECKED, null, null);
            }

            if (String.valueOf(stringBuilder3.charAt(i)).equals("1"))
            {
                editTextThird[i].setCompoundDrawablesWithIntrinsicBounds(null, TOP_ICON_CHECKED, null, null);
            }
        }
    }


    private void getDataAndShow()
    {
        HashMap<String, String> map = MySharedPreference.getInstance().getAllAlarm(context);

        String firstAlarm = map.get(MyConstant.ALARM_FIRST);
        String secondAlarm = map.get(MyConstant.ALARM_SECOND);
        String thirdAlarm = map.get(MyConstant.ALARM_THIRD);


//        Log.e("firstAlarm",firstAlarm);
//        Log.e("secondAlarm",secondAlarm);
//        Log.e("thirdAlarm",thirdAlarm);

        String[] arrayfirst = firstAlarm.split(",");
        String[] arraySecond = secondAlarm.split(",");
        String[] arrayThird = thirdAlarm.split(",");


        String[] arrayfirstNew = arrayfirst[0].split(" ");
        String[] arraySecondNew = arraySecond[0].split(" ");
        String[] arrayThirdNew = arrayThird[0].split(" ");


        txtv_alarmFirst.setText(firstAlarm.isEmpty() ? "00:00" : arrayfirstNew[0]);
        txtv_firstAmPm.setText(firstAlarm.isEmpty() ? "" : arrayfirstNew[1]);

        txtv_alarmSecond.setText(secondAlarm.isEmpty() ? "00:00" : arraySecondNew[0]);
        txtv_secondAmPm.setText(secondAlarm.isEmpty() ? "" : arraySecondNew[1]);

        txtv_alarmThird.setText(thirdAlarm.isEmpty() ? "00:00" : arrayThirdNew[0]);
        txtv_thirdAmPm.setText(thirdAlarm.isEmpty() ? "" : arrayThirdNew[1]);


        switch_firstOnOff.setChecked(MySharedPreference.getInstance().getIsAlarmActivated(context, MyConstant.IS_ALARM_FIRST_ACTIVATED));
        switch_secondOnOff.setChecked(MySharedPreference.getInstance().getIsAlarmActivated(context, MyConstant.IS_ALARM_SECOND_ACTIVATED));
        switch_thirdOnOff.setChecked(MySharedPreference.getInstance().getIsAlarmActivated(context, MyConstant.IS_ALARM_THIRD_ACTIVATED));


    }


    class SwitchListener implements CompoundButton.OnCheckedChangeListener
    {
        Switch mySwitch;

        boolean previousValue = false;

        public SwitchListener(Switch mySwitch)
        {
            this.mySwitch = mySwitch;

            previousValue = mySwitch.isChecked();
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
        {

            //  Log.e(TAG, "Balli----"+b);


            if (!((MainActivityNew) context).BLE_STATUS.equals(MyConstant.CONNECTED) && startWork)
            {
                MyUtil.showToast(context, "Your device is not connected yet!!");
                mySwitch.setChecked(!b);
            }
            else if (mySwitch == switch_firstOnOff && startWork)
            {
                String alarm = MySharedPreference.getInstance().getPreference(context).getString(MyConstant.ALARM_FIRST, "00:00 AM,alm000007F1000000");
                String[] commandString = alarm.split(",");

                //  Log.e(TAG,"Alarm inside----" + alarm);

                if (!alarm.isEmpty())
                {
                    if (b)
                    {
                        MySharedPreference.getInstance().setTrueIsAlarmActivated(context, MyConstant.ALARM_FIRST);
                        //    Log.e(TAG,"commandString inside" + commandString[1]);
                        // To set alarm
                        setAlarm(commandString[1]);
                    }
                    else
                    {
                        MySharedPreference.getInstance().deleteAlarm(context, MyConstant.ALARM_FIRST);
                        disableAlarm(commandString[1]);

                    }
                }
            }
            else if (mySwitch == switch_secondOnOff && startWork)
            {
                String alarm = MySharedPreference.getInstance().getPreference(context).getString(MyConstant.ALARM_SECOND, "00:00 AM,alm100007F1000000");
                String[] commandString = alarm.split(",");

                if (!alarm.isEmpty())
                {

                    if (b)
                    {
                        MySharedPreference.getInstance().setTrueIsAlarmActivated(context, MyConstant.ALARM_SECOND);
                        //  Log.e(TAG,"commandString" + commandString[1]);
                        // To set alarm
                        setAlarm(commandString[1]);
                    }
                    else
                    {
                        MySharedPreference.getInstance().deleteAlarm(context, MyConstant.ALARM_SECOND);
                        disableAlarm(commandString[1]);
                    }
                }
            }
            else if (mySwitch == switch_thirdOnOff && startWork)
            {
                String alarm = MySharedPreference.getInstance().getPreference(context).getString(MyConstant.ALARM_THIRD, "00:00 AM,alm200007F1000000");

                String[] commandString = alarm.split(",");

                if (!alarm.isEmpty())
                {
                    if (b)
                    {
                        MySharedPreference.getInstance().setTrueIsAlarmActivated(context, MyConstant.ALARM_THIRD);
                        //   Log.e(TAG,"commandString---" + commandString[1]);
                        // To set alarm
                        setAlarm(commandString[1]);
                    }
                    else
                    {
                        MySharedPreference.getInstance().deleteAlarm(context, MyConstant.ALARM_THIRD);
                        disableAlarm(commandString[1]);
                    }
                }
            }
        }
    }


    Calendar c;

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {

            case R.id.txtv_alarmFirst:

                ALARM_NUMBER = MyConstant.ALARM_FIRST;
                addNewAlarm();

                break;

            case R.id.txtv_alarmSecond:

                ALARM_NUMBER = MyConstant.ALARM_SECOND;
                addNewAlarm();

                break;


            case R.id.txtv_alarmThird:

                ALARM_NUMBER = MyConstant.ALARM_THIRD;
                addNewAlarm();

                break;

        }
    }


    private void addNewAlarm()
    {
        if (((MainActivityNew) context).BLE_STATUS.equals(MyConstant.CONNECTED))
        {

            c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            new TimePickerDialog(getActivity(), this, hour, minute, false).show();
        }
        else
        {
            MyUtil.showToast(context, "Your device is not connected yet!!");
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1)
    {

        c.set(Calendar.HOUR_OF_DAY, i);
        c.set(Calendar.MINUTE, i1);
        SimpleDateFormat mSDF = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat mSDF2 = new SimpleDateFormat("HHmm");

        String alarmTime = mSDF2.format(c.getTime());

        String showTime = mSDF.format(c.getTime());

        String alarmKey = "";

        String weeksDaysCommand = "";

        if (ALARM_NUMBER.equals(MyConstant.ALARM_FIRST))
        {
            String[] array = showTime.split(" ");
            txtv_alarmFirst.setText(array[0]);
            txtv_firstAmPm.setText(array[1]);

            alarmKey = "0";

            switch_firstOnOff.setChecked(true);

            weeksDaysCommand = convertTOHEX(alarm1Command);
        }
        else if (ALARM_NUMBER.equals(MyConstant.ALARM_SECOND))
        {
            String[] array = showTime.split(" ");
            txtv_alarmSecond.setText(array[0]);
            txtv_secondAmPm.setText(array[1]);

            alarmKey = "1";

            switch_secondOnOff.setChecked(true);

            weeksDaysCommand = convertTOHEX(alarm2Command);


        }
        else if (ALARM_NUMBER.equals(MyConstant.ALARM_THIRD))
        {
            String[] array = showTime.split(" ");
            txtv_alarmThird.setText(array[0]);
            txtv_thirdAmPm.setText(array[1]);

            alarmKey = "2";

            switch_thirdOnOff.setChecked(true);

            weeksDaysCommand = convertTOHEX(alarm3Command);
        }

        saveAlarm(alarmKey, showTime, alarmTime, weeksDaysCommand,true);


        Log.e(TAG, "alarmTime---" + alarmTime + "----" + showTime);
    }


    public void saveAlarm(String alarmKey, String showTime, String alarmTime, String weeksDaysCommand,boolean abc)
    {
        String commandToSetAlarm = "alm" + alarmKey + alarmTime + weeksDaysCommand + "1000000";

        Log.e(TAG, "commandToSetAlarm Map" + commandToSetAlarm + "          " + weeksDaysCommand);


        if(abc)
        {
            // BY ME NOW
            setAlarm(commandToSetAlarm);
        }


        MySharedPreference.getInstance().saveAlarm(context, ALARM_NUMBER, showTime + "," + commandToSetAlarm);


    }


    public void setAlarm(String commandToSetAlarm)
    {
        Log.e(TAG, "Command" + commandToSetAlarm);

        ((MainActivityNew) context).commandToBLE(commandToSetAlarm);

        MyUtil.showToast(context, "Alarm is set");
    }

    public void disableAlarm(String command)
    {

        StringBuilder stringBuilder = new StringBuilder(command);

        stringBuilder.setCharAt(10, '0');

        Log.e(TAG, "disableAlarm" + stringBuilder.toString());

        ((MainActivityNew) context).commandToBLE(stringBuilder.toString());

        MyUtil.showToast(context, "Alarm is removed");
    }


    class MyTouchListerner implements View.OnTouchListener
    {
        EditText editText;

        public MyTouchListerner(EditText editText)
        {
            this.editText = editText;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                if (event.getRawX() >= (editText.getTop() - editText.getCompoundDrawables()[DRAWABLE_TOP].getBounds().width()))
                {
                    Drawable[] drawables = editText.getCompoundDrawables();


                    if (((MainActivityNew) context).BLE_STATUS.equals(MyConstant.CONNECTED))
                    {


                        int[] positionAndCommand = getPosition(editText);

                        //**************************************************************************
                        int position = positionAndCommand[0];

                        String almCommand = positionAndCommand[1] == 1 ? alarm1Command : positionAndCommand[1] == 2 ? alarm2Command : alarm3Command;

                        //**************************************************************************

                        StringBuilder stringBuilder;


                        if (TOP_ICON_UNCHECKED == drawables[1])
                        {
                            editText.setCompoundDrawablesWithIntrinsicBounds(null, TOP_ICON_CHECKED, null, null);

                            stringBuilder = new StringBuilder(almCommand);

                            stringBuilder.setCharAt(position, '1');

                        }
                        else
                        {
                            editText.setCompoundDrawablesWithIntrinsicBounds(null, TOP_ICON_UNCHECKED, null, null);

                            stringBuilder = new StringBuilder(almCommand);

                            stringBuilder.setCharAt(position, '0');
                        }


                        HashMap<String, String> map = MySharedPreference.getInstance().getAllAlarm(context);

                        String firstAlarm = map.get(MyConstant.ALARM_FIRST);
                        String secondAlarm = map.get(MyConstant.ALARM_SECOND);
                        String thirdAlarm = map.get(MyConstant.ALARM_THIRD);


//                        Log.e("firstAlarm", firstAlarm);
//                        Log.e("secondAlarm", secondAlarm);
//                        Log.e("thirdAlarm", thirdAlarm);

                        String[] arrayfirst = firstAlarm.split(",");
                        String[] arraySecond = secondAlarm.split(",");
                        String[] arrayThird = thirdAlarm.split(",");


                        String showtimeFirst = arrayfirst[0];
                        String showtimeSecond = arraySecond[0];
                        String showtimethird = arrayThird[0];

                        SimpleDateFormat input = new SimpleDateFormat("hh:mm a");

                        SimpleDateFormat output = new SimpleDateFormat("HHmm");

                        String alarmTime1 = "";
                        String alarmTime2 = "";
                        String alarmTime3 = "";
                        try
                        {
                            alarmTime1 = output.format(input.parse(showtimeFirst));
                            alarmTime2 = output.format(input.parse(showtimeSecond));
                            alarmTime3 = output.format(input.parse(showtimethird));

                            Log.e(TAG, "showTime " + alarmTime1 + "-----" + alarmTime2 + "    " + alarmTime3);


                        } catch (ParseException e)
                        {
                            e.printStackTrace();
                        }


                        if (positionAndCommand[1] == 1)
                        {
                            MySharedPreference.getInstance().saveAlarmFirstCommand(context, alarm1Command = stringBuilder.toString());

                            ALARM_NUMBER = MyConstant.ALARM_FIRST;
                            saveAlarm("0", showtimeFirst, alarmTime1, alarm1Command,switch_firstOnOff.isChecked());



                        }
                        else if (positionAndCommand[1] == 2)
                        {
                            MySharedPreference.getInstance().saveAlarmSecondCommand(context, alarm2Command = stringBuilder.toString());
                            ALARM_NUMBER = MyConstant.ALARM_SECOND;
                            saveAlarm("1", showtimeSecond, alarmTime2, alarm2Command,switch_secondOnOff.isChecked());
                        }
                        else if (positionAndCommand[1] == 3)
                        {
                            MySharedPreference.getInstance().saveAlarmThirdCommand(context, alarm3Command = stringBuilder.toString());
                            ALARM_NUMBER = MyConstant.ALARM_THIRD;
                            saveAlarm("2", showtimethird, alarmTime3, alarm3Command,switch_thirdOnOff.isChecked());
                        }

//                        Log.e(TAG, "AlarmCommand----" + positionAndCommand[1] + "========" + stringBuilder.toString());

                    }
                    else
                    {
                        MyUtil.showToast(context, "Your device is not connected yet!!");
                    }


                    return true;
                }
            }

            return false;
        }

    }


    private int[] getPosition(EditText editText)
    {
        int[] positionAndAlarmCommand = new int[2];


        for (int i = 0; i < 7; i++)
        {
            if (editText == editTextFirst[i])
            {
                positionAndAlarmCommand[0] = i; // Specifies the editetxt of which alarm
                positionAndAlarmCommand[1] = 1; // Specifies which alarm command we have too use
            }
            else if (editText == editTextSecond[i])
            {
                positionAndAlarmCommand[0] = i;
                positionAndAlarmCommand[1] = 2;
            }
            else if (editText == editTextThird[i])
            {
                positionAndAlarmCommand[0] = i;
                positionAndAlarmCommand[1] = 3;
            }

        }

        return positionAndAlarmCommand;
    }


}