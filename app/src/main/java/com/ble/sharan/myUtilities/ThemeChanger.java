package com.ble.sharan.myUtilities;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import com.ble.sharan.R;

/**
 * Created by brst-pc93 on 5/22/17.
 */

public class ThemeChanger
{
    public static int CURRENT_THEME = 0;

    public final static int THEME_BLUE = 1;
    public final static int THEME_RED = 2;
    public final static int THEME_ORANGE = 3;



    public static ThemeChanger instance = null;

    public static ThemeChanger getInstance()
    {
        if (instance == null)
        {
            instance = new ThemeChanger();

        }
        return instance;

    }


    public <T> T getBackground(Context context, String imageName)
    {

        switch (CURRENT_THEME)
        {
            case 1: // BLUE
                if (imageName.equals(MyConstant.BACKGROUND))
                {
                    return (T) ContextCompat.getDrawable(context, R.drawable.ic_background_blue);
                }
                else if (imageName.equals(MyConstant.TODAY))
                {
                    return (T) (Integer) R.drawable.ic_today_blue;
                }
                else if (imageName.equals(MyConstant.THIS_WEEK))
                {
                    return (T) (Integer) R.drawable.ic_week_blue;
                }
                else if (imageName.equals(MyConstant.MY_GOALS))
                {
                    return (T) (Integer) R.drawable.ic_goal_blue;
                }
                else if (imageName.equals(MyConstant.OVERALL))
                {
                    return (T) (Integer) R.drawable.ic_overall_blue;
                }
                else if (imageName.equals(MyConstant.HOME))
                {
                    return (T) (Integer) R.mipmap.ic_home_blue;
                }
                else if (imageName.equals(MyConstant.CHALLENGE))
                {
                    return (T) (Integer) R.mipmap.ic_challenge_blue;
                }
                else if (imageName.equals(MyConstant.PROFILE))
                {
                    return (T) (Integer) R.mipmap.ic_profile_blue;
                }
                else if (imageName.equals(MyConstant.ALARM))
                {
                    return (T) (Integer) R.mipmap.ic_alarm_blue;
                }


                break;


            case 2: // RED

                if (imageName.equals(MyConstant.BACKGROUND))
                {
                    return (T) ContextCompat.getDrawable(context, R.drawable.ic_background_red);
                }
                else if (imageName.equals(MyConstant.TODAY))
                {
                    return (T) (Integer) R.drawable.ic_today_red;
                }
                else if (imageName.equals(MyConstant.THIS_WEEK))
                {
                    return (T) (Integer) R.drawable.ic_week_red;
                }
                else if (imageName.equals(MyConstant.MY_GOALS))
                {
                    return (T) (Integer) R.drawable.ic_goal_red;
                }
                else if (imageName.equals(MyConstant.OVERALL))
                {
                    return (T) (Integer) R.drawable.ic_overall_red;
                }
                else if (imageName.equals(MyConstant.HOME))
                {
                    return (T) (Integer) R.mipmap.ic_home_red;
                }
                else if (imageName.equals(MyConstant.CHALLENGE))
                {
                    return (T) (Integer) R.mipmap.ic_challenge_red;
                }
                else if (imageName.equals(MyConstant.PROFILE))
                {
                    return (T) (Integer) R.mipmap.ic_profile_red;
                }
                else if (imageName.equals(MyConstant.ALARM))
                {
                    return (T) (Integer) R.mipmap.ic_alarm_red;
                }

                break;

        }

        return null;


    }


    public void onActivityCreateSetTheme(Activity activity, int THEME)
    {
        CURRENT_THEME = THEME;

        switch (THEME)
        {
            case THEME_BLUE:
                activity.setTheme(R.style.AppThemeBlue);
                break;

            case THEME_RED:
                activity.setTheme(R.style.AppThemeRed);
                break;

            case THEME_ORANGE:
                activity.setTheme(R.style.AppThemeOrange);
                break;


        }
    }


    public int getThemePrimaryColor(final Context context)
    {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimary});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }


    public int getActivityOverallBackground(String fromWhere)
    {
        int id = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            switch (fromWhere)
            {
                case MyConstant.ACTIVITY: // BLUE
                    id = R.drawable.left_selector_blue;
                    break;
                case MyConstant.OVERALL:
                    id = R.drawable.right_selector_blue;
                    break;
                case MyConstant.REFRESH:
                    id = R.drawable.ic_refresh_new;
                    break;
                case MyConstant.CONNECT:
                    id = R.drawable.ic_connect_new;
                    break;
                case MyConstant.ALARM_CHECKED:
                    id = R.drawable.ic_checked;
                    break;
                case MyConstant.ALARM_UNCHECKED:
                    id = R.drawable.ic_unchecked;
                    break;

            }
        }
        else
        {
            switch (CURRENT_THEME)
            {
                case 1: // BLUE
                    if (fromWhere.equals(MyConstant.ACTIVITY))
                    {
                        id = R.drawable.left_selector_blue_kitkat;
                    }
                    else  if (fromWhere.equals(MyConstant.OVERALL))
                    {
                        id = R.drawable.right_selector_blue_kitkat;
                    }
                    else  if (fromWhere.equals(MyConstant.REFRESH))
                    {
                        id = R.mipmap.ic_refresh_blue;
                    }
                    else  if (fromWhere.equals(MyConstant.CONNECT))
                    {
                        id = R.mipmap.ic_connect_blue;
                    }
                    else  if (fromWhere.equals(MyConstant.ALARM_CHECKED))
                    {
                        id = R.mipmap.ic_check_blue;
                    }
                    else  if (fromWhere.equals(MyConstant.ALARM_UNCHECKED))
                    {
                        id = R.mipmap.ic_uncheck_blue;
                    }

                    break;

                case 2: // RED

                    if (fromWhere.equals(MyConstant.ACTIVITY))
                    {
                        id = R.drawable.left_selector_red_kitkat;
                    }
                    else  if (fromWhere.equals(MyConstant.OVERALL))
                    {
                        id = R.drawable.right_selector_red_kitkat;
                    }
                    else  if (fromWhere.equals(MyConstant.REFRESH))
                    {
                        id = R.mipmap.ic_refresh_red;
                    }
                    else  if (fromWhere.equals(MyConstant.CONNECT))
                    {
                        id = R.mipmap.ic_connect_red;
                    }
                    else  if (fromWhere.equals(MyConstant.ALARM_CHECKED))
                    {
                        id = R.mipmap.ic_check_red;
                    }
                    else  if (fromWhere.equals(MyConstant.ALARM_UNCHECKED))
                    {
                        id = R.mipmap.ic_uncheck_red;
                    }

                    break;
            }
        }
        return id;
    }
}
