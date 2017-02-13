package com.ble.sharan.adapters;

/**
 * Created by Sharanpal on 7/31/2015.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewPagerAdapter extends PagerAdapter
{

    Context context;
    LayoutInflater inflater;

    private ArrayList<HashMap> list = new ArrayList<>();

    MyUtil myUtil = new MyUtil();

    public ViewPagerAdapter(Context context, ArrayList<HashMap> list)
    {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;


    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == ((FrameLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup row, int position)
    {
        View v = inflater.inflate(R.layout.custom_viewpager_item, null);

        HashMap<String, String> map = list.get(position);

        ImageView imgv_user = (ImageView) v.findViewById(R.id.imgv_user);
        TextView txtv_comments = (TextView) v.findViewById(R.id.txtv_comments);
        TextView txtv_name = (TextView) v.findViewById(R.id.txtv_name);

        txtv_comments.setText(map.get(MyConstant.COMMENT));
        txtv_name.setText(map.get(MyConstant.NAME));


        myUtil.showImageWithGlide(context,imgv_user,map.get(MyConstant.IMAGE));

        row.addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((FrameLayout) object);
    }
}
