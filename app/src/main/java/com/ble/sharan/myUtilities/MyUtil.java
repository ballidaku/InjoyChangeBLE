package com.ble.sharan.myUtilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ble.sharan.R;
import com.ble.sharan.asyncTask.Super_AsyncTask;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by brst-pc93 on 12/21/16.
 */

public class MyUtil
{

    public static final String TAG = MyUtil.class.getSimpleName();

    public static Toast toast;
    public static Snackbar snackbar;

    MyNotification myNotification = new MyNotification();


    public boolean checkConnection()
    {
        return ConnectivityReceiver.isConnected();
    }


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

    public static void  myLog(String tag, String message)
    {
        Log.e(tag, message);
    }


    /*public static class MyTextWatcher implements android.text.TextWatcher
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
    }*/



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
        //System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c.getTime());
    }

    public String getTodaydate2()
    {
        Calendar c = Calendar.getInstance();
        //System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(c.getTime());
    }


    //**********************************************************************************************
    // STEPS
    //**********************************************************************************************


    public String getRemainingSteps(Context context, int steps)
    {
        int remainingSteps = Integer.parseInt(MySharedPreference.getInstance().getDailySteps(context)) - steps;

        /*if (remainingSteps < 0 && MainActivityNew.shownStepsNotification)
        {
            MainActivityNew.shownStepsNotification=false;
            vibrate(context);
            myNotification.showNotification(context,"Steps goal completed successfully.",1);
            //MyUtil.showToast(context, "Steps goal completed.");
        }*/

        return remainingSteps > 0 ? String.valueOf(remainingSteps) : "" + 0;
    }


    //**********************************************************************************************
    // STEPS To CALORIES
    //**********************************************************************************************

    public double stepsToCaloriesFormula(Context context, int steps)
    {
        //        int weight = 60;
        double weight = Double.parseDouble(MySharedPreference.getInstance().getWeight(context).replace("Kg", "").replace("Lbs", "").trim());

        String weightUnit = MySharedPreference.getInstance().getUnit(context, MyConstant.WEIGHT);

        if (!weightUnit.equals(MyConstant.LBS))
        {
            weight = new HeightWeightHelper().kgToLbConverter(weight);

//            myLog(TAG, "WeightInLBS-----" + weightInDouble);
        }

        double Energy = (weight - 30) * 0.000315 + 0.00495;
        double Calories = Energy * steps;

        return Calories;
    }


    public String stepsToCalories(Context context, int steps)
    {
//        int weight = 60;
//        double weight =Double.parseDouble(MySharedPreference.getInstance().getWeight(context).replace("Kg", "").replace("Lbs", "").trim());
//
//        String weightUnit = MySharedPreference.getInstance().getWeightUnit(context);
//
//        if (!weightUnit.equals(MyConstant.LBS))
//        {
//            weight = new HeightWeightHelper().kgToLbConverter(weight);
//
////            myLog(TAG, "WeightInLBS-----" + weightInDouble);
//        }
//
//        double Energy = (weight - 30) * 0.000315 + 0.00495;
//        double Calories = Energy * steps;
//
//        return new DecimalFormat("##.##").format(Calories);

        double Calories = stepsToCaloriesFormula(context, steps);

        //  myLog(TAG, "Calories-----" + Calories);
//        myLog(TAG,"Calories-----"+Calories+"-------------"+(int)(Calories));

//        return Calories > 0 ? new DecimalFormat("##").format(Calories) : "" + 0;
//        return Calories > 0 ? String.valueOf(round3(Calories) ): "" + 0;

        return Calories > 0 ? String.valueOf((int) Calories) : "" + 0;


    }

    public String stepsToRemainingCalories(Context context, int steps)
    {
//        int weight = 60;
//        double weight = Double.parseDouble(MySharedPreference.getInstance().getWeight(context).replace("Kg", "").replace("Lbs", "").trim());
//
////        weight=(int)weight;
//
//        String weightUnit = MySharedPreference.getInstance().getWeightUnit(context);
//
//        if (!weightUnit.equals(MyConstant.LBS))
//        {
//            weight = new HeightWeightHelper().kgToLbConverter(weight);
//
////            myLog(TAG, "WeightInLBS-----" + weightInDouble);
//        }
//
//        double Energy = (weight - 30) * 0.000315 + 0.00495;
//        double Calories = Energy * steps;


        double Calories = stepsToCaloriesFormula(context, steps);

        double remainingCalories = Double.parseDouble(MySharedPreference.getInstance().getDailyCalories(context)/*.replace("per day", "")*/.trim()) - (int) Calories;


       /* if (remainingCalories < 0 && MainActivityNew.shownCaloriesNotification)
        {
            MainActivityNew.shownCaloriesNotification=false;
            vibrate(context);
            myNotification.showNotification(context,"Calories goal completed successfully.",2);
            //MyUtil.showToast(context, "Calories goal completed.");
        }*/

        //  myLog(TAG, "remainingCalories-----" + remainingCalories);

//        myLog(TAG,"remainingCalories-----"+Calories+"-------------"+remainingCalories);


//        return remainingCalories > 0 ? new DecimalFormat("##.##").format(remainingCalories) : "" + 0;
        return remainingCalories > 0 ? String.valueOf((int) remainingCalories) : "" + 0;

    }


    //**********************************************************************************************
    // STEPS To DISTANCE
    //**********************************************************************************************

    public double stepsToDistanceFormula(Context context, int steps)
    {
        double strideInDouble = Double.parseDouble(MySharedPreference.getInstance().getStride(context).replace("In", "").replace("Cm", "").trim());

        String strideUnit = MySharedPreference.getInstance().getUnit(context, MyConstant.STRIDE);


//        myLog(TAG,"distance--strideInDouble-----"+strideInDouble);

        if (strideUnit.equals(MyConstant.CM))
        {
            strideInDouble = new HeightWeightHelper().cmToInches(strideInDouble);
        }

        strideInDouble = ((int) strideInDouble * 0.0254) * 0.001 * 0.621371;

//        double stride = 0.00045; //in Km

        double distance = steps * strideInDouble;

        return distance;
    }

    public String twoDigitsAfterDecimalWithoutRoundOff(double value)
    {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);

        return df.format(value);

    }


    public String stepsToDistance(Context context, int steps)
    {
        double distance = stepsToDistanceFormula(context, steps);

//        myLog(TAG,"distance-----1-"+distance+"-------------"+twoDigitsAfterDecimalWithoutRoundOff( distance));

        return distance > 0 ? twoDigitsAfterDecimalWithoutRoundOff(distance) : "" + 0;
    }


    public String stepsToRemainingDistance(Context context, int steps)
    {
        double distance = Double.parseDouble(twoDigitsAfterDecimalWithoutRoundOff(stepsToDistanceFormula(context, steps)));

        double remainingKm = Double.parseDouble(MySharedPreference.getInstance().getDailyMiles(context).trim()) - distance;

//        myLog(TAG,"distance-----2-"+remainingKm+"-------------"+twoDigitsAfterDecimalWithoutRoundOff( remainingKm));

        return remainingKm > 0 ? twoDigitsAfterDecimalWithoutRoundOff(remainingKm) : "" + 0;
    }


    //**********************************************************************************************
    // SLEEP HR
    //**********************************************************************************************

    public String getSleepHr(Context context, MyDatabase myDatabase)
    {
        String sleepTime = "";

        try
        {
            long millis = myDatabase.getTodaySleepTime(context);
            SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm");

            int Hours = (int) ((millis / (1000 * 60 * 60)) % 24);
            int Mins = (int) (millis / (1000 * 60)) % 60;

            String diff = Hours + ":" + Mins; // updated value every1 second

//            myLog("dakuu","---"+millis+"----"+myFormat.format(myFormat.parse(diff)));

            sleepTime = myFormat.format(myFormat.parse(diff));

        } catch (Exception e)
        {
            e.printStackTrace();
        }


        return sleepTime;
//        myLog("Sleep Database",""+myDatabase.getAllSleepData());
    }


    //**********************************************************************************************
    // SLEEP HR TO REMAINING HR
    //**********************************************************************************************


    public String sleepHrToRemainingHr(Context context, String sleepHr)
    {
        Date startDate = null;
        Date endDate = null;


        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            startDate = simpleDateFormat.parse(sleepHr);


//            myLog(TAG,"getDailySleep-----"+MySharedPreference.getInstance().getDailySleep(context));
            endDate = simpleDateFormat.parse(MySharedPreference.getInstance().getDailySleep(context)/*.replace(" hours per day", "")*/);


        } catch (ParseException e)
        {
            e.printStackTrace();
        }


        long difference = endDate.getTime() - startDate.getTime();

        //  myLog(TAG, "Difference---" + difference);

        if (difference > 0)
        {
            return convertMillisToHrMins(difference);
        }
        else
        {
            if (MainActivityNew.shownSleepNotification)
            {
                MainActivityNew.shownSleepNotification = false;
                vibrate(context);
                myNotification.showNotification(context, "CONGRATULATIONS!!\n" +
                        "You Hit Your Sleep Goal for Today : )\n" +
                        "You Totally Rock!\n", 4);
            }

            return "00:00";
        }
    }

    public String getDifferenceBetweenTwoTimes(String s, String s1)
    {
        Date startDate = null;
        Date endDate = null;

        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            startDate = simpleDateFormat.parse(s);

            endDate = simpleDateFormat.parse(s1);


        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        long difference = endDate.getTime() - startDate.getTime();

        if (difference > 0)
        {
            String[] a = convertMillisToHrMins(difference).split(":");
            return a[0] + " Hour : " + a[1] + " Min";
        }
        else
        {
            return "00 Hour : 00 Min";
        }
    }


    public String convertMillisToHrMins(long millis)
    {
        String time = "";
        try
        {
//            myLog(TAG,"millis        "+millis);
            SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm");

            int Hours = (int) ((millis / (1000 * 60 * 60)) % 24);
            int Mins = (int) (millis / (1000 * 60)) % 60;

            String diff = Hours + ":" + Mins;
            time = myFormat.format(myFormat.parse(diff));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return time;
    }


    //**********************************************************************************************
    //**********************************************************************************************


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

    public static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++)
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    /*To hit multiple services at same time*/
    public static void execute(Super_AsyncTask asyncTask)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            asyncTask.execute();
        }

    }



        /*TO SHOW SNACKBAR*/

    public static void show_snackbar(View view, Context context, String message)
    {

        if (snackbar != null)
        {
            snackbar.dismiss();
        }

        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        snackbar.show();

    }


    // To hide Keyboard *************************************************************************************************
    public void hide_keyboard(final Context con)
    {


        try
        {
            InputMethodManager inputMethodManager = (InputMethodManager) con.getSystemService(con.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isAcceptingText())
            {
                inputMethodManager.hideSoftInputFromWindow(((Activity) con).getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public void hide_keyboard2(final View view)
    {

        view.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }
        }, 1);
    }


    // IMAGES LOADING


    public void showImageWithGlide(final Context context, final ImageView imageView, String url)
    {


        if (url.trim().isEmpty())
        {
            Glide.with(context)
                 .load("abc")
                 .centerCrop()
                 .placeholder(R.drawable.ic_no_user_new)
                 .crossFade()
                 .into(imageView);

        }
        else
        {
            Glide.with(context).load(url.trim()).asBitmap().into(new BitmapImageViewTarget(imageView)
            {

                @Override
                protected void setResource(Bitmap resource)
                {

                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);

                    circularBitmapDrawable.setCircular(true);

                    imageView.setImageDrawable(circularBitmapDrawable);
                }
            });

        }


    }


    public void showCircularImageWithPicasso(Context context, ImageView imageView, String url)
    {
        if (url.trim().isEmpty() || url.equals("null"))
        {
            /*Picasso.with(context)
                   .load("abc")
                   .placeholder(R.drawable.ic_no_user)
                   .error(R.drawable.ic_no_user)
                   .into(imageView);*/

            Picasso.with(context)
                   .load("abc")
                   .placeholder(R.drawable.ic_no_user_new)
                   .error(R.drawable.ic_no_user_new)
                   .transform(new CircleTransform())
                   .into(imageView);
        }
        else
        {

            Picasso.with(context)
                   .load(url.trim())
                   .placeholder(R.drawable.ic_no_user_new)
                   .error(R.drawable.ic_no_user_new)
                   .transform(new CircleTransform())
                   .into(imageView);
        }
    }


    public void showCircularImageWithPicasso(Context context, ImageView imageView, int file)
    {


        Picasso.with(context)
               .load(file)
               .placeholder(R.drawable.ic_no_user_new)
               .error(R.drawable.ic_no_user_new)
               .transform(new CircleTransform())
               .into(imageView);

    }

    public void showImageWithPicasso(Context context, ImageView imageView, String url)
    {
        if (url.trim().isEmpty())
        {
            Picasso.with(context)
                   .load("abc")
                   // .placeholder(R.mipmap.ic_no_user_new)
                   // .error(R.mipmap.ic_no_user_new)
                   .into(imageView);
        }
        else
        {

            Picasso.with(context)
                   .load(url.trim())
                   // .placeholder(R.mipmap.ic_no_user_new)
                   // .error(R.mipmap.ic_no_user_new)
                   .into(imageView);
        }
    }


    public class CircleTransform implements Transformation
    {
        @Override
        public Bitmap transform(Bitmap source)
        {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source)
            {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key()
        {
            return "circle";
        }
    }


    // COnvert Date to Day

    public String convertDateToDay(String date)
    {
        String day = "";

        SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat output = new SimpleDateFormat("EEEE");

        try
        {
            day = output.format(input.parse(date));

        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return day;
    }


    // GET DATES BETWEEN TWO DATES
    public List<String> getDates(String dateString1, String dateString2)
    {
        ArrayList<String> dates = new ArrayList<String>();
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");

        Date date1 = null;
        Date date2 = null;

        try
        {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2))
        {
            dates.add(df1.format(cal1.getTime()));
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }


    // To Get Previous Date
    public String getPreviousDate(String inputDate)
    {
//        inputDate = "15-12-2015"; // for example
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try
        {

//            myLog(TAG, "Input date : " + inputDate);

            Date date = format.parse(inputDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            c.add(Calendar.DATE, -1);
            inputDate = format.format(c.getTime());

//            myLog(TAG, "Previous date : " + inputDate);

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            inputDate = "";
        }
        return inputDate;
    }


    public Date convertStringToDate(String inputDate)
    {

        Date date = null;

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try
        {

            date = format.parse(inputDate);
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date;
    }


    // Get Current Time Stamp
    public String getCurrentTimeStamp()
    {
        try
        {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
    }


    public String getCurrentDay()
    {
        try
        {

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
    }


    //Set height of listview
    public void setListViewHeight(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    //**********************************************************************************************
    // Vibrate
    //**********************************************************************************************

    public void vibrate(Context context)
    {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 1 seconds
        v.vibrate(1000);

    }


    ProgressDialog progressDialog;

    public void showProgressDialog(Context context)
    {
        progressDialog = ProgressDialog.show(context, "", "");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setContentView(R.layout.progress_dialog);

        GeometricProgressView progressView15 = (GeometricProgressView) progressDialog.findViewById(R.id.progressView15);
        progressView15.setType(GeometricProgressView.TYPE.KITE);
        progressView15.setFigurePaddingInDp(1);
        progressView15.setNumberOfAngles(30);

        progressDialog.show();
    }


    public void hideProgressDialog()
    {
        if (progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }


}
