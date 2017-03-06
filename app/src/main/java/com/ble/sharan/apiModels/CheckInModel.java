package com.ble.sharan.apiModels;

import com.ble.sharan.myUtilities.MyConstant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by brst-pc93 on 3/1/17.
 */

public class CheckInModel
{
    @SerializedName(MyConstant.STATUS)
    String status;

    public String getStatus()
    {
        return status;
    }

    @SerializedName(MyConstant.COUNT)
    int count;

    public int getCount()
    {
        return count;
    }



    @SerializedName(MyConstant.DATA)
    @Expose
    private ArrayList<Comment> checkInCommentList = new ArrayList<>();

    public ArrayList getCheckInCommentList()
    {
        return checkInCommentList;
    }



    public class Comment
    {
        @SerializedName(MyConstant.COMMENT)
        String comment;

        public String getComment()
        {
            return comment;
        }
    }
}
