package com.ble.sharan.myUtilities;

import android.content.Context;
import android.widget.Toast;

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
