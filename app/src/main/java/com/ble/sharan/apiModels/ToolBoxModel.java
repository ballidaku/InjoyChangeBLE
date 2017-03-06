package com.ble.sharan.apiModels;

import com.ble.sharan.myUtilities.MyConstant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by brst-pc93 on 3/2/17.
 */

public class ToolBoxModel
{
    @SerializedName(MyConstant.STATUS)
    String status;

    public String getStatus()
    {
        return status;
    }

    @SerializedName(MyConstant.TITLE)
    String title;

    public String getTitle()
    {
        return title;
    }


    @SerializedName(MyConstant.IMAGE)
    String image;

    public String getImage()
    {
        return image;
    }

    @SerializedName(MyConstant.DESCRIPTION)
    String description;

    public String getDescription()
    {
        return description;
    }


    @SerializedName(MyConstant.COUNT)
    int count;

    public int getCount()
    {
        return count;
    }



    @SerializedName(MyConstant.DATA)
    @Expose
    private ArrayList<SubData> toolBoxList = new ArrayList<>();

    public ArrayList getToolBoxlList()
    {
        return toolBoxList;
    }

    public class SubData
    {
        @SerializedName(MyConstant.DESCRIPTION)
        String description;

        public String getDescription()
        {
            return description;
        }


        @SerializedName(MyConstant.IMAGE)
        String image;

        public String getImage()
        {
            return image;
        }


        @SerializedName(MyConstant.TITLE)
        String title;

        public String getTitle()
        {
            return title;
        }
    }

}
