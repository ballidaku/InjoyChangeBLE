package com.ble.sharan.splashScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.ble.sharan.R;
import com.ble.sharan.loginScreen.LoginActivity;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MySharedPreference;

public class Splash extends AppCompatActivity
{
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        context=this;


        /*GeometricProgressView progressView15 = (GeometricProgressView) findViewById(R.id.progressView15);
        progressView15.setType(GeometricProgressView.TYPE.KITE);
        progressView15.setFigurePaddingInDp(1);
        progressView15.setNumberOfAngles(30);*/


        c.start();

    }

    CountDownTimer c = new CountDownTimer(2000, 1000)
    {

        public void onTick(long millisUntilFinished)
        {

        }

        public void onFinish()
        {

            if(MySharedPreference.getInstance().getUID(context).isEmpty())
            {
                Intent intent = new Intent(context, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent = new Intent(context, MainActivityNew.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }



        }
    };
}
