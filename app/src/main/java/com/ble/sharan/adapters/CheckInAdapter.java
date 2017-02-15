package com.ble.sharan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.MyConstant;

import java.util.HashMap;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class CheckInAdapter extends BaseAdapter
{
    Context context;
    int images[];
    String[] stringvalue;
    LayoutInflater inflator;
    HashMap<String,String> map;

    public CheckInAdapter(Context context, int[] images, String[] stringvalue, HashMap<String,String> map)
    {
        this.context = context;
        this.stringvalue = stringvalue;
        this.map = map;
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

        if(i==0)
        {
            num_tv.setText(map.get(MyConstant.STEPS));
        }
        else if(i==1)
        {
            num_tv.setText(map.get(MyConstant.CALORIES));
        }
        else if(i==2)
        {
            num_tv.setText(map.get(MyConstant.DISTANCE));
        }
        else if(i==3)
        {
            String[] s= map.get(MyConstant.SLEEP).split(":");
            num_tv.setText(s[0]+"H:"+s[1]+"M");
        }

        return view;
    }
}