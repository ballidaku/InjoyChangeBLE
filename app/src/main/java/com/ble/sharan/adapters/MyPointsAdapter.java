package com.ble.sharan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.apiModels.TopUsersModel;
import com.ble.sharan.myUtilities.MyUtil;

import java.util.List;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class MyPointsAdapter  extends BaseAdapter
{
    Context context;

    LayoutInflater inflator;

    MyUtil myUtil=new MyUtil();

    List<TopUsersModel.SubData> list;




    public MyPointsAdapter(Context context, List<TopUsersModel.SubData> list) {

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


        TopUsersModel.SubData subData=list.get(i);

        ImageView imgv_user=(ImageView)view.findViewById(R.id.imgv_user);

        TextView txtv_name=(TextView)view.findViewById(R.id.txtv_name);

        TextView txtv_points=(TextView)view.findViewById(R.id.txtv_points);

        TextView txtv_rank=(TextView)view.findViewById(R.id.txtv_rank);

        myUtil.showImageWithGlide(context,imgv_user,subData.getImage());



        txtv_name.setText(subData.getName());
        txtv_points.setText(subData.getPoints()+" Pts");

        txtv_rank.setText("Rank "+subData.getRank());
        return view;
    }
}
