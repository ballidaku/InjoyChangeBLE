package com.ble.sharan.apiModels;

import com.ble.sharan.myUtilities.MyConstant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by brst-pc93 on 3/1/17.
 */

public class DailyInspirationViewAllModel
{

    @SerializedName(MyConstant.STATUS)
    String status;

    public String getStatus()
    {
        return status;
    }


    @SerializedName(MyConstant.DATA)
    @Expose
    private ArrayList<Image> dailyInspirationViewAllList = new ArrayList<>();

    public ArrayList getDailyInspirationViewallList()
    {
        return dailyInspirationViewAllList;
    }



    public class Image
    {
        @SerializedName(MyConstant.IMAGE)
        String Image;

        public String getImage()
        {
            return Image;
        }
    }

}
