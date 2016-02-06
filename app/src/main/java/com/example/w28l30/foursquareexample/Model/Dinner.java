package com.example.w28l30.foursquareexample.Model;

import com.orm.SugarRecord;

import org.json.JSONArray;

/**
 * Created by W28L30 on 15/11/1.
 */
public class Dinner extends SugarRecord<Dinner> {
    public String restaurant;
    public String address;
    public String time;
    //table limit
    public int num;
    //current number
    public int current;
    public int appoinmentID;
    public String memo;
    public String username;
    public String latitude;
    public String longitude;
    public String thumbnailUrl;
    public String phoneNum;
    public JSONArray members;

    public Dinner() {

    }
    //set up dinner constructor
    public Dinner(String restaurant, String address, int num, String time, String memo,String username,String latitude,String longitude,String thumbnailUrl, String phoneNum)
    {
        this.restaurant = restaurant;
        this.address = address;
        this.num = num;
        this.time = time;
        this.memo = memo;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.thumbnailUrl = thumbnailUrl;
        this.phoneNum = phoneNum;
    }

    public void setMembers(JSONArray members) {this.members = members;}

    public void setPhoneNum(String phoneNum) {this.phoneNum = phoneNum;}

    public void setThumbnailUrl(String thumbnailUrl) {this.thumbnailUrl = thumbnailUrl;}

    public void setUsername(String username) {this.username = username;}

    public void setAddress(String address) {this.address = address;}

    public void setLatitude(String latitude) {this.latitude = latitude;}

    public void setLongitude(String longitude) {this.longitude = longitude;}

    public void setMemo(String memo) {this.memo = memo;}

    public void setNum(int num) {this.num = num;}

    public void setCurrent(int current) {this.current = current;}

    public void setAppoinmentID(int appoinmentID) {
        this.appoinmentID = appoinmentID;
    }

    public void setRestaurant(String restaurant) {this.restaurant = restaurant;}

    public void setTime(String time) {this.time = time;}


    public String getLatitude() {return latitude;}

    public String getLongitude() {return longitude;}

    public String getAddress() {return address;}

    public String getRestaurant() {
        return restaurant;
    }

    public int getNum() {return num;}

    public int getCurrent() {return current;}

    public int getAppoinmentID() {return appoinmentID;}

    public String getTime() {return time;}

    public String getMemo() {
        return memo;
    }

    public String getUsername() {return username;}

    public String getPhoneNum() {return phoneNum;}

    public String getThumbnailUrl() {return thumbnailUrl;}

    public JSONArray getMembers() {return members;}
}
