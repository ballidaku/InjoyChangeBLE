package com.ble.sharan.apiModels;

import com.ble.sharan.myUtilities.MyConstant;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by brst-pc93 on 3/1/17.
 */

public interface ApiInterface
{


    @POST("/api/login-token")
    Call<LoginModel> basicLogin();

    @GET("/api/user_point")
    Call<DataModel> getpoint(@Query(MyConstant.DATE) String date, @Query(MyConstant.UID) String uid);

    @GET("/api/daily_inspiration")
    Call<DataModel> getDailyInspiration(@Query(MyConstant.TIME) String date, @Query(MyConstant.UID) String uid);


    @GET("/api/submit_daily_ins")
    Call<DataModel> submitDailyInspirationPoints(@Query(MyConstant.UID) String uid,@Query(MyConstant.DATE) String date);

    @GET("/api/daily_inspiration")
    Call<DailyInspirationViewAllModel> getDailyInspirationViewAll();


    @GET("/api/list_checkin")
    Call<CheckInModel> getCheckInComment(@Query(MyConstant.DATE) String date, @Query(MyConstant.UID) String uid);


    @GET("/api/submit_checkin")
    Call<CheckInModel> addCheckInComment(@Query(MyConstant.COMMENT) String comment, @Query(MyConstant.DATE) String date, @Query(MyConstant.UID) String uid);


    @GET("/api/top_users")
    Call<TopUsersModel> getTopUsers();


    @GET("/api/shout_out")
    Call<ShoutOutModel> getShoutOutData(@Query(MyConstant.DATE) String date, @Query(MyConstant.UID) String uid, @Query("scroll_id") int scroll_id);


    @GET("/api/submit_shout_out")
    Call<ShoutOutModel> addShoutOutComment(@Query(MyConstant.COMMENT) String comment, @Query(MyConstant.UID) String uid, @Query(MyConstant.DATE) String date);

    @GET("/api/submit_high_five")
    Call<ShoutOutModel> submitHighFive(@Query(MyConstant.DATE) String date, @Query(MyConstant.ID) String id, @Query(MyConstant.UID) String uid);


    @GET("/api/toolbox")
    Call<ToolBoxModel> readNowToolBox(@Query(MyConstant.TIME) String time, @Query(MyConstant.DAY) String day, @Query(MyConstant.UID) String uid);


    @GET("/api/submit_toolbox")
    Call<ToolBoxModel> submitPointsToolBox(@Query(MyConstant.DATE) String date, @Query(MyConstant.UID) String uid, @Query(MyConstant.DAY) String day);


    @GET("/api/toolbox")
    Call<ToolBoxModel> getToolBoxAllData();


    @GET("/api/share_win")
    Call<ShareWinModel> getShareWinData(@Query(MyConstant.DATE) String date, @Query(MyConstant.UID) String uid);


    @GET("/api/share_win_see_all")
    Call<ShareWinSeeAllModel> getShareWinSeeAllData(@Query("scroll_id") int scroll_id);


    @GET("/api/submit_share_win")
    Call<ShareWinModel> shareShareWinComment(@Query(MyConstant.COMMENT) String comment, @Query(MyConstant.UID) String uid, @Query(MyConstant.DATE) String date);


    @GET("/api/submit_weekly_video")
    Call<ShareWinModel> shareWeeklyVideoComment(@Query(MyConstant.COMMENT) String comment, @Query(MyConstant.UID) String uid, @Query(MyConstant.DATE) String date);




    @POST("/api/postdata")
    @FormUrlEncoded
    Call<UploadDataModel> postData(@Field(MyConstant.DATE) String date,
                                 @Field(MyConstant.STEPS) String steps,
                                 @Field(MyConstant.CALORIES) String calories,
                                 @Field("sleephr") String sleephr,
                                 @Field(MyConstant.DISTANCE) String distance,
                                 @Field(MyConstant.UID) String uid);
}
