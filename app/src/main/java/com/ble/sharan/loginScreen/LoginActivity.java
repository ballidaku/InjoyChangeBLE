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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{

    View view;

    Context context;

    EditText edtv_userName;
    EditText edtv_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        view = findViewById(R.id.main);

        context = this;





        setUpIds();

    }

    private void setUpIds()
    {

        edtv_userName = (EditText) findViewById(R.id.edtv_userName);
        edtv_Password = (EditText) findViewById(R.id.edtv_Password);


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

                checkHit();

//                startActivity(new Intent(context, MainActivityNew.class));
//                finish();

//                TestingSleep2();


                break;

            case R.id.txtv_forgotPassword:

                hitApi();

                break;

            case R.id.txtv_createAccount:

                hitApi();

                break;
        }
    }


    public void hitApi()
    {
        String url = MyConstant.MAIN_API;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void checkHit()
    {
       String userName= edtv_userName.getText().toString().trim();
       String password= edtv_Password.getText().toString().trim();


        if(userName.isEmpty())
        {
            MyUtil.show_snackbar(view,context, "Please enter the username");
        }
        else  if(password.isEmpty())
        {
            MyUtil.show_snackbar(view,context, "Please enter the password");
        }
        else
        {
            HashMap<String,String>map=new HashMap<>();
            map.put(MyConstant.USER_NANE,userName);
            map.put(MyConstant.PASSWORD,password);


            LoginService(map);
        }


    }



    /*Login Service*/

    public void LoginService(HashMap<String, String> map) {


        MyUtil.execute(new Super_AsyncTask(context, map, MyConstant.SIGN_IN,MyConstant.LOGIN_ACTIVITY, new Super_AsyncTask_Interface() {

            @Override
            public void onTaskCompleted(String output) {


                Log.e("OUTPUT",""+output);

                try {

                    JSONObject object = new JSONObject(output);

                    if(object.getString(MyConstant.STATUS).equals("200"))
                    {
                        String ACCESS_TOKEN= object.getString(MyConstant.ACCESS_TOKEN);


                        if(!ACCESS_TOKEN.isEmpty())
                        MySharedPreference.getInstance().saveAccessToken(context, ACCESS_TOKEN);



                        JSONObject object2= object.getJSONObject("profile");

                        String name=object2.getString("fullname");
                        String gender=object2.getString("gender");
                        String height=object2.getString("height");
                        String weight=object2.getString("weight");
                        String stride=object2.getString("stride");
                        String profile_picture=object2.getString("profile_picture");

                        if(!name.isEmpty())
                        MySharedPreference.getInstance().saveName(context, name);

                        if(!gender.isEmpty())
                        MySharedPreference.getInstance().saveGender(context, gender);

                        if(!height.isEmpty())
                        MySharedPreference.getInstance().saveHeight(context, height);

                        if(!weight.isEmpty())
                        MySharedPreference.getInstance().saveWeight(context, weight);

                        if(!stride.isEmpty())
                        MySharedPreference.getInstance().saveStride(context, stride);

                        if(!profile_picture.isEmpty())
                        MySharedPreference.getInstance().savePhoto(context, profile_picture);





                        Intent intent = new Intent(context, MainActivityNew.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        finish();
                    }
                    else  if(object.getString(MyConstant.STATUS).equals("401"))
                    {
                        MyUtil.show_snackbar(view,context, "Username and password did not match");
                    }


                }
                catch (Exception ex) {
                    Log.e("Exception is", ex.toString());
                }

            }
        }, true));
    }






//    public void TestingSleep()
//    {
//
//        long totalTime = 0;
//
////        String h = "00037C0000000000001007010F03005C0F2F0006";
//
////        String remaining = h.substring(8, h.length());
//
////        String remaining = "00000000001007010F03005C0F2F0006100A0006102E000B10300002111000021128000C1129000511330005113B0002120000081211001D130D0003131000221401000A14070007140A0004140F0001141100171413001E1414000D1417000E1418000F1419001FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
//
//
////        String remaining = "0000000000110105001800A00023000F002500080101000901020004010F000C0110001D01120018011300140136000F0137000C0200000B0204000902050011020B00070212000902190007021A000A0220000F02270018022A0005022B0007022D000B0305000103080001030D000B0318000C03190045031A0001031E0014033300180339001C04030002040400020405000B040800100411000B0418000B041B000F041C002D041D000B0000000000110105051D002C0523001A05250004053000060533000B0604001B06070019060D001C061C000F061D0008061F000106320050FFFFFFFFFFFFFFFFFFFFFFFF";
////        String remaining = "0000000000110105161D000C161E001A161F0040162000220000000000110106011C000C01330011013400100135000D0000000000110106020B001002160011031A00010336000A04070050000000000011010605070044052C001205320023053700190601000606080007060E000506110001061A0002061B001106220008062800020708002D0714000A071F002B073700080738000B07390017000000000011010B1611000416370014000000000011010C16090008160F000117170008000000000011010D1108000411110050000000000011010D1203007412040001120500021206000212080002120A0002120B0003120C0001120E0001120F000212100002122800141229004E122C0002123000091235000112360001130000011302000213030001130500071311000113140001131A0001132B0001132C0001132E000113300001133A0002140A0050000000000011010E093400040A080050000000000011010F0410007C04260008042F000104390011051E0018051F001E060A0022060B0004060D0031060E000C061800150619004D061A0001062E0001070700180708000F072200140726000E080D0010080F00070810000108110002081200100813000608170006081800240819000108250015082700020828001408290012082A004000000000001101100217000C022E002E022F00120230003100000000001101100312000C03290034032A001E032B000A0000000000110110052A0040062A00170630001506380014071D0010072200050723001E072B000207390005080B0006081A0002081B001F081C000309080019092C00180930000A09310050FFFFFFFFFFFFFFFFFFFFFFFF2130783030303030303237";
//
//        String remaining="00000000001007010F03005C0F2F0006100A0006102E000B10300002111000021128000C1129000511330005113B0002120000081211001D130D0003131000221401000A14070007140A0004140F0001141100171413001E1414000D1417000E1418000F1419001F000000000011010E1439000C143A0006143B000C15000021000000000011010E15330014161B0005161E000116240018162E002B162F000400000000001101100019000C001D0005001E000101240001FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF2130783030303030303044";
//        String[] c = remaining.split("0000000000");
//
//
//        int containgStringCount = 0;
//
//        for (int j = 0; j < c.length - 1; j++)
//        {
//            containgStringCount++;
//
//            String remainingLast = c[containgStringCount];
//
//            /*if (remaining.substring(0, 10).equals(0000000000))
//            {*/
//            // String realData = remainingLast.substring(10, remainingLast.length());
//
//            Log.e("realData", remainingLast);
//
//            int count = 0;
//
//            String date = "";
//            String startTime = "";
//            String endTime = "";
//
//            int totalBytes = 0;
//
//            for (int i = 0; i < remainingLast.length(); i += 2)
//            {
//                String str = remainingLast.substring(i, i + 2);
//                int value = Integer.parseInt(str, 16);
//
//                count++;
//
//                if (count <= 3)
//                {
//                    date = date.isEmpty() ? String.valueOf(value) : date + "-" + String.valueOf(value);
//                }
//                else if (count <= 5)
//                {
//                    startTime = startTime.isEmpty() ? String.valueOf(value) : startTime + ":" + String.valueOf(value);
//                }
//                else if (count <= 7)
//                {
//                    totalBytes += value;
//
//                    Log.e("Bytes", "" + totalBytes);
//                }
//                else if (count <= totalBytes + 7)
//                {
//                    Log.e("Inside", count + "----" + str + "----" + Integer.parseInt(str, 16));
//
//                    if (count == totalBytes + 7 - 3)
//                    {
//                        Log.e("InsideCount", "" + count);
//                        endTime = String.valueOf(value);
//                    }
//                    else if (count == totalBytes + 7 - 2)
//                    {
//                        Log.e("InsideCount", "" + count);
//                        endTime = endTime + ":" + String.valueOf(value);
//                    }
//                }
//                else
//                {
//                    Log.e("Outside", str);
//                }
//
//            }
//
//            Log.e("date", date);
//            Log.e("startTime", startTime);
//            Log.e("endTime", endTime);
//            Log.e("totalBytes", "" + totalBytes);
//
//
//            try
//            {
//
//                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//                Date Date1 = format.parse(startTime);
//                Date Date2 = format.parse(endTime);
//                long mills = Date2.getTime() - Date1.getTime();
//
//                totalTime += mills;
//                Log.e("Data1", "" + Date1.getTime());
//                Log.e("Data2", "" + Date2.getTime());
//                int Hours = (int) (mills / (1000 * 60 * 60));
//                int Mins = (int) (mills / (1000 * 60)) % 60;
//
//                String diff = Hours + ":" + Mins; // updated value every1 second
//                Log.e("Time ", "" + diff);
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//
//        }
//
//
//        try
//        {
//            int Hours = (int) (totalTime / (1000 * 60 * 60));
//            int Mins = (int) (totalTime / (1000 * 60)) % 60;
//
//            String diff = Hours + ":" + Mins; // updated value every1 second
//            Log.e("Time Final", "" + diff);
//
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
}
