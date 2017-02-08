package com.ble.sharan.myUtilities;

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

import java.util.HashMap;
import java.util.List;

public class EndlessAdapter extends ArrayAdapter<HashMap> {

    private List<HashMap> itemList;
    private Context context;
    private int layoutId;
    LayoutInflater inflater;

    MyUtil myUtil=new MyUtil();

    public EndlessAdapter(Context context, List<HashMap> itemList, int layoutId) {
        super(context, layoutId, itemList);
        this.itemList = itemList;
        this.context = context;
        this.layoutId = layoutId;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HashMap hashMap = itemList.get(position);

        convertView = inflater.inflate(R.layout.custom_shoutout_list, null);
        if (position % 2 == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorGrayWhite));
        }

        ImageView profile_iv = (ImageView) convertView.findViewById(R.id.profile_iv);

        TextView txtv_name = (TextView) convertView.findViewById(R.id.txtv_name);
        TextView txtv_time = (TextView) convertView.findViewById(R.id.txtv_time);
        TextView txtv_likes = (TextView) convertView.findViewById(R.id.txtv_likes);


        txtv_name.setText(hashMap.get(MyConstant.NAME).toString());
        txtv_time.setText(hashMap.get(MyConstant.TIME).toString());
        txtv_likes.setText(hashMap.get(MyConstant.LIKES).toString());


        myUtil.showImageWithPicasso(context,profile_iv,hashMap.get(MyConstant.IMAGE).toString());



        return convertView;

    }
}


