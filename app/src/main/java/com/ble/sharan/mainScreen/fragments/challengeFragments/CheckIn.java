package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.CheckInAdapter;
import com.ble.sharan.adapters.CheckInCommentAdapter;
import com.ble.sharan.apiModels.ApiClient;
import com.ble.sharan.apiModels.ApiInterface;
import com.ble.sharan.apiModels.CheckInModel;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MyDialogs;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class CheckIn extends Fragment implements View.OnClickListener
{

    String TAG = CheckIn.class.getSimpleName();

    Context context;
    View view;


    int images[] = {
            R.mipmap.ic_steps_small,
            R.mipmap.ic_calories_small,
            R.mipmap.ic_km_miles_small,
            R.mipmap.ic_sleep_small
    };

    String stringvalue[] = {"STEPS", "CALORIES", "MILES", "SLEEP"};

    TextView txtv_date;
    TextView txtv_submit;


    MyDatabase myDatabase;

    MyUtil myUtil = new MyUtil();

    ListView listViewCheckIn;
    ListView listViewCheckInComment;

    EditText edtv_checkIn;


    ImageView check1_iv;
    ImageView check2_iv;

    FrameLayout frameLayoutSubmit;

    MyDialogs myDialogs = new MyDialogs();


    int commentCount = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_checkin, container, false);


            myDatabase = new MyDatabase(context);
            setUpIds();

        }

        GET_DATA_FROM_SERVER_RETROFIT();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ((MainActivityNew) getActivity()).setTitleHeader("Check In With Yourself");


    }

    private void setUpIds()
    {
        int steps = myDatabase.getTodaySteps(context);

        HashMap<String, String> map = new HashMap<>();
        map.put(MyConstant.STEPS, String.valueOf(steps));
        map.put(MyConstant.CALORIES, myUtil.stepsToCalories(context, steps));
        map.put(MyConstant.DISTANCE, myUtil.stepsToDistance(context, steps));
        map.put(MyConstant.SLEEP, myUtil.getSleepHr(context, myDatabase));


        txtv_date = (TextView) view.findViewById(R.id.txtv_date);
        txtv_submit = (TextView) view.findViewById(R.id.txtv_submit);

        edtv_checkIn = (EditText) view.findViewById(R.id.edtv_checkIn);

        listViewCheckIn = (ListView) view.findViewById(R.id.listViewCheckIn);
        listViewCheckInComment = (ListView) view.findViewById(R.id.listViewCheckInComment);


        check1_iv = (ImageView) view.findViewById(R.id.check1_iv);
        check2_iv = (ImageView) view.findViewById(R.id.check2_iv);

        frameLayoutSubmit = (FrameLayout) view.findViewById(R.id.frameLayoutSubmit);

        frameLayoutSubmit.setOnClickListener(this);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEEE, MMM dd");

        txtv_date.setText(df.format(c.getTime()));

        CheckInAdapter checkInAdapter = new CheckInAdapter(context, images, stringvalue, map);
        listViewCheckIn.setAdapter(checkInAdapter);

        myUtil.setListViewHeight(listViewCheckIn);

    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.frameLayoutSubmit:

                if (commentCount > 0)
                {
                    edtv_comment = myDialogs.showShareWinCheckInDialog(context, "CheckIn", onClickListenerShareWin);
                }
                else
                {
                    String comm = edtv_checkIn.getText().toString().trim();

                    if (!comm.isEmpty())
                    {
                        ADD_COMMENT_RETROFIT(comm);
                    }
                    else
                    {
                        MyUtil.showToast(context, "Please enter comment");
                    }
                }

                break;
        }
    }

    EditText edtv_comment;

    View.OnClickListener onClickListenerShareWin = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            String comment = edtv_comment.getText().toString().trim();

            if (!comment.isEmpty())
            {

                myDialogs.dialog.dismiss();
                ADD_COMMENT_RETROFIT(comment);

            }
            else
            {
                MyUtil.showToast(context, "Please enter comment");
            }

        }
    };

    private void setData(ArrayList<CheckInModel.Comment> list)
    {
        CheckInCommentAdapter checkInCommentAdapter = new CheckInCommentAdapter(context, list);
        listViewCheckInComment.setAdapter(checkInCommentAdapter);

        listViewCheckInComment.setOnTouchListener(new ListView.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                int action = event.getAction();
                switch (action)
                {
                    case MotionEvent.ACTION_DOWN:

                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:

                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

    }


    // RETROFIT
    public void GET_DATA_FROM_SERVER_RETROFIT()
    {


        myUtil.showProgressDialog(context);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<CheckInModel> call = apiService.getCheckInComment(myUtil.getCurrentTimeStamp(), MySharedPreference.getInstance().getUID(context));

        call.enqueue(new Callback<CheckInModel>()
        {
            @Override
            public void onResponse(Call<CheckInModel> call, Response<CheckInModel> response)
            {

                myUtil.hideProgressDialog();
                MyUtil.myLog(TAG, "Response----" + response.body());

                CheckInModel checkInModel = response.body();


                //MyUtil.myLog("checkInModel.getStatus()",""+checkInModel.getStatus());
                if (checkInModel.getStatus().equals(MyConstant.TRUE))
                {

                    edtv_checkIn.setVisibility(View.GONE);

                    commentCount = checkInModel.getCount();

                    if (commentCount >= 2)
                    {
                        check1_iv.setImageResource(R.mipmap.ic_check);
                        check2_iv.setImageResource(R.mipmap.ic_check);

                        txtv_submit.setText("Add More");
                    }
                    else if (commentCount == 1)
                    {
                        check1_iv.setImageResource(R.mipmap.ic_check);
                    }


                    ArrayList<CheckInModel.Comment> list = checkInModel.getCheckInCommentList();

                    setData(list);

                }
                else
                {
                    edtv_checkIn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<CheckInModel> call, Throwable t)
            {
                myUtil.hideProgressDialog();
                MyUtil.myLog(TAG, t.getMessage());
//                MyUtil.showToast(context, "Server side error");

            }
        });
    }


    // RETROFIT
    public void ADD_COMMENT_RETROFIT(String comment)
    {

        myUtil.hide_keyboard2(view);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<CheckInModel> call = apiService.addCheckInComment(comment, myUtil.getCurrentTimeStamp(), MySharedPreference.getInstance().getUID(context));

        call.enqueue(new Callback<CheckInModel>()
        {
            @Override
            public void onResponse(Call<CheckInModel> call, Response<CheckInModel> response)
            {
                MyUtil.myLog(TAG, "Response----" + response.body());

                CheckInModel checkInModel = response.body();

                if (checkInModel.getStatus().equals(MyConstant.TRUE))
                {

                    commentCount = checkInModel.getCount();

                    if (commentCount >= 2)
                    {
                        check1_iv.setImageResource(R.mipmap.ic_check);
                        check2_iv.setImageResource(R.mipmap.ic_check);
                    }
                    else if (commentCount == 1)
                    {
                        check1_iv.setImageResource(R.mipmap.ic_check);
                    }

                    GET_DATA_FROM_SERVER_RETROFIT();


                }
            }

            @Override
            public void onFailure(Call<CheckInModel> call, Throwable t)
            {
                myUtil.hideProgressDialog();
                MyUtil.myLog(TAG, t.getMessage());
//                MyUtil.showToast(context, "Server side error");

            }
        });
    }


}