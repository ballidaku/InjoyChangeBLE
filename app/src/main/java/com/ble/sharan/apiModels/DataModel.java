package com.ble.sharan.apiModels;

import com.ble.sharan.myUtilities.MyConstant;
import com.google.gson.annotations.SerializedName;

/**
 * Created by brst-pc93 on 3/1/17.
 */

public class DataModel
{

    //**********************************************************************************************
    // USER POINTS
    //**********************************************************************************************

    @SerializedName(MyConstant.TOTAL_POINTS)
    String points;

    public String getTotalPoints()
    {
        return points;
    }

   /* public void setPoints(String points) {

        this.points = points;
    }*/

    @SerializedName(MyConstant.RAFFLE_TICKET)
    String ticket;

    public String getRaffleTicket()
    {
        return ticket;
    }

    /*public void setTicket(String ticket) {
        this.ticket = ticket;
    }*/


    @SerializedName(MyConstant.STATUS)
    String status;

    public String getStatus()
    {
        return status;
    }


    //**********************************************************************************************
    // DAILY INSPIRATION
    //**********************************************************************************************

    @SerializedName(MyConstant.DATA)
    String data;

    public String getData()
    {
        return data;
    }

    @SerializedName(MyConstant.COUNT)
    int count;

    public int getCount()
    {
        return count;
    }






}
