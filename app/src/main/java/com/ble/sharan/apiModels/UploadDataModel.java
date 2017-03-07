package com.ble.sharan.apiModels;

import com.ble.sharan.myUtilities.MyConstant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by brst-pc93 on 3/6/17.
 */

public class UploadDataModel
{
    @SerializedName(MyConstant.STATUS)
    String status;

    public String getStatus()
    {
        return status;
    }


    @SerializedName(MyConstant.DATA)
    @Expose
    Object data;

    public Object getData()
    {
        return data;
    }

}
