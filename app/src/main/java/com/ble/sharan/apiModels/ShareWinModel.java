package com.ble.sharan.apiModels;

import com.ble.sharan.myUtilities.MyConstant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by brst-pc93 on 3/2/17.
 */

public class ShareWinModel
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
    Data data;
    public Data getData()
    {
        return data;
    }

    public class Data
    {
        @SerializedName(MyConstant.SHARE_WIN)
        @Expose
        private ArrayList<SubData> shareWinList = new ArrayList<>();

        public ArrayList getshareWinList()
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

            @SerializedName(MyConstant.COMMENT)
            String comment;

            public String getComment()
            {
                return comment;
            }
        }


        @SerializedName(MyConstant.WEEKLY_CHALLENGE)
        @Expose
        WeeklyChallenge weeklyChallenge;

        public WeeklyChallenge getWeeklyChallenge()
        {
            return weeklyChallenge;
        }


       public class  WeeklyChallenge
        {
            @SerializedName(MyConstant.URL)
            String url;

            public String getUrl()
            {
                return url;
            }
        }


        @SerializedName(MyConstant.WEEKLY_COUNT)
        int weekly_count;

        public int getWeeklyCount()
        {
            return weekly_count;
        }


        @SerializedName(MyConstant.SHAREWIN_COUNT)
        int sharewin_count;

        public int getShareWinCount()
        {
            return sharewin_count;
        }

    }
}
