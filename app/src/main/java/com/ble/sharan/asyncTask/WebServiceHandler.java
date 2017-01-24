package com.ble.sharan.asyncTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Gagan on 8/18/15.
 */
public class WebServiceHandler
{

/*    public String performPostCall(String requestURL, HashMap<String, String> postDataParams)
    {

        URL url;
        String response = "";
        try
        {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //  conn.setReadTimeout(15000);
            //conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.addRequestProperty("Cache-Control", "no-cache");
            conn.addRequestProperty("Content-Type", "text/plain; charset=utf-8");

//            conn.setChunkedStreamingMode(1024);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK)
            {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = br.readLine()) != null)
                {
                    response += line;
                }
            }
            else
            {
                response = "ERROR";

                // throw new Exception(responseCode + "");
            }
        }
      *//*  catch (Exception e)
        {
            response = "ERROR";
            e.printStackTrace();
        }*//*
        catch (SocketTimeoutException e)
        {
            e.printStackTrace();
            return "SLOW";
        }
        catch (ConnectTimeoutException e)
        {
            e.printStackTrace();
            return "SLOW";
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            return "ERROR";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "SLOW";
        }

        return response;
    }*/

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

   /* public String performGetCall(String url) throws Exception
    {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //print result

//        Log.e("==getCall response====", response.toString());
        return response.toString(); //this is your response

    }*/

    String res = "";

    public String getPostDataResponse(String url, HashMap<String, String> params) throws UnsupportedEncodingException
    {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, getPostDataString(params));
        Request request = new Request.Builder().url(url).post(body).addHeader("cache-control", "no-cache").addHeader("content-type", "application/x-www-form-urlencoded").build();

        try
        {
            Response response = client.newCall(request).execute();

            return res = response.body().string();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return res;

    }

    public String getGetDataResponse(String URL) throws UnsupportedEncodingException
    {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(URL).get().addHeader("cache-control", "no-cache").build();

//        Response response = client.newCall(request).execute();}


        try
        {
            Response response = client.newCall(request).execute();

            return res = response.body().string();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return res;
    }












}