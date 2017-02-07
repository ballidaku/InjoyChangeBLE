package com.ble.sharan.adapters;

/**
 * Created by Sharanpal on 7/31/2015.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ble.sharan.R;

import java.util.ArrayList;

public
class ViewPagerAdapter extends PagerAdapter
{

    private Context con;
    LayoutInflater inflater;

    private ArrayList<Integer> imageIdList = new ArrayList<>();

    public ViewPagerAdapter(Context con, ArrayList<Integer> imageIdList)
    {
        this.con = con;
        inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageIdList=imageIdList;


    }

    @Override
    public
    int getCount()
    {
        return imageIdList.size();
    }

    @Override
    public
    boolean isViewFromObject(View view, Object object)
    {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup row, int position)
    {
        View v = inflater.inflate(R.layout.custom_viewpager_item, null);

        ImageView image = (ImageView) v.findViewById(R.id.pager_images);
        image.setImageResource(imageIdList.get(position));
        row.addView(v);

        return v;
    }

    @Override
    public
    void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((LinearLayout) object);
    }
}
