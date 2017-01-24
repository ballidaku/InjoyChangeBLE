package com.ble.sharan.asyncTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;




public class ImageDownload_AsynTask extends AsyncTask<String, Void, Bitmap> {
    Image_Notifier listener;
    String Url = "";

    public void setOnResultsListener(Image_Notifier listener) {
        this.listener = listener;
    }

    public ImageDownload_AsynTask(String meth) {
        Url = meth;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String encoded = "";
        Bitmap mIcon1 = null;
        try {
            URL img_value = null;
            try {
                img_value = new URL(Url);
                mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
               /* ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                mIcon1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);*/

                return mIcon1;
            } catch (MalformedURLException e) {
                Log.e("Exception 1", e.toString());
            } catch (IOException e) {
                Log.e("Exception 2", e.toString());
            }


            return mIcon1;
        } catch (Exception e) {
            Log.e("Exception 3 ", e.toString());
            return mIcon1;
        }

    }

    @Override
    protected void onPostExecute(Bitmap Base64_String) {
        // TODO Auto-generated method stub
        super.onPostExecute(Base64_String);
        listener.Image_Downloaded(Base64_String);
    }

}
