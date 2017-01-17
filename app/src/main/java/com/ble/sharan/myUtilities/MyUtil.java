package com.ble.sharan.myUtilities;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
//            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_edit, 0);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {


            //editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_unselected, 0);

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



     /*Switch Fragment*/

    public void switchfragment(Fragment fromWhere, Fragment toWhere)
    {
        FragmentTransaction fragmentTransaction = fromWhere.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, toWhere);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

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

    //To show Keyboard
    public static void showKeyBoard(Activity activity, EditText editText)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }


    public String getTodaydate()
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c.getTime());
    }


    public String getRemainingSteps(Context context, int steps)
    {
        int remainingSteps = Integer.parseInt(MySharedPreference.getInstance().getDailySteps(context)) - steps;

        return remainingSteps > 0 ? String.valueOf(remainingSteps) : "" + 0;
    }

    public String stepsToCalories(int steps)
    {
        int weight = 60;

        double Energy = (weight - 15) * 0.000693 + 0.005895;
        double Calories = Energy * steps;

        return new DecimalFormat("##.##").format(Calories);
    }

    public String stepsToRemainingCalories(Context context, int steps)
    {
        int weight = 60;

        double Energy = (weight - 15) * 0.000693 + 0.005895;
        double Calories = Energy * steps;

        double remainingCalories = Double.parseDouble(MySharedPreference.getInstance().getDailyCalories(context).replace("per day", "").trim()) - Calories;

        return new DecimalFormat("##.##").format(remainingCalories);
    }

    public String stepsToDistance(int steps)
    {
        double stride = 0.00045; //in Km
        double distance = steps * stride;

        //txtv_milesKm.setText(String.format("%.2f", distance));
        return new DecimalFormat("##.##").format(distance);
    }


    public String stepsToRemainingDistance(Context context, int steps)
    {
        double stride = 0.00045; //in Km
        double distance = steps * stride;

        double remainingKm = Double.parseDouble(MySharedPreference.getInstance().getDailyMiles(context).replace("per day", "").trim()) - distance;

        return new DecimalFormat("##.##").format(remainingKm);
    }


    public static class HeightWeightHelper
    {

        /**
         * @param value double that is formatted
         * @return double that has 1 decimal place
         */
        private double format(double value)
        {
            if (value != 0)
            {
                DecimalFormat df = new DecimalFormat("###.#");
                return Double.valueOf(df.format(value));
            }
            else
            {
                return -1;
            }
        }

        /**
         * @param lb - pounds
         * @return kg rounded to 1 decimal place
         */
        public double lbToKgConverter(double lb)
        {
            return format(lb * 0.45359237);
        }

        /**
         * @param kg - kilograms
         * @return lb rounded to 1 decimal place
         */
        public double kgToLbConverter(double kg)
        {
            return format(kg * 2.20462262);
        }

        /**
         * @param cm - centimeters
         * @return feet rounded to 1 decimal place
         */
        public double cmToFeetConverter(double cm)
        {
            return format(cm * 0.032808399);
        }

        /**
         * @param feet - feet
         * @return centimeters rounded to 1 decimal place
         */
        public double feetToCmConverter(double feet)
        {
            return format(feet * 30.48);
        }


        public double inchesToCm(double inches)
        {
            return inches * 2.54;
        }

        public double cmToInches(double cm)
        {
            return cm / 2.54;
        }




    }


    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }




}
