package com.ble.sharan.myUtilities;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.ble.sharan.R;

/**
 * Created by brst-pc93 on 5/22/17.
 */

public class ThemeChanger
{
    public final static int THEME_BLUE = 1;
    public final static int THEME_ORANGE = 2;



    public static ThemeChanger instance = null;

    public static ThemeChanger getInstance()
    {
        if (instance == null)
        {
            instance = new ThemeChanger();

        }
        return instance;

    }


    public int getDefaultColor(Context context, int ID)
    {
        int color = 0;

        switch (ID)
        {
            case 1:
                color = R.color.colorOrange;
                break;

            case 2:

                break;

            default:
                color = R.color.colorBlue;
                break;

        }
        return ContextCompat.getColor(context, color);


    }



    public void onActivityCreateSetTheme(Activity activity , int THEME)
    {
        switch (THEME)
        {
            case THEME_BLUE:
                activity.setTheme(R.style.AppThemeBlue);
                break;

            case THEME_ORANGE:
                activity.setTheme(R.style.AppThemeOrange);
                break;

             /*case THEME_WHITE:
                activity.setTheme(R.style.SecondTheme);
                break;*/

        }
    }


   /* public int getThemePrimaryColor (final Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorPrimary });
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }*/


}
