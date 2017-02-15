package com.ble.sharan.mainScreen.fragments.mainFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.BeanRecords;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by brst-pc93 on 1/4/17.
 */

public class Overall extends Fragment
{
    String TAG = Overall.class.getSimpleName();

    Context context;
    View view;


    TextView txtv_totalSteps;
    TextView txtv_totalKm;
    TextView txtv_totalCalories;
    TextView txtv_km_milesHeading;

    MyDatabase myDatabase;

    MyUtil myUtil = new MyUtil();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_overall, container, false);

            myDatabase = new MyDatabase(getActivity());

            setUpIds();

        }


        return view;
    }

    private void setUpIds()
    {
        txtv_totalSteps = (TextView) view.findViewById(R.id.txtv_totalSteps);
        txtv_totalKm = (TextView) view.findViewById(R.id.txtv_totalKm);
        txtv_totalCalories = (TextView) view.findViewById(R.id.txtv_totalCalories);
        txtv_km_milesHeading = (TextView) view.findViewById(R.id.txtv_km_milesHeading);


    }


    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    long totalSteps = 0;


    public void updateUI()
    {

        List<BeanRecords> list = myDatabase.getAllStepRecords(context);

        totalSteps = 0;

        for (int i = 0; i < list.size(); i++)
        {
//            Log.e("Data", "----" + list.get(i).getID() + "----" + list.get(i).getDate() + "----" + list.get(i).getSteps());

            totalSteps += Long.parseLong(list.get(i).getSteps());
        }

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(totalSteps);


        txtv_totalSteps.setText(yourFormattedString);



//        txtv_totalKm.setText(myUtil.stepsToDistance(context, (int) totalSteps));
        txtv_totalKm.setText(stepsToDistance((int) totalSteps));


        txtv_totalCalories.setText(myUtil.stepsToCalories(context, (int) totalSteps));


    }


    public String stepsToDistance(int steps)
    {
        double strideInDouble = Double.parseDouble(MySharedPreference.getInstance().getStride(context).replace("In", "").replace("Cm", "").trim());

        String strideUnit = MySharedPreference.getInstance().getUnit(context,MyConstant.STRIDE);


        if (strideUnit.equals(MyConstant.CM))
        {
            strideInDouble = new MyUtil.HeightWeightHelper().cmToInches(strideInDouble);
        }


        strideInDouble = (strideInDouble * 0.0254) * 0.001 * 0.621371;


//        Log.e(TAG, "strideInDouble-----" + strideInDouble);


//        double stride = 0.00045; //in Km
        double distance = steps * strideInDouble;


        if(MySharedPreference.getInstance().getUnit(context,MyConstant.DISTANCE).equals(MyConstant.MILES))
        {
            txtv_km_milesHeading.setText("TOTAL MILES");
//            Log.e(TAG, "distance-----" + distance);
            return new DecimalFormat("##.##").format(distance);
        }
        else
        {
            txtv_km_milesHeading.setText("TOTAL KM");
            distance=distance*1.60934;
            return new DecimalFormat("##.##").format(distance);


        }


    }


}
