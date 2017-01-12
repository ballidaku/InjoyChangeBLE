package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

/**
 * Created by brst-pc93 on 11/4/16.
 */

public class ProfileFragment extends Fragment
{

    Context context;
    View view;

    TextView txtv_username;
    TextView txtv_gender;

    EditText edtv_name;
    EditText edtv_height;
    EditText edtv_weight;
    EditText edtv_stride;
    EditText edtv_gender;


    KeyListener edtv_nameListener;
    KeyListener edtv_heightListener;
    KeyListener edtv_weightListener;
    KeyListener edtv_strideListener;
    KeyListener edtv_genderListener;


    Drawable RIGHT_ICON_GREEN;
    Drawable EDIT_ICON;


    Switch switch_distance;
    Switch switch_weight;
    Switch switch_stride;


    MyUtil.HeightWeightHelper heightWeightHelper =new MyUtil.HeightWeightHelper();


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

    private void setUpIds()
    {


        txtv_username = (TextView) view.findViewById(R.id.txtv_username);
        txtv_gender = (TextView) view.findViewById(R.id.txtv_gender);


        edtv_name = (EditText) view.findViewById(R.id.edtv_name);
        edtv_nameListener = edtv_name.getKeyListener();
        edtv_name.setKeyListener(null);
        //  edtv_name.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_name));
        edtv_name.setOnTouchListener(new MyTouchListerner(edtv_name));

        edtv_height = (EditText) view.findViewById(R.id.edtv_height);
        edtv_heightListener = edtv_height.getKeyListener();
        edtv_height.setKeyListener(null);
        // edtv_height.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_height));
        edtv_height.setOnTouchListener(new MyTouchListerner(edtv_height));


        edtv_weight = (EditText) view.findViewById(R.id.edtv_weight);
        edtv_weightListener = edtv_weight.getKeyListener();
        edtv_weight.setKeyListener(null);
        //  edtv_weight.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_weight));
        edtv_weight.setOnTouchListener(new MyTouchListerner(edtv_weight));


        edtv_stride = (EditText) view.findViewById(R.id.edtv_stride);
        edtv_strideListener = edtv_stride.getKeyListener();
        edtv_stride.setKeyListener(null);
        // edtv_stride.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_stride));
        edtv_stride.setOnTouchListener(new MyTouchListerner(edtv_stride));


        edtv_gender = (EditText) view.findViewById(R.id.edtv_gender);
        edtv_gender.setFilters(new InputFilter[]{
                new InputFilter()
                {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend)
                    {
                        if (src.equals(""))
                        { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z ]+"))
                        {
                            return src;
                        }
                        return "";
                    }
                }
        });


        edtv_genderListener = edtv_gender.getKeyListener();
        edtv_gender.setKeyListener(null);
        // edtv_gender.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_gender));
        edtv_gender.setOnTouchListener(new MyTouchListerner(edtv_gender));


        switch_distance = (Switch) view.findViewById(R.id.switch_distance);
        switch_weight = (Switch) view.findViewById(R.id.switch_weight);
        switch_stride = (Switch) view.findViewById(R.id.switch_stride);


        switch_distance.setOnCheckedChangeListener(new SwitchListener(switch_distance));
        switch_weight.setOnCheckedChangeListener(new SwitchListener(switch_weight));
        switch_stride.setOnCheckedChangeListener(new SwitchListener(switch_stride));


        String distanceUnit = MySharedPreference.getInstance().getDistanceUnit(context);
        String weightUnit = MySharedPreference.getInstance().getWeightUnit(context);
        String strideUnit = MySharedPreference.getInstance().getStrideUnit(context);


        switch_distance.setChecked(distanceUnit.equals(MyConstant.KM));
        switch_weight.setChecked(weightUnit.equals(MyConstant.KG));
        switch_stride.setChecked(strideUnit.equals(MyConstant.CM));


        refreshUI();
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
                if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                {

                    if (editText == edtv_name)
                    {
                        edtv_name.setKeyListener(edtv_nameListener);
                    }
                    else if (editText == edtv_height)
                    {
                        edtv_height.setKeyListener(edtv_heightListener);
                        edtv_height.setText(editText.getText().toString().replace("Cm", "").trim());
                    }
                    else if (editText == edtv_weight)
                    {
                        edtv_weight.setKeyListener(edtv_weightListener);
                        edtv_weight.setText(editText.getText().toString().replace("Kg", "").replace("Lbs", "").trim());
                    }
                    else if (editText == edtv_stride)
                    {
                        edtv_stride.setKeyListener(edtv_strideListener);
                        edtv_stride.setText(editText.getText().toString().replace("Cm", "").replace("In", "").trim());
                    }
                    else if (editText == edtv_gender)
                    {
                        edtv_gender.setKeyListener(edtv_genderListener);
                    }


                    editText.requestFocus();

                    Drawable[] drawables = editText.getCompoundDrawables();


//                    Log.e("Bitmap1", "---" + RIGHT_ICON_GREEN);
//                    Log.e("Bitmap2", "---" + drawables[2]);

                    if (RIGHT_ICON_GREEN == drawables[2])
                    {
//                        Log.e("hello", "Hello");
                        UpdateProfile(editText);
                    }
                    else
                    {
                        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, RIGHT_ICON_GREEN, null);

                        MyUtil.showKeyBoard(getActivity(), editText);
                    }


                    /*if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                        {
                            UpdateProfile(editText);

                            return true;
                        }
                    }
                    return false;*/

                    return true;
                }
            }
            return false;
        }
    }


    class SwitchListener implements CompoundButton.OnCheckedChangeListener
    {
        Switch mySwitch;

        public SwitchListener(Switch mySwitch)
        {
            this.mySwitch = mySwitch;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
        {
            if (mySwitch == switch_distance)
            {
                MySharedPreference.getInstance().saveDistanceUnit(context,b ? MyConstant.KM : MyConstant.MILES);
                Log.e("switch_distance", "" + b);
            }
            else if (mySwitch == switch_weight)
            {
                MySharedPreference.getInstance().saveWeightUnit(context,b ? MyConstant.KG : MyConstant.LBS);
                Log.e("switch_weight", "" + b);

                double d= Double.parseDouble(MySharedPreference.getInstance().getWeight(context).replace("Kg","").replace("Lbs","").trim());

                double result;
                if(b)
                {
                    result=heightWeightHelper.lbToKgConverter(d);
                }
                else
                {
                    result=heightWeightHelper.kgToLbConverter(d);
                }

                MySharedPreference.getInstance().saveWeight(context, String.valueOf(result));

                edtv_weight.setText(MySharedPreference.getInstance().getWeight(context));

            }
            else if (mySwitch == switch_stride)
            {
                MySharedPreference.getInstance().saveStrideUnit(context,b ? MyConstant.CM : MyConstant.IN);
                Log.e("switch_stride", "" + b);

                double d= Double.parseDouble(MySharedPreference.getInstance().getStride(context).replace("In","").replace("Cm","").trim());


                double result;
                if(b)
                {
                    result=heightWeightHelper.inchesToCm(d);
                }
                else
                {
                    result=heightWeightHelper.cmToInches(d);
                }


                MySharedPreference.getInstance().saveStride(context, String.valueOf(result));

                edtv_stride.setText(MySharedPreference.getInstance().getStride(context));
            }
        }
    }


    private void UpdateProfile(EditText editText)
    {
        editText.setKeyListener(null);


        String value = editText.getText().toString().trim();


        if (editText == edtv_name)
        {
            MySharedPreference.getInstance().saveName(context, value);
            txtv_username.setText(MySharedPreference.getInstance().getName(context));
            ((MainActivityNew) getActivity()).onRefreshName();


        }
        else if (editText == edtv_height)
        {
            value = value.isEmpty() ? "0" : value;

            MySharedPreference.getInstance().saveHeight(context, value);

            edtv_height.setText(MySharedPreference.getInstance().getHeight(context));

        }
        else if (editText == edtv_weight)
        {
            value = value.isEmpty() ? "0" : value;

            MySharedPreference.getInstance().saveWeight(context, value);

            edtv_weight.setText(MySharedPreference.getInstance().getWeight(context));

        }
        else if (editText == edtv_stride)
        {

            value = value.isEmpty() ? "0" : value;

            MySharedPreference.getInstance().saveStride(context, value);

            edtv_stride.setText(MySharedPreference.getInstance().getStride(context));

        }
        else if (editText == edtv_gender)
        {
            MySharedPreference.getInstance().saveGender(context, value);

            txtv_gender.setText(MySharedPreference.getInstance().getGender(context));

        }


        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, EDIT_ICON, null);

        MyUtil.hideKeyBoard(getActivity());

    }

    private void refreshUI()
    {

        txtv_username.setText(MySharedPreference.getInstance().getName(context));
        txtv_gender.setText(MySharedPreference.getInstance().getGender(context));

        edtv_name.setText(MySharedPreference.getInstance().getName(context));
        edtv_height.setText(MySharedPreference.getInstance().getHeight(context));
        edtv_weight.setText(MySharedPreference.getInstance().getWeight(context));
        edtv_stride.setText(MySharedPreference.getInstance().getStride(context));
        edtv_gender.setText(MySharedPreference.getInstance().getGender(context));


    }


}
