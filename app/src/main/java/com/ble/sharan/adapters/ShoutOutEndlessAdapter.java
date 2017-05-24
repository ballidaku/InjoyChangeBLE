package com.ble.sharan.adapters;

/*
 * Copyright (C) 2012 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.apiModels.ApiClient;
import com.ble.sharan.apiModels.ApiInterface;
import com.ble.sharan.apiModels.ShoutOutModel;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoutOutEndlessAdapter extends ArrayAdapter<ShoutOutModel.SubData> {

    String TAG =ShoutOutEndlessAdapter.class.getSimpleName();

    private List<ShoutOutModel.SubData> itemList;
    private Context context;
    private int layoutId;
    LayoutInflater inflater;

    MyUtil myUtil=new MyUtil();

    public ShoutOutEndlessAdapter(Context context, List<ShoutOutModel.SubData> itemList, int layoutId) {
        super(context, layoutId, itemList);
        this.itemList = itemList;
        this.context = context;
        this.layoutId = layoutId;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ShoutOutModel.SubData subData = itemList.get(position);

        convertView = inflater.inflate(R.layout.custom_shoutout_list, null);
        if (position % 2 == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorGrayWhite));
        }

        ImageView profile_iv = (ImageView) convertView.findViewById(R.id.profile_iv);

        TextView txtv_name = (TextView) convertView.findViewById(R.id.txtv_name);
        TextView txtv_time = (TextView) convertView.findViewById(R.id.txtv_time);
        TextView txtv_likes = (TextView) convertView.findViewById(R.id.txtv_likes);
        TextView txtv_comment = (TextView) convertView.findViewById(R.id.txtv_comment);
        TextView txtv_highFive = (TextView) convertView.findViewById(R.id.txtv_highFive);


        txtv_name.setText(subData.getName());
        txtv_time.setText(subData.getTime());
        txtv_likes.setText(String.valueOf(subData.getLikes()));
        txtv_comment.setText(subData.getOuterComment());

        final String highFive=subData.getHighFiveStatus();
        if(highFive.equals(MyConstant.TRUE))
        {
            txtv_highFive.setBackgroundResource(R.drawable.blue_background_round_corners);
        }


        myUtil.showCircularImageWithPicasso(context,profile_iv,subData.getImage());


        txtv_highFive.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!highFive.equals(MyConstant.TRUE))
                {
                    ADD_HIGH_FIVE_RETROFIT(subData.getId(),position);
                }
            }
        });


        return convertView;

    }


    // RETROFIT
    public void ADD_HIGH_FIVE_RETROFIT(String id,final int position)
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ShoutOutModel> call = apiService.submitHighFive(myUtil.getCurrentTimeStamp(),id, MySharedPreference.getInstance().getUID(context));

        call.enqueue(new Callback<ShoutOutModel>()
        {
            @Override
            public void onResponse(Call<ShoutOutModel> call, Response<ShoutOutModel> response)
            {
                MyUtil.myLog(TAG, "Response----"+response.body());

                ShoutOutModel shoutOutModel = response.body();

                if(shoutOutModel.getStatus().equals(MyConstant.TRUE))
                {
                    itemList.get(position).setHighFiveStatus(MyConstant.TRUE);
                    itemList.get(position).addLikes(shoutOutModel.getLikes());
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ShoutOutModel> call, Throwable t)
            {
                MyUtil.myLog(TAG, t.getMessage());
                MyUtil.showToast(context, "Server side error");

            }
        });
    }
}


