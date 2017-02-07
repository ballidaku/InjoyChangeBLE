package com.ble.sharan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ble.sharan.R;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class ChallengeAdapter extends BaseAdapter
{
    Context context;
    String[] points;
    LayoutInflater inflator;

    public ChallengeAdapter(Context context, String[] points)
    {
        this.points = points;
        this.context = context;
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return points.length;
    }

    @Override
    public Object getItem(int i)
    {
        return 0;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {

        view = inflator.inflate(R.layout.custom_challenge_list, null);
        ImageView circle_image = (ImageView) view.findViewById(R.id.circle_image);
        TextView list_tv = (TextView) view.findViewById(R.id.list_tv);
        ImageView expand_image = (ImageView) view.findViewById(R.id.expand_image);
        list_tv.setText(points[position]);


        return view;
    }
}
