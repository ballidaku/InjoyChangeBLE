package com.ble.sharan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by brst-pc93 on 2/10/17.
 */

public class DailyInspirationSeeAllAdapter extends BaseAdapter
{
    Context context;

    LayoutInflater inflator;

    ArrayList<HashMap<String, String>> list;


    MyUtil myUtil=new MyUtil();

    public DailyInspirationSeeAllAdapter(Context context, ArrayList<HashMap<String, String>>list)
    {
        this.context = context;
        this.list = list;
        inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        view = inflator.inflate(R.layout.custom_daily_inspiration_see_all_item, null);

        final ImageView imgv_logo = (ImageView) view.findViewById(R.id.imgv_logo);
        imgv_logo.setAdjustViewBounds(true);

        myUtil.showImageWithPicasso(context,imgv_logo,list.get(i).get(MyConstant.IMAGE));

        return view;
    }
}