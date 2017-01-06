package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_profile, container, false);

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
        edtv_name.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_name));
        edtv_name.setOnTouchListener(new MyTouchListerner(edtv_name));

        edtv_height = (EditText) view.findViewById(R.id.edtv_height);
        edtv_heightListener = edtv_height.getKeyListener();
        edtv_height.setKeyListener(null);
        edtv_height.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_height));
        edtv_height.setOnTouchListener(new MyTouchListerner(edtv_height));


        edtv_weight = (EditText) view.findViewById(R.id.edtv_weight);
        edtv_weightListener = edtv_weight.getKeyListener();
        edtv_weight.setKeyListener(null);
        edtv_weight.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_weight));
        edtv_weight.setOnTouchListener(new MyTouchListerner(edtv_weight));


        edtv_stride = (EditText) view.findViewById(R.id.edtv_stride);
        edtv_strideListener = edtv_stride.getKeyListener();
        edtv_stride.setKeyListener(null);
        edtv_stride.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_stride));
        edtv_stride.setOnTouchListener(new MyTouchListerner(edtv_stride));


        edtv_gender = (EditText) view.findViewById(R.id.edtv_gender);
        edtv_genderListener = edtv_gender.getKeyListener();
        edtv_gender.setKeyListener(null);
        edtv_gender.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_gender));
        edtv_gender.setOnTouchListener(new MyTouchListerner(edtv_gender));


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


            if (editText == edtv_name)
            {
                edtv_name.setKeyListener(edtv_nameListener);
            }
            else if (editText == edtv_height)
            {
                edtv_height.setKeyListener(edtv_heightListener);
                edtv_height.setText(editText.getText().toString().replace("Cm","").trim());
            }
            else if (editText == edtv_weight)
            {
                edtv_weight.setKeyListener(edtv_weightListener);
                edtv_weight.setText(editText.getText().toString().replace("Kg","").trim());
            }
            else if (editText == edtv_stride)
            {
                edtv_stride.setKeyListener(edtv_strideListener);
                edtv_stride.setText(editText.getText().toString().replace("Cm","").trim());
            }
            else if (editText == edtv_gender)
            {
                edtv_gender.setKeyListener(edtv_genderListener);
            }


            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                {
                    UpdateProfile(editText);

                    return true;
                }
            }
            return false;
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
            ((MainActivityNew)getActivity()).onRefreshName();
            //Log.e("Hello","1");

        }
        else if (editText == edtv_height)
        {
            MySharedPreference.getInstance().saveHeight(context, value);
            edtv_height.setText(MySharedPreference.getInstance().getHeight(context));
            //Log.e("Hello","2");
        }
        else if (editText == edtv_weight)
        {
            MySharedPreference.getInstance().saveWeight(context, value);
            edtv_weight.setText(MySharedPreference.getInstance().getWeight(context));
            //Log.e("Hello","3");
        }
        else if (editText == edtv_stride)
        {
            MySharedPreference.getInstance().saveStride(context, value);
            edtv_stride.setText(MySharedPreference.getInstance().getStride(context));
           // Log.e("Hello","4");
        }
        else if (editText == edtv_gender)
        {
            MySharedPreference.getInstance().saveGender(context, value);
            txtv_gender.setText(MySharedPreference.getInstance().getGender(context));
            //Log.e("Hello","5");
        }


        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_selected, 0);

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


        edtv_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_selected, 0);
        edtv_height.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_selected, 0);
        edtv_weight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_selected, 0);
        edtv_stride.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_selected, 0);
        edtv_gender.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_selected, 0);

    }


}
