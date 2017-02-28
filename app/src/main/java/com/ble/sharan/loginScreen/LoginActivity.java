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
                            MySharedPreference.getInstance().saveUID(context, UID);


                        if (!name.isEmpty() && !name.equals("null"))
                        {
                            MySharedPreference.getInstance().saveName(context, name);
                        }

//                        if (!gender.isEmpty())
//                            MySharedPreference.getInstance().saveGender(context, gender);

                        if (!height.isEmpty()  && !height.equals("null"))
                        {
                            MySharedPreference.getInstance().saveHeight(context, height);
                        }

                        if (!weight.isEmpty() && !weight.equals("null"))
                        {
                            MySharedPreference.getInstance().saveWeight(context, weight);
                        }


                        if (!stride.isEmpty() && !stride.equals("null"))
                        {
                            MySharedPreference.getInstance().saveStride(context, stride);
                        }


                        if (!profile_picture.isEmpty() && !profile_picture.equals("null"))
                            MySharedPreference.getInstance().savePhoto(context, profile_picture);


                        Intent intent = new Intent(context, MainActivityNew.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

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

   /* @Override
    public void onNetworkConnectionChanged(boolean isConnected)
    {

    }*/



}
