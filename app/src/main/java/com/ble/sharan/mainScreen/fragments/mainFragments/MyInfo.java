package com.ble.sharan.mainScreen.fragments.mainFragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;


/**
 * Created by brst-pc93 on 11/4/16.
 */

public class MyInfo extends Fragment
{

    String TAG = MyInfo.class.getSimpleName();

    Context context;
    View view;

    ImageView circularImageView_Profile;

    TextView txtv_username;
    TextView txtv_gender;

    EditText edtv_name;
    EditText edtv_height;
    EditText edtv_weight;
    EditText edtv_stride;


    Spinner spinner_gender;
    TextView txtv_gender_temp;


    KeyListener edtv_nameListener;
    KeyListener edtv_heightListener;
    KeyListener edtv_weightListener;
    KeyListener edtv_strideListener;


    Drawable RIGHT_ICON_GREEN;
    Drawable EDIT_ICON;



//    RadioGroup radioGroupDistance;
//    RadioGroup radioGroupWeight;
//    RadioGroup radioGroupStride;
    RadioGroup radioGroupStandard;


    MyUtil.HeightWeightHelper heightWeightHelper = new MyUtil.HeightWeightHelper();


    MyUtil myUtil = new MyUtil();


    boolean startWork = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_profile, container, false);

            RIGHT_ICON_GREEN = getContext().getResources().getDrawable(R.drawable.ic_tick);
            EDIT_ICON = getContext().getResources().getDrawable(R.mipmap.ic_edit);

            setUpIds();
        }


        return view;
    }


    @Override
    public void onResume()
    {
        super.onResume();

        startWork = true;
    }

    private void setUpIds()
    {

        circularImageView_Profile = (ImageView) view.findViewById(R.id.circularImageView_Profile);

        txtv_username = (TextView) view.findViewById(R.id.txtv_username);
        txtv_gender = (TextView) view.findViewById(R.id.txtv_gender);
        txtv_gender_temp = (TextView) view.findViewById(R.id.txtv_gender_temp);


        edtv_name = (EditText) view.findViewById(R.id.edtv_name);
        edtv_nameListener = edtv_name.getKeyListener();
        edtv_name.setKeyListener(null);

        edtv_height = (EditText) view.findViewById(R.id.edtv_height);
        edtv_heightListener = edtv_height.getKeyListener();
        edtv_height.setKeyListener(null);


        edtv_weight = (EditText) view.findViewById(R.id.edtv_weight);
        edtv_weightListener = edtv_weight.getKeyListener();
        edtv_weight.setKeyListener(null);


        edtv_stride = (EditText) view.findViewById(R.id.edtv_stride);
        edtv_strideListener = edtv_stride.getKeyListener();
        edtv_stride.setKeyListener(null);


        spinner_gender = (Spinner) view.findViewById(R.id.spinner_gender);


        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                if (i != 0)
                {
                    String gender = spinner_gender.getSelectedItem().toString();

                    MySharedPreference.getInstance().saveGender(context, gender);

                    txtv_gender.setText(MySharedPreference.getInstance().getGender(context));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

//        radioGroupDistance = (RadioGroup) view.findViewById(R.id.radioGroupDistance);
//        radioGroupWeight = (RadioGroup) view.findViewById(R.id.radioGroupWeight);
//        radioGroupStride = (RadioGroup) view.findViewById(R.id.radioGroupStride);
        radioGroupStandard = (RadioGroup) view.findViewById(R.id.radioGroupStandard);




//        String distanceUnit = MySharedPreference.getInstance().getDistanceUnit(context);
//        String weightUnit = MySharedPreference.getInstance().getWeightUnit(context);
//        String strideUnit = MySharedPreference.getInstance().getStrideUnit(context);
        String standardUnit = MySharedPreference.getInstance().getStandardUnit(context);



//        radioGroupDistance.setOnCheckedChangeListener(new RadioGroupListener(radioGroupDistance));
//        radioGroupWeight.setOnCheckedChangeListener(new RadioGroupListener(radioGroupWeight));
//        radioGroupStride.setOnCheckedChangeListener(new RadioGroupListener(radioGroupStride));
        radioGroupStandard.setOnCheckedChangeListener(new RadioGroupListener(radioGroupStandard));




//        radioGroupDistance.check(distanceUnit.equals(MyConstant.KM) ? R.id.radioButtonKm : R.id.radioButtonMiles);
//        radioGroupWeight.check(weightUnit.equals(MyConstant.KG) ? R.id.radioButtonKg : R.id.radioButtonLbs);
//        radioGroupStride.check(strideUnit.equals(MyConstant.CM) ? R.id.radioButtonCm : R.id.radioButtonIn);
        radioGroupStandard.check(standardUnit.equals(MyConstant.IMPERIAL) ? R.id.radioButtonImperial : R.id.radioButtonMetric);


        refreshUI();

    }


    public class RadioGroupListener implements RadioGroup.OnCheckedChangeListener
    {

        RadioGroup radioGroup;

        RadioGroupListener(RadioGroup radioGroup)
        {
            this.radioGroup=radioGroup;
        }

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId)
        {

            if(radioGroup == radioGroupStandard && startWork)
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

                edtv_stride.setText(MySharedPreference.getInstance().getStride(context));



            }


            /*if (radioGroup == radioGroupDistance && startWork)
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
            }*/
        }
    }




    private void refreshUI()
    {

        myUtil.showCircularImageWithPicasso(context, circularImageView_Profile, MySharedPreference.getInstance().getPhoto(context));

        txtv_username.setText(MySharedPreference.getInstance().getName(context));
        txtv_gender.setText(MySharedPreference.getInstance().getGender(context));

        edtv_name.setText(MySharedPreference.getInstance().getName(context));
        edtv_height.setText(MySharedPreference.getInstance().getHeight(context));
        edtv_weight.setText(MySharedPreference.getInstance().getWeight(context));
        edtv_stride.setText(MySharedPreference.getInstance().getStride(context));

        String gender = MySharedPreference.getInstance().getGender(context);
        if (gender.isEmpty())
        {
            spinner_gender.setSelection(0);
        }
        else if (gender.equalsIgnoreCase("MALE"))
        {
            spinner_gender.setSelection(1);
            txtv_gender_temp.setText("Male");
        }
        else
        {
            spinner_gender.setSelection(2);
            txtv_gender_temp.setText("Female");
        }
    }
}
