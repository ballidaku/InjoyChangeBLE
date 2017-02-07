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

public class MyPointsAdapter  extends BaseAdapter
{
    Context context;
    int images[];
    String[] names,rank,num_val;
    LayoutInflater inflator;

    public MyPointsAdapter(Context context, int[] images, String[] names, String[] rank, String[] num_val) {
        this.images=images;
        this.names=names;
        this.rank=rank;
        this.num_val=num_val;
        this.context=context;
        inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=inflator.inflate(R.layout.custom_mypoints_list,null);

        if(i%2==0) {
            view.setBackgroundColor(context.getResources().getColor(R.color.colorGrayWhite));
        }

        ImageView circle_image=(ImageView)view.findViewById(R.id.circle_image);

        TextView list_tv=(TextView)view.findViewById(R.id.list_tv);

        TextView num_tv=(TextView)view.findViewById(R.id.num_tv);

        TextView rank_tv=(TextView)view.findViewById(R.id.rank_tv);


        circle_image.setImageResource(images[i]);
        list_tv.setText(names[i]);
        num_tv.setText(num_val[i]);
        rank_tv.setText(rank[i]);
        return view;
    }
}
