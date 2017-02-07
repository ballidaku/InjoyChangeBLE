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

public class CheckInAdapter extends BaseAdapter
{
    Context context;
    int images[];
    String[] stringvalue, num_value;
    LayoutInflater inflator;

    public CheckInAdapter(Context context, int[] images, String[] stringvalue, String[] num_value)
    {
        this.context = context;
        this.stringvalue = stringvalue;
        this.num_value = num_value;
        this.images = images;
        inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return images.length;
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
        view = inflator.inflate(R.layout.custom_checkin_list, null);

        ImageView circle_image = (ImageView) view.findViewById(R.id.circle_image);

        TextView txtv_heading = (TextView) view.findViewById(R.id.txtv_heading);

        TextView num_tv = (TextView) view.findViewById(R.id.num_tv);

        circle_image.setImageResource(images[i]);
        txtv_heading.setText(stringvalue[i]);
        num_tv.setText(num_value[i]);

        return view;
    }
}