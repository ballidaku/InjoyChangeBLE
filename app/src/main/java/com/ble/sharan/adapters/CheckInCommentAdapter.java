package com.ble.sharan.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.apiModels.CheckInModel;
import com.ble.sharan.myUtilities.MyUtil;

import java.util.ArrayList;

/**
 * Created by brst-pc93 on 3/1/17.
 */

public class CheckInCommentAdapter  extends BaseAdapter
{
    Context context;
    ArrayList<CheckInModel.Comment> list;
    MyUtil myUtil = new MyUtil();



    public CheckInCommentAdapter(Context context, ArrayList<CheckInModel.Comment> list)
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

        row = inflater.inflate(R.layout.custom_checkin_comment_item, parent, false);


        if(position%2 != 0)
        {
            row.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGrayWhite));
        }

        TextView txtv_comment = (TextView) row.findViewById(R.id.txtv_comment);

        txtv_comment.setText(list.get(position).getComment());

        return row;
    }


}