package com.ble.sharan.asyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.GeometricProgressView;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyUtil;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;


/**
 * Created by sharan on 10/9/15.
 */
public class Super_AsyncTask extends AsyncTask<Void, Void, String>
{

    String URL = "";
    HashMap<String, String> inputData = null;
    Context context;
    ProgressDialog dialog;
    Super_AsyncTask_Interface listener = null;
    boolean show_progressbar_or_not = false;
    MyUtil myUtil = new MyUtil();


    String fromWhere = "";

    public Super_AsyncTask(Context context, HashMap<String, String> inputData, String URL, Super_AsyncTask_Interface listener, boolean show_progressbar_or_not)
    {
        this.context = context;
        this.inputData = inputData;
        this.URL = URL;
        this.listener = listener;
        this.show_progressbar_or_not = show_progressbar_or_not;

        myUtil.hide_keyboard(context);
    }


    // Specially designed for login
    public Super_AsyncTask(Context context, HashMap<String, String> inputData, String URL, String fromWhere, Super_AsyncTask_Interface listener, boolean show_progressbar_or_not)
    {
        this.context = context;
        this.inputData = inputData;
        this.URL = URL;
        this.listener = listener;
        this.show_progressbar_or_not = show_progressbar_or_not;
        this.fromWhere = fromWhere;

        myUtil.hide_keyboard(context);
    }


    public Super_AsyncTask(Context context, String URL, Super_AsyncTask_Interface listener, boolean show_progressbar_or_not)
    {
        this.context = context;
        this.URL = URL;
        this.listener = listener;
        this.show_progressbar_or_not = show_progressbar_or_not;
        myUtil.hide_keyboard(context);
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        if (show_progressbar_or_not == true)
        {
            dialog = ProgressDialog.show(context, "", "");
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.progress_dialog);

            GeometricProgressView progressView15 = (GeometricProgressView)dialog.findViewById(R.id.progressView15);
            progressView15.setType(GeometricProgressView.TYPE.KITE);
            progressView15.setFigurePaddingInDp(1);
            progressView15.setNumberOfAngles(30);

            dialog.show();
        }

    }

    @Override
    protected String doInBackground(Void... params)
    {
        String response = "";

        Log.e("inputData", "" + inputData);

        try
        {

            if (fromWhere.equals(MyConstant.LOGIN_ACTIVITY))
            {
                response = fetch(URL, inputData.get(MyConstant.USER_NANE), inputData.get(MyConstant.PASSWORD));
            }
            else
            {
                response = performOkGetPostCall(URL, inputData);
            }

            //            response=new WebServiceHandler().performGetCall(URL);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String ResponseString)
    {
        super.onPostExecute(ResponseString);

        if (show_progressbar_or_not == true)
        {

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }

        Log.e("Response for " + context.getClass().getName(), " " + ResponseString);

        if (!ResponseString.equals("SLOW") && !ResponseString.equals("ERROR"))
        {
            listener.onTaskCompleted(ResponseString);

        }
        else if (ResponseString.equals("SLOW"))
        {
            MyUtil.showToast(context, "Please check your network.");
        }
        else if (ResponseString.equals("ERROR"))
        {
            MyUtil.showToast(context, "Server side error.");
        }

    }


    private OkHttpClient createAuthenticatedClient(final String username, final String password)
    {

        // build client with authentication information.
        OkHttpClient httpClient = new OkHttpClient.Builder().authenticator(new Authenticator()
        {
            int mCounter = 0;

            public Request authenticate(Route route, Response response) throws IOException
            {

                String credential = Credentials.basic(username, password);

                if (credential.equals(response.request().header("Authorization"))) {
                    return null; // If we already failed with these credentials, don't retry.
                }

                return response.request().newBuilder().header("Authorization", credential).build();
            }


        }).build();
        return httpClient;
    }


    private String doRequest(OkHttpClient httpClient, String anyURL) throws Exception
    {
        Request request = new Request.Builder().url(anyURL).build();
        Response response = httpClient.newCall(request).execute();
//        if (!response.isSuccessful())
//        {
//            throw new IOException("Unexpected code " + response);
//
//        }
//        System.out.println(response.body().string());
        return response.body().string();
    }


    public String fetch(String url, String username, String password) throws Exception
    {

        OkHttpClient httpClient = createAuthenticatedClient(username, password);
        // execute request

            return doRequest(httpClient, url);


    }




   /* Helper classes*/

    public String performOkGetPostCall(String requestURL, HashMap<String, String> postDataParams)
    {

        String responseString = "";
        try
        {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

            Request request;

            if (inputData != null)
            {
                RequestBody body = RequestBody.create(mediaType, getPostDataString(postDataParams));

                request = new Request.Builder().url(requestURL)
                                               .post(body)
                                               .addHeader("cache-control", "no-cache")
                                               .addHeader("content-type", "application/x-www-form-urlencoded").build();
            }
            else
            {

                request = new Request.Builder().url(requestURL).get().addHeader("cache-control", "no-cache").build();

            }

            Response response = client.newCall(request).execute();

            responseString = response.body().string();

        } catch (SocketTimeoutException e)
        {
            e.printStackTrace();
            return "SLOW";
        } catch (ConnectTimeoutException e)
        {
            e.printStackTrace();
            return "SLOW";
        } catch (NullPointerException e)
        {
            e.printStackTrace();
            return "ERROR";
        } catch (IOException e)
        {
            e.printStackTrace();
            return "ERROR";
        } catch (Exception e)
        {
            e.printStackTrace();
            return "ERROR";
        }

        return responseString;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet())
        {
            if (first)
            {
                first = false;
            }
            else
            {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


}
