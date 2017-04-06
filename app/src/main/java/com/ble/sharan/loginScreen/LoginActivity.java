package com.ble.sharan.loginScreen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ble.sharan.R;
import com.ble.sharan.asyncTask.Super_AsyncTask;
import com.ble.sharan.asyncTask.Super_AsyncTask_Interface;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener/*, ConnectivityReceiver.ConnectivityReceiverListener*/
{

    String TAG=LoginActivity.class.getSimpleName();


    View view;

    Context context;

    EditText edtv_userName;
    EditText edtv_Password;

    MyUtil myUtil = new MyUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        view = findViewById(R.id.main);

        context = this;


//       Log.e("CONNECTION","------------"+ myUtil.checkConnection());


        setUpIds();

    }

    private void setUpIds()
    {

        edtv_userName = (EditText) findViewById(R.id.edtv_userName);
        edtv_Password = (EditText) findViewById(R.id.edtv_Password);


        findViewById(R.id.txtv_signIn).setOnClickListener(this);
//        findViewById(R.id.txtv_forgotPassword).setOnClickListener(this);
//        findViewById(R.id.txtv_createAccount).setOnClickListener(this);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
//        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtv_signIn:

                checkHit();

//                startActivity(new Intent(context, MainActivityNew.class));
//                finish();
//                TestingSleep2();


                break;

           /* case R.id.txtv_forgotPassword:

                hitApi(MyConstant.FORGOT_PASSWORD);

                break;

            case R.id.txtv_createAccount:

                hitApi(MyConstant.SIGN_UP);

                break;*/
        }
    }


    public void hitApi(String url)
    {
        if (!myUtil.checkConnection())
        {
            MyUtil.show_snackbar(view, context, "Please check your internet connection");
        }
        else
        {

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

    private void checkHit()
    {
        String userName = edtv_userName.getText().toString().trim();
        String password = edtv_Password.getText().toString().trim();


        if (!myUtil.checkConnection())
        {
            MyUtil.show_snackbar(view, context, "Please check your internet connection");
        }
        else if (userName.isEmpty())
        {
            MyUtil.show_snackbar(view, context, "Please enter the username");
        }
        else if (password.isEmpty())
        {
            MyUtil.show_snackbar(view, context, "Please enter the password");
        }
        else
        {
            HashMap<String, String> map = new HashMap<>();
            map.put(MyConstant.USER_NANE, userName);
            map.put(MyConstant.PASSWORD, password);


            LoginService(map);

//            LOGIN(userName,password);
        }





    }

    /*Login Service*/

    public void LoginService(HashMap<String, String> map)
    {


        MyUtil.execute(new Super_AsyncTask(context, map, MyConstant.SIGN_IN, MyConstant.LOGIN_ACTIVITY, new Super_AsyncTask_Interface()
        {

            @Override
            public void onTaskCompleted(String output)
            {


                Log.e("OUTPUT", "" + output);

                try
                {

                    JSONObject object = new JSONObject(output);

                    if (object.getString(MyConstant.STATUS).equals("200"))
                    {
                        JSONObject object2 = object.getJSONObject("profile");

                        String name = object2.getString("fullname");
                        // String gender = object2.getString(MyConstant.GENDER);
                        String height = object2.getString(MyConstant.HEIGHT);
                        String weight = object2.getString(MyConstant.WEIGHT);
                        String stride = object2.getString(MyConstant.STRIDE);
                        String profile_picture = object2.getString("profile_picture");
                        String UID = object2.getString(MyConstant.UID);


                        if (!UID.isEmpty())
                        {
                            MySharedPreference.getInstance().saveUID(context, UID);
                        }
                        else
                        {
                            MySharedPreference.getInstance().saveUID(context, "");
                        }


                        if (!name.isEmpty() && !name.equals("null"))
                        {
                            MySharedPreference.getInstance().saveName(context, name);
                        }
                        else
                        {
                            MySharedPreference.getInstance().saveName(context, "");
                        }

//                        if (!gender.isEmpty())
//                            MySharedPreference.getInstance().saveGender(context, gender);

                        if (!height.isEmpty() && !height.equals("null"))
                        {
                            MySharedPreference.getInstance().saveHeight(context, height);
                        }
                        else
                        {
                            MySharedPreference.getInstance().saveHeight(context, "");
                        }

                        if (!weight.isEmpty() && !weight.equals("null"))
                        {
                            MySharedPreference.getInstance().saveWeight(context, weight);
                        }
                        else
                        {
                            MySharedPreference.getInstance().saveWeight(context, "");
                        }


                        if (!stride.isEmpty() && !stride.equals("null"))
                        {
                            MySharedPreference.getInstance().saveStride(context, stride);
                        }
                        else
                        {
                            MySharedPreference.getInstance().saveStride(context, "");
                        }


                        if (!profile_picture.isEmpty() && !profile_picture.equals("null"))
                        {
                            MySharedPreference.getInstance().savePhoto(context, profile_picture);
                        }
                        else
                        {
                            MySharedPreference.getInstance().savePhoto(context, "");
                        }

                        Intent intent = new Intent(context, MainActivityNew.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                        finish();
                    }
                    else if (object.getString(MyConstant.STATUS).equals("401"))
                    {
                        MyUtil.show_snackbar(view, context, "Username and password did not match");
                    }


                } catch (Exception ex)
                {
                    Log.e("Exception is", ex.toString());
                }

            }
        }, true));
    }

    /*Retrofit retrofit = null;

    public class ServiceGenerator {


        private  OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        private  Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(MyConstant.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        public  <S> S createService(Class<S> serviceClass) {
            return createService(serviceClass, null, null);
        }

        public  <S> S createService(
                Class<S> serviceClass, String username, String password) {
            if (!TextUtils.isEmpty(username)
                    && !TextUtils.isEmpty(password)) {
                String authToken = Credentials.basic(username, password);
                return createService(serviceClass, authToken);
            }

            return createService(serviceClass, null, null);
        }

        public  <S> S createService(
                Class<S> serviceClass, final String authToken) {
            if (!TextUtils.isEmpty(authToken)) {
                AuthenticationInterceptor interceptor =
                        new AuthenticationInterceptor(authToken);

                if (!httpClient.interceptors().contains(interceptor)) {
                    httpClient.addInterceptor(interceptor);

                    builder.client(httpClient.build());
                    retrofit = builder.build();
                }
            }

            return retrofit.create(serviceClass);
        }
    }

    public class AuthenticationInterceptor implements Interceptor
    {

        private String authToken;

        public AuthenticationInterceptor(String token) {
            this.authToken = token;
        }

        @Override
        public Response intercept(Chain chain) throws IOException
        {
            Request original = chain.request();

            Request.Builder builder = original.newBuilder()
                                              .header("Authorization", authToken);

            Request request = builder.build();
            return chain.proceed(request);
        }
    }








    public void LOGIN(String userName, String password)
    {
        ServiceGenerator serviceGenerator=new ServiceGenerator();
        ApiInterface loginService = serviceGenerator.createService(ApiInterface.class, userName, password);
        Call<LoginModel> call = loginService.basicLogin();

        call.enqueue(new Callback<LoginModel>()
        {
            @Override
            public void onResponse(Call<LoginModel> call, retrofit2.Response<LoginModel> response)
            {
                Log.e(TAG, "Response----" + response.isSuccessful());
                Log.e(TAG, "Response----" + response.message());
                Log.e(TAG, "Response----" + response.raw());

                *//*LoginModel loginModel = response.body();

                if (loginModel.getStatus().equals("200"))
                {
                    Log.e(TAG, "NAME----" + loginModel.getProfile().getFullName());
                }*//*
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t)
            {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
            }
        });
    }*/


}
