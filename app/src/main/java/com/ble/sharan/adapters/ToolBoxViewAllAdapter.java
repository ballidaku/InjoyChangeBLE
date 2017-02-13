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
import com.ble.sharan.myUtilities.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by brst-pc93 on 2/10/17.
 */

public class ToolBoxViewAllAdapter  extends BaseAdapter
{
    Context context;
    ArrayList<HashMap<String,String>> list;
    MyUtil myUtil = new MyUtil();


    public ToolBoxViewAllAdapter(Context context, ArrayList<HashMap<String,String>> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View row, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        row = inflater.inflate(R.layout.custom_tool_box_view_all_item, parent, false);


        ImageView imgvToolBoxviewDetail=(ImageView)row.findViewById(R.id.imgvToolBoxviewDetail);
        TextView txtvToolBoxviewDetail=(TextView)row.findViewById(R.id.txtvToolBoxviewDetail);

        myUtil.showCircularImageWithPicasso(context,imgvToolBoxviewDetail,list.get(position).get(MyConstant.IMAGE));
        txtvToolBoxviewDetail.setText(list.get(position).get(MyConstant.DESCRIPTION));

        return row;
    }


}