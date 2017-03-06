package com.ble.sharan.apiModels;

import com.ble.sharan.myUtilities.MyConstant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by brst-pc93 on 3/2/17.
 */

public class ShareWinSeeAllModel
{

    @SerializedName(MyConstant.STATUS)
    String status;

    public String getStatus()
    {
        return status;
    }


    @SerializedName(MyConstant.DATA)
    @Expose
    private ArrayList<SubData> shareWinList = new ArrayList<>();

    public ArrayList getShareWinList()
    {
        return shareWinList;
    }



    public class SubData
    {
        @SerializedName(MyConstant.UID)
        String uid;

        public String getUid()
        {
            return uid;
        }


        @SerializedName(MyConstant.COMMENT)
        String comment;

        public String getComment()
        {
            return comment;
        }


        @SerializedName(MyConstant.NAME)
        String name;

        public String getName()
        {
            return name;
        }


        @SerializedName(MyConstant.IMAGE)
        String image;

        public String getImage()
        {
            return image;
        }


        @SerializedName(MyConstant.DATE)
        String date;

        public String getDate()
        {
            return date;
        }
    }

}
