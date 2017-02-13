package com.ble.sharan.adapters;

/**
 * Created by brst-pc93 on 2/9/17.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyUtil;

import java.util.HashMap;
import java.util.List;

public class ShareWinEndlessAdapter extends ArrayAdapter<HashMap> {

    private List<HashMap> itemList;
    private Context context;
    private int layoutId;
    LayoutInflater inflater;

    MyUtil myUtil=new MyUtil();

    public ShareWinEndlessAdapter(Context context, List<HashMap> itemList, int layoutId) {
        super(context, layoutId, itemList);
        this.itemList = itemList;
        this.context = context;
        this.layoutId = layoutId;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HashMap hashMap = itemList.get(position);

        convertView = inflater.inflate(R.layout.custom_share_win_list, null);
        if (position % 2 == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorGrayWhite));
        }

        ImageView imgv_user = (ImageView) convertView.findViewById(R.id.imgv_user);

        TextView txtv_name = (TextView) convertView.findViewById(R.id.txtv_name);
        TextView txtv_time = (TextView) convertView.findViewById(R.id.txtv_time);
        TextView txtv_comment = (TextView) convertView.findViewById(R.id.txtv_comment);


        txtv_name.setText(hashMap.get(MyConstant.NAME).toString());
        txtv_time.setText(hashMap.get(MyConstant.DATE).toString());
        txtv_comment.setText(hashMap.get(MyConstant.COMMENT).toString());


        myUtil.showCircularImageWithPicasso(context,imgv_user,hashMap.get(MyConstant.IMAGE).toString());



        return convertView;

    }
}


