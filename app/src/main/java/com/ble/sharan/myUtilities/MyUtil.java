package com.ble.sharan.myUtilities;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ble.sharan.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by brst-pc93 on 12/21/16.
 */

public class MyUtil
{
    public static Toast toast;

    // To show Toast ****************************************************************************************************
    public static void showToast(Context context, String message)
    {
        if (toast != null)
        {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);

        toast.show();

    }


    public static class MyTextWatcher implements android.text.TextWatcher
    {

        EditText editText;

        public MyTextWatcher(EditText editText)
        {
            this.editText = editText;
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_selected, 0);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {


            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_unselected, 0);

        }

        @Override
        public void afterTextChanged(Editable s)
        {

            if (s.length() == 0)
            {
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_unselected, 0);
            }
        }
    }


    //To hide Keyboard

    public static void hideKeyBoard(Activity activity)
    {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }


    public String getTodaydate()
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c.getTime());
    }


    public String getRemainingSteps(Context context,int steps)
    {
        int remainingSteps =  Integer.parseInt(MySharedPreference.getInstance().getDailySteps(context)) -steps;

        return remainingSteps > 0 ? String.valueOf(remainingSteps) :  ""+0;
    }

    public String stepsToCalories(int steps)
    {
        int weight = 60;

        double Energy = (weight - 15) * 0.000693 + 0.005895;
        double Calories = Energy * steps;

       return new DecimalFormat("##.##").format(Calories);
    }

    public String stepsToRemainingCalories(Context context,int steps)
    {
        int weight = 60;

        double Energy = (weight - 15) * 0.000693 + 0.005895;
        double Calories = Energy * steps;

        double remainingCalories =  Double.parseDouble(MySharedPreference.getInstance().getDailyCalories(context).replace("per day","").trim()) - Calories;

        return new DecimalFormat("##.##").format(remainingCalories);
    }

    public String stepsToDistance(int steps)
    {
        double stride = 0.00045; //in Km
        double distance = steps * stride;

        //txtv_milesKm.setText(String.format("%.2f", distance));
        return new DecimalFormat("##.##").format(distance);
    }


    public String stepsToRemainingDistance(Context context,int steps)
    {
        double stride = 0.00045; //in Km
        double distance = steps * stride;

        double remainingKm =  Double.parseDouble(MySharedPreference.getInstance().getDailyMiles(context).replace("per day","").trim()) - distance;

        return new DecimalFormat("##.##").format(remainingKm);
    }

}
