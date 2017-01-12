package com.ble.sharan.loginScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{

    Context context;

    EditText edtv_userName;
    EditText edtv_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context=this;


        setUpIds();

    }

    private void setUpIds()
    {

        edtv_userName=(EditText)findViewById(R.id.edtv_userName);
        edtv_Password=(EditText)findViewById(R.id.edtv_Password);


        findViewById(R.id.txtv_signIn).setOnClickListener(this);
        findViewById(R.id.txtv_forgotPassword).setOnClickListener(this);
        findViewById(R.id.txtv_createAccount).setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtv_signIn:

                startActivity(new Intent(context, MainActivityNew.class));
                finish();

                break;

            case R.id.txtv_forgotPassword:

                break;

            case R.id.txtv_createAccount:

                break;
        }
    }
}
