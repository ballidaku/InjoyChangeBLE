package com.ble.sharan.apiModels;

import com.ble.sharan.myUtilities.MyConstant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by brst-pc93 on 3/1/17.
 */

public class ShoutOutModel
{
    @SerializedName(MyConstant.STATUS)
    String status;

    public String getStatus()
    {
        return status;
    }

    @SerializedName(MyConstant.LIKES)
    int likes;

    public int getLikes()
    {
        return likes;
    }



    @SerializedName(MyConstant.COUNT)
    int count;

    public int getCount()
    {
        return count;
    }


    @SerializedName(MyConstant.SHOUT_OUT_COUNT)
    int shout_out_count;

    public int getShoutOutCount()
    {
        return shout_out_count;
    }


    @SerializedName(MyConstant.DATA)
    @Expose
    private ArrayList<ShoutOutModel.SubData> shoutOutList = new ArrayList<>();

    public ArrayList getShoutOutList()
    {
        return shoutOutList;
    }


    public class SubData
    {

        @SerializedName(MyConstant.ID)
        String id;

        public String getId()
        {
            return id;
        }

        @SerializedName(MyConstant.UID)
        String uid;

        public String getUid()
        {
            return uid;
        }


        @SerializedName(MyConstant.OUTER_COMMENT)
        String outer_comment;

        public String getOuterComment()
        {
            return outer_comment;
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


        @SerializedName(MyConstant.TIME)
        String time;

        public String getTime()
        {
            return time;
        }

        @SerializedName(MyConstant.LIKES)
        int likes;

        public int getLikes()
        {
            return likes;
        }

        public void addLikes(int likesCount)
        {
            this.likes=likesCount;
        }


       /* @SerializedName(MyConstant.POP_UP_COMMENT)
        String pop_up_comment;

        public String getPopUpComment()
        {
            return pop_up_comment;
        }*/


        @SerializedName(MyConstant.HIGH_FIVE_STATUS)
        String high_five_status;

        public String getHighFiveStatus()
        {
            return high_five_status;
        }


        public void setHighFiveStatus(String high_five_status)
        {
            this.high_five_status=high_five_status;
        }
    }
}
