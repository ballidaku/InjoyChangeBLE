package com.ble.sharan.mainScreen.fragments.mainFragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;
import com.ble.sharan.myUtilities.ThemeChanger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import no.nordicsemi.android.nrftoolbox.dfu.DfuActivity;


/**
 * Created by brst-pc93 on 11/4/16.
 */

public class MyProfile extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener
{

    String TAG = MyProfile.class.getSimpleName();

    Context context;
    View view;

    ImageView circularImageView_Profile;

    TextView txtv_username;
    TextView txtv_StartTime;
    TextView txtv_EndTime;
//    TextView txtv_gender;

    EditText edtv_name;
    EditText edtv_height;
    EditText edtv_weight;
    //EditText edtv_stride;

    LinearLayout linearLayout_update;


//    Spinner spinner_gender;
//    TextView txtv_gender_temp;


    KeyListener edtv_nameListener;
    KeyListener edtv_heightListener;
    KeyListener edtv_weightListener;
    KeyListener edtv_strideListener;

    CardView cardviewSleepModule;


    Drawable RIGHT_ICON_GREEN;
    Drawable EDIT_ICON;


//    RadioGroup radioGroupDistance;
//    RadioGroup radioGroupWeight;
//    RadioGroup radioGroupStride;
//    RadioGroup radioGroupStandard;


    MyUtil.HeightWeightHelper heightWeightHelper = new MyUtil.HeightWeightHelper();


    MyUtil myUtil = new MyUtil();


    boolean startWork = false;
    MyProfile myProfile;

    Calendar calendar;

    int START_END_TIME = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();
        myProfile = this;

        if (view == null)
        {

            int layout;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                layout = R.layout.fragment_profile;
            }
            else
            {
                layout = R.layout.fragment_profile_kitkat;
            }

//            view = inflater.inflate(R.layout.fragment_profile, container, false);
            view = inflater.inflate(layout, container, false);

            RIGHT_ICON_GREEN = getContext().getResources().getDrawable(R.drawable.ic_tick);
            EDIT_ICON = getContext().getResources().getDrawable(R.mipmap.ic_edit);

            setUpIds();
        }


        return view;
    }


   /* int getVersion(String firmwareVersion)
    {
        int length = firmwareVersion.length();

        return Integer.parseInt(firmwareVersion.substring(length - 2, length).trim());
    }*/


    @Override
    public void onResume()
    {
        super.onResume();



        startWork = true;


        String firmwareVersionFromBand = MySharedPreference.getInstance().getFirmwareVersion(context);

        MyUtil.myLog("firmwareVersionFromBand", " " + firmwareVersionFromBand);

        if (firmwareVersionFromBand.isEmpty())
        {
            // cardViewUpdate.setVisibility(View.GONE);
        }
        //else if (getVersion(firmwareVersionFromBand) == 32)
        else if (firmwareVersionFromBand.equals(MyConstant.LATEST_BAND_VERSION))
        {
            linearLayout_update.setVisibility(View.GONE);
        }
    }

    private void setUpIds()
    {

        view.findViewById(R.id.linearLayoutBackground).setBackground((Drawable) ThemeChanger.getInstance().getBackground(context, MyConstant.BACKGROUND));

        circularImageView_Profile = (ImageView) view.findViewById(R.id.circularImageView_Profile);

        txtv_username = (TextView) view.findViewById(R.id.txtv_username);
//        txtv_gender = (TextView) view.findViewById(R.id.txtv_gender);
//        txtv_gender_temp = (TextView) view.findViewById(R.id.txtv_gender_temp);

        (txtv_StartTime = (TextView) view.findViewById(R.id.txtv_StartTime)).setOnClickListener(this);
        (txtv_EndTime = (TextView) view.findViewById(R.id.txtv_EndTime)).setOnClickListener(this);


        edtv_name = (EditText) view.findViewById(R.id.edtv_name);
        edtv_nameListener = edtv_name.getKeyListener();
        edtv_name.setKeyListener(null);

        edtv_height = (EditText) view.findViewById(R.id.edtv_height);
        edtv_heightListener = edtv_height.getKeyListener();
        edtv_height.setKeyListener(null);


        edtv_weight = (EditText) view.findViewById(R.id.edtv_weight);
        edtv_weightListener = edtv_weight.getKeyListener();
        edtv_weight.setKeyListener(null);


        cardviewSleepModule=(CardView)view.findViewById(R.id.cardviewSleepModule);
        // This  functionality is stopped by client yet
        cardviewSleepModule.setVisibility(View.GONE);


        (linearLayout_update = (LinearLayout) view.findViewById(R.id.linearLayout_update)).setOnClickListener(this);
        view.findViewById(R.id.linearLayout_signOut).setOnClickListener(this);


        txtv_StartTime.setText(MySharedPreference.getInstance().getSleepStartTime(context));
        txtv_EndTime.setText(MySharedPreference.getInstance().getSleepEndTime(context));


//        edtv_stride = (EditText) view.findViewById(R.id.edtv_stride);
//        edtv_strideListener = edtv_stride.getKeyListener();
//        edtv_stride.setKeyListener(null);


//        spinner_gender = (Spinner) view.findViewById(R.id.spinner_gender);
//
//
//        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
//            {
//
//                if (i != 0)
//                {
//                    String gender = spinner_gender.getSelectedItem().toString();
//
//                    MySharedPreference.getInstance().saveGender(context, gender);
//
//                    txtv_gender.setText(MySharedPreference.getInstance().getGender(context));
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView)
//            {
//
//            }
//        });

//        radioGroupDistance = (RadioGroup) view.findViewById(R.id.radioGroupDistance);
//        radioGroupWeight = (RadioGroup) view.findViewById(R.id.radioGroupWeight);
//        radioGroupStride = (RadioGroup) view.findViewById(R.id.radioGroupStride);
//        radioGroupStandard = (RadioGroup) view.findViewById(R.id.radioGroupStandard);


//        String distanceUnit = MySharedPreference.getInstance().getDistanceUnit(context);
//        String weightUnit = MySharedPreference.getInstance().getWeightUnit(context);
//        String strideUnit = MySharedPreference.getInstance().getStrideUnit(context);
//        String standardUnit = MySharedPreference.getInstance().getStandardUnit(context);


//        radioGroupDistance.setOnCheckedChangeListener(new RadioGroupListener(radioGroupDistance));
//        radioGroupWeight.setOnCheckedChangeListener(new RadioGroupListener(radioGroupWeight));
//        radioGroupStride.setOnCheckedChangeListener(new RadioGroupListener(radioGroupStride));
//        radioGroupStandard.setOnCheckedChangeListener(new RadioGroupListener(radioGroupStandard));


//        radioGroupDistance.check(distanceUnit.equals(MyConstant.KM) ? R.id.radioButtonKm : R.id.radioButtonMiles);
//        radioGroupWeight.check(weightUnit.equals(MyConstant.KG) ? R.id.radioButtonKg : R.id.radioButtonLbs);
//        radioGroupStride.check(strideUnit.equals(MyConstant.CM) ? R.id.radioButtonCm : R.id.radioButtonIn);
//        radioGroupStandard.check(standardUnit.equals(MyConstant.IMPERIAL) ? R.id.radioButtonImperial : R.id.radioButtonMetric);


        refreshUI();

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {

            case R.id.txtv_StartTime:

                addTime();
                START_END_TIME = 1;

                break;

            case R.id.txtv_EndTime:

                addTime();
                START_END_TIME = 2;

                break;

            case R.id.linearLayout_signOut:

                ((MainActivityNew) getActivity()).displayView(5);

                break;


            case R.id.linearLayout_update:


               /* if (((MainActivityNew) getActivity()).BLE_STATUS.equals(MyConstant.CONNECTED))
                {*/
                Intent intent = new Intent(context, DfuActivity.class);
                   /* intent.putExtra(MyConstant.DEVICE_ADDRESS,((MainActivityNew) getActivity()).mDevice.getAddress());
                    intent.putExtra(MyConstant.DEVICE_NAME,((MainActivityNew) getActivity()).mDevice.getName());*/
                // startActivity(intent);
                startActivityForResult(intent, 1390);

                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
//                }


                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == 1390)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                boolean result = data.getBooleanExtra("result", false);
                MyUtil.myLog(TAG, "Result    " + result);

                if (result)
                {
                    MySharedPreference.getInstance().saveFirmwareVersion(context, "CS11753b32");
                }
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                //Write your code if there's no result
            }
        }
    }


    private void refreshUI()
    {

       myUtil.showCircularImageWithPicasso(context, circularImageView_Profile, MySharedPreference.getInstance().getPhoto(context));
//        myUtil.showCircularImageWithPicasso(context,circularImageView_Profile, "");

        txtv_username.setText(MySharedPreference.getInstance().getName(context));
//        txtv_gender.setText(MySharedPreference.getInstance().getGender(context));

        edtv_name.setText(MySharedPreference.getInstance().getName(context));
        edtv_height.setText(MySharedPreference.getInstance().getHeight(context));
        edtv_weight.setText(MySharedPreference.getInstance().getWeight(context));
//        edtv_stride.setText(MySharedPreference.getInstance().getStride(context));

//        String gender = MySharedPreference.getInstance().getGender(context);
//        if (gender.isEmpty())
//        {
//            spinner_gender.setSelection(0);
//        }
//        else if (gender.equalsIgnoreCase("MALE"))
//        {
//            spinner_gender.setSelection(1);
//            txtv_gender_temp.setText("Male");
//        }
//        else
//        {
//            spinner_gender.setSelection(2);
//            txtv_gender_temp.setText("Female");
//        }
    }


    public void addTime()
    {
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(getActivity(), this, hour, minute, false).show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1)
    {
        calendar.set(Calendar.HOUR_OF_DAY, i);
        calendar.set(Calendar.MINUTE, i1);
        SimpleDateFormat mSDF = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat mSDF2 = new SimpleDateFormat("HHmm");

        String alarmTime = mSDF2.format(calendar.getTime());

        String showTime = mSDF.format(calendar.getTime());


        if (START_END_TIME == 1)
        {
            txtv_StartTime.setText(showTime);
            MySharedPreference.getInstance().saveSleepStartTime(context, showTime);
        }
        else
        {
            txtv_EndTime.setText(showTime);
            MySharedPreference.getInstance().saveSleepEndTime(context, showTime);
        }


        MyUtil.myLog(TAG, " showTime " + showTime + " alarmTime " + alarmTime);
    }




    /*public class RadioGroupListener implements RadioGroup.OnCheckedChangeListener
    {

        RadioGroup radioGroup;

        RadioGroupListener(RadioGroup radioGroup)
        {
            this.radioGroup = radioGroup;
        }

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId)
        {

           *//* if(radioGroup == radioGroupStandard && startWork)
            {
                MySharedPreference.getInstance().saveStandardUnit(context, checkedId == R.id.radioButtonImperial? MyConstant.IMPERIAL : MyConstant.METRIC);

                MyUtil.showToast(context,"Standard unit changed.");



                // For Weight
                double weight = Double.parseDouble(MySharedPreference.getInstance().getWeight(context).replace("Kg", "").replace("Lbs", "").trim());

                double resultWeight;
                if (checkedId == R.id.radioButtonMetric)
                {
                    resultWeight = heightWeightHelper.lbToKgConverter(weight);
                }
                else
                {
                    resultWeight = heightWeightHelper.kgToLbConverter(weight);
                }

                MySharedPreference.getInstance().saveWeight(context, String.valueOf(resultWeight));

                edtv_weight.setText(MySharedPreference.getInstance().getWeight(context));


                // For Stride

                double stride = Double.parseDouble(MySharedPreference.getInstance().getStride(context).replace("In", "").replace("Cm", "").trim());


                double resultStride;
                if (checkedId == R.id.radioButtonMetric)
                {
                    resultStride = heightWeightHelper.inchesToCm(stride);
                }
                else
                {
                    resultStride = heightWeightHelper.cmToInches(stride);
                }


                MySharedPreference.getInstance().saveStride(context, String.valueOf(resultStride));

               // edtv_stride.setText(MySharedPreference.getInstance().getStride(context));



            }*//*


            *//*if (radioGroup == radioGroupDistance && startWork)
            {
                MySharedPreference.getInstance().saveDistanceUnit(context, checkedId == R.id.radioButtonKm ? MyConstant.KM : MyConstant.MILES);

                MyUtil.showToast(context,"Distance unit changed.");

            }
            else if (radioGroup == radioGroupWeight && startWork)
            {
                MySharedPreference.getInstance().saveWeightUnit(context, checkedId == R.id.radioButtonKg ? MyConstant.KG : MyConstant.LBS);


                double d = Double.parseDouble(MySharedPreference.getInstance().getWeight(context).replace("Kg", "").replace("Lbs", "").trim());

                double result;
                if (checkedId == R.id.radioButtonKg)
                {
                    result = heightWeightHelper.lbToKgConverter(d);
                }
                else
                {
                    result = heightWeightHelper.kgToLbConverter(d);
                }

                MySharedPreference.getInstance().saveWeight(context, String.valueOf(result));

                edtv_weight.setText(MySharedPreference.getInstance().getWeight(context));


                MyUtil.showToast(context,"Weight unit changed.");
            }
            else if (radioGroup == radioGroupStride && startWork)
            {
                MySharedPreference.getInstance().saveStrideUnit(context, checkedId == R.id.radioButtonCm ? MyConstant.CM : MyConstant.IN);

                double d = Double.parseDouble(MySharedPreference.getInstance().getStride(context).replace("In", "").replace("Cm", "").trim());


                double result;
                if (checkedId == R.id.radioButtonCm)
                {
                    result = heightWeightHelper.inchesToCm(d);
                }
                else
                {
                    result = heightWeightHelper.cmToInches(d);
                }


                MySharedPreference.getInstance().saveStride(context, String.valueOf(result));

                edtv_stride.setText(MySharedPreference.getInstance().getStride(context));

                MyUtil.showToast(context,"Stride Measurement unit changed.");
            }*//*
        }
    }*/


}
