package com.ble.sharan.myUtilities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

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





}
