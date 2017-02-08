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

import java.util.HashMap;
import java.util.List;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class MyPointsAdapter  extends BaseAdapter
{
    Context context;

    LayoutInflater inflator;

    MyUtil myUtil=new MyUtil();

    List<HashMap> list;




    public MyPointsAdapter(Context context, List<HashMap> list) {

        this.context=context;
        this.list=list;
        inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
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


        HashMap hashMap=list.get(i);

        ImageView imgv_user=(ImageView)view.findViewById(R.id.imgv_user);

        TextView txtv_name=(TextView)view.findViewById(R.id.txtv_name);

        TextView txtv_points=(TextView)view.findViewById(R.id.txtv_points);

        TextView txtv_rank=(TextView)view.findViewById(R.id.txtv_rank);

        myUtil.showImageWithPicasso(context,imgv_user,hashMap.get(MyConstant.IMAGE).toString());



        txtv_name.setText(hashMap.get(MyConstant.NAME).toString());
        txtv_points.setText(hashMap.get(MyConstant.POINTS).toString()+" Pts");

        txtv_rank.setText("Rank "+String.valueOf(i+1));
        return view;
    }
}
