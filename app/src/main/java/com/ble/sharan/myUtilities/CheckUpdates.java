package com.ble.sharan.myUtilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;

/**
 * Created by brst-pc93 on 6/16/17.
 */

public class CheckUpdates
{
    String TAG = CheckUpdates.class.getSimpleName();

    Context context;

    MyNotification myNotification =new MyNotification();

    public CheckUpdates(Context context)
    {
        this.context = context;

        asyncTask.execute();
    }

    private AsyncTask asyncTask = new AsyncTask<Object, Void, Boolean>()
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Object... params)
        {

            try
            {
                boolean isNewVersionAvailable ;


                Log.e(TAG, context.getPackageName());

                String curVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
                String newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en")
                                         .timeout(30000)
                                         .userAgent("Mozilla/5.0 (Windows;U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                         .referrer("http://www.google.com")
                                         .get().select("div[itemprop=softwareVersion]")
                                         .first()
                                         .ownText();

                Log.e(TAG, "   curVersion   " + curVersion + "  newVersion  " + newVersion);

                double c = Double.parseDouble(curVersion);
                double n = Double.parseDouble(newVersion);

                isNewVersionAvailable = c < n;


                return isNewVersionAvailable;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isNewVersionAvailable)
        {
            Log.e(TAG+"isNewVersionAvailable", "" + isNewVersionAvailable);

            MySharedPreference.getInstance().saveIsNewVersionAvailable(context,isNewVersionAvailable);


            if (isNewVersionAvailable)
            {
              //  MyDialogs.getInstance().showUpdateDialog(context);
                //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
               // myNotification.showNotification(context, "App new version is avaliable now.", 5);
            }

            super.onPostExecute(isNewVersionAvailable);
        }
    };

}

