package com.ble.sharan.asyncTask;

/*import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.appozone.investoshare.R;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;*/


public class Upload_Files_AsyncTask /*extends AsyncTask<Void, Void, String>*/
{

/*    Context con;

    HttpResponse      response;
    String            PropertyId;
    ArrayList<Bitmap> Images_Bitmap;
    ProgressDialog    dialog;
    // String            jason_response;
    Super_AsyncTask_Interface listener = null;
    String Upload_Url;

    public Upload_Files_AsyncTask(Context context, String PropertyId, ArrayList<Bitmap> Images_Bitmap, String Upload_Url, Super_AsyncTask_Interface listener)
    {
        this.con = context;
        this.listener = listener;
        this.Upload_Url = Upload_Url;
        this.PropertyId = PropertyId;
        this.Images_Bitmap = Images_Bitmap;

    }

    protected void onPreExecute()
    {

        dialog = ProgressDialog.show(con, "", "");
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params)
    {
        String response = "";
        try
        {

            response = executeMultipartPost(Upload_Url, PropertyId);

        }
        catch (Exception e)
        {
            Log.e("Exception issss", "" + e.toString());

        }
        return response;
    }

    @Override
    protected void onPostExecute(String ResponseString)
    {
        super.onPostExecute(ResponseString);
        if (dialog.isShowing())
        {
            dialog.dismiss();
        }

        if (ResponseString.equals("Success"))
        {
            listener.onTaskCompleted(ResponseString);

        }
        else if (ResponseString.equals("SLOW"))
        {
            MyUtil.show_Toast("Please check your network.", con);
        }
        else if (ResponseString.equalsIgnoreCase("ERROR"))
        {
            MyUtil.show_Toast("Server side error.", con);
        }
        else
        {
            try
            {
                JSONObject object = new JSONObject(ResponseString);

                MyUtil.show_Toast(object.getString("Message").toString(), con);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

    }

    public String executeMultipartPost(String url, String PropertyId) throws Exception
    {
        try
        {

            HttpClient httpClient  = new DefaultHttpClient();
            HttpPost   postRequest = new HttpPost(url);

            // File file= new File("/mnt/sdcard/forest.png");
            // FileBody bin = new FileBody(file);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);


             reqEntity.addPart("AgentId", new StringBody(PropertyId));

            for (int i = 0; i < Images_Bitmap.size(); i++)
            {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Bitmap                bm  = Images_Bitmap.get(i);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[]        data       = bos.toByteArray();
                String        image_name = "event_image" + i + ".jpg";
                ByteArrayBody bab        = new ByteArrayBody(data, image_name);
                reqEntity.addPart("EventImage" + i, bab);

                Log.e("file body value is ", "" + bab);

            }
                reqEntity.addPart("EventImage0",bab);
            postRequest.setEntity(reqEntity);
            HttpResponse   response = httpClient.execute(postRequest);
            BufferedReader reader   = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String         sResponse;
            StringBuilder  s        = new StringBuilder();

            while ((sResponse = reader.readLine()) != null)
            {
                s = s.append(sResponse);
            }

            Log.e("*************", "Response: " + s);
            return s.toString();
        }
        catch (Exception e)
        {
            // handle exception here
            Log.e(e.getClass().getName() + "+++", e.getMessage());

            return "ERROR";
        }
    }*/




}