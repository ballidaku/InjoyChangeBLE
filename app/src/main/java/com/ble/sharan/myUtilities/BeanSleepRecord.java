package com.ble.sharan.myUtilities;

/**
 * Created by brst-pc93 on 1/11/17.
 */

public class BeanSleepRecord
{
    int id;
    String date;
    String time;


    public BeanSleepRecord(int id,String date, String time){
        this.id=id;
        this.date = date;
        this.time = time;
    }

    public BeanSleepRecord(String date, String time){
        this.date = date;
        this.time = time;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }
}
