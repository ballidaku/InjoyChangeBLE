package com.ble.sharan.apiModels;

import com.ble.sharan.myUtilities.MyConstant;
import com.google.gson.annotations.SerializedName;

/**
 * Created by brst-pc93 on 3/6/17.
 */

public class LoginModel
{
    @SerializedName(MyConstant.STATUS)
    String status;

    public String getStatus()
    {
        return status;
    }

    @SerializedName(MyConstant.PROFILE)
    SubData profile;

    public SubData getProfile()
    {
        return profile;
    }


   public class SubData
    {
        @SerializedName(MyConstant.FULLNAME)
        String fullname;

        public String getFullName()
        {
            return fullname;
        }


        @SerializedName(MyConstant.UID)
        String uid;

        public String getUID()
        {
            return uid;
        }


        @SerializedName(MyConstant.GENDER)
        String gender;

        public String getGender()
        {
            return gender;
        }


        @SerializedName(MyConstant.HEIGHT)
        String height;

        public String getHeight()
        {
            return height;
        }

        @SerializedName(MyConstant.WEIGHT)
        String weight;

        public String getWeight()
        {
            return weight;
        }


        @SerializedName(MyConstant.STRIDE)
        String stride;

        public String getStride()
        {
            return stride;
        }

        @SerializedName(MyConstant.PROFILE_PICTURE)
        String profile_picture;

        public String getProfilePicture()
        {
            return profile_picture;
        }

    }


}
