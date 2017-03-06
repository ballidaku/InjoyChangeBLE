package com.ble.sharan.apiModels;

import com.ble.sharan.myUtilities.MyConstant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by brst-pc93 on 3/1/17.
 */

public class TopUsersModel
{

    @SerializedName(MyConstant.STATUS)
    String status;

    public String getStatus()
    {
        return status;
    }


    @SerializedName(MyConstant.TOTAL_ELEVATION_ACTIONS)
    String total_elevation_actions;

    public String getTopElevationActions()
    {
        return total_elevation_actions;
    }


    @SerializedName(MyConstant.DATA)
    @Expose
    private ArrayList<SubData> topUsersList = new ArrayList<>();

    public ArrayList getTopUserslList()
    {
        return topUsersList;
    }



    public class SubData
    {
        @SerializedName(MyConstant.UID)
        String uid;

        public String getUid()
        {
            return uid;
        }


        @SerializedName(MyConstant.RANK)
        int rank;

        public int getRank()
        {
            return rank;
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


        @SerializedName(MyConstant.POINTS)
        String points;

        public String getPoints()
        {
            return points;
        }
    }
}
