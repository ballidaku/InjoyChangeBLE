package com.ble.sharan.myUtilities;

/**
 * Created by brst-pc93 on 1/6/17.
 */

public class BeanRecords
{
    //private variables
    int id;
    String date;
    String steps;
    String access_token;

    // Empty constructor
    public BeanRecords(){

    }
    // constructor
    public BeanRecords(int id, String date, String steps,String access_token){
        this.id = id;
        this.date = date;
        this.steps = steps;
        this.access_token=access_token;
    }

    public String getAccess_token()
    {
        return access_token;
    }

    public void setAccess_token(String access_token)
    {
        this.access_token = access_token;
    }

    // constructor
    public BeanRecords(String date, String steps){
        this.date = date;
        this.steps = steps;
    }

    // getting ID
    public int getID(){
        return this.id;
    }

    // setting id
    public void setID(int id){
        this.id = id;
    }

    // getting date
    public String getDate(){
        return this.date;
    }

    // setting date
    public void setDate(String date){
        this.date = date;
    }

    // getting steps
    public String getSteps(){
        return this.steps;
    }

    // setting steps
    public void setSteps(String steps){
        this.steps = steps;
    }


}
