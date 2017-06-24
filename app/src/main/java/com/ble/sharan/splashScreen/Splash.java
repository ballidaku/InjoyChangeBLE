package com.ble.sharan.splashScreen;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.ble.sharan.R;
import com.ble.sharan.loginScreen.LoginActivity;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.AbsRuntimeMarshmallowPermission;
import com.ble.sharan.myUtilities.MySharedPreference;

public class Splash extends AbsRuntimeMarshmallowPermission
{
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;


        // this is to check whether app is in background or not, if yes finish the new one
        if (!isTaskRoot()
                  && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                  && getIntent().getAction() != null
                  && getIntent().getAction().equals(Intent.ACTION_MAIN))
        {

            finish();
            return;
        }


        String[] permission = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};

        requestAppPermissions(permission, R.string.permission, 54);

    }

    @Override
    public void onPermissionGranted(int requestCode)
    {

        if (requestCode == 54)
        {
            c.start();
        }

    }

    CountDownTimer c = new CountDownTimer(2000, 1000)
    {

        public void onTick(long millisUntilFinished)
        {

        }

        public void onFinish()
        {

            if (MySharedPreference.getInstance().getUID(context).isEmpty())
            {
                Intent intent = new Intent(context, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
            else
            {
                Intent intent = new Intent(context, MainActivityNew.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        }
    };


}
