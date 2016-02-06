package com.example.w28l30.foursquareexample.Model;

import android.util.Log;

import com.example.w28l30.foursquareexample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by XiaowenJiang on 11/10/15.
 */
public class JSONUtl {
    //put user information in a jsonobject and convert to string
    public static String UserToJson(UserInform userInform)
    {
        JSONObject InformObject = new JSONObject();
        try {
            int command = 1;
            InformObject.put("Command",command);
            InformObject.put("Name",userInform.getName());
            InformObject.put("Password", userInform.getPassword());
            InformObject.put("Username", userInform.getUsername());
            InformObject.put("Email", userInform.getEmail());
            InformObject.put("Gender", userInform.getGender());
            InformObject.put("Rate", userInform.getRate());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return InformObject.toString();
    }

    public static UserInform JsonToUser(JSONObject object)
    {
        UserInform userInform = new UserInform();
        try {
            userInform.setUsername(object.getString("Username"));
            userInform.setName(object.getString("Name"));
            userInform.setEmail(object.getString("Email"));
            //userInform.setPassword(object.getString("Password"));
            userInform.setGender(object.getInt("Gender"));
            userInform.setRate(object.getInt("Rate"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInform;
    }

    //set up invitation json
    public static JSONObject DinnerToJson(Dinner dinner) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Command",4);
        jsonObject.put("Username",dinner.getUsername());
        jsonObject.put("NameRest",dinner.getRestaurant());
        jsonObject.put("Catagory","hehe");
        jsonObject.put("Address",dinner.getAddress());
        jsonObject.put("Limit",dinner.getNum());
        jsonObject.put("Time",dinner.getTime());
        Log.d("time: ",dinner.getTime());
        jsonObject.put("latitude",dinner.getLatitude());
        jsonObject.put("longitude",dinner.getLongitude());
        jsonObject.put("memo",dinner.getMemo());
        jsonObject.put("url",dinner.getThumbnailUrl());
        jsonObject.put("Telephone",dinner.getPhoneNum());
        return jsonObject;
    }

    //join meal json
    public static JSONObject JoinMealJson(Dinner dinner,String username) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Command",5);
        jsonObject.put("Username",username);
        jsonObject.put("Appoint_num",dinner.getAppoinmentID());
        return jsonObject;
    }

    //refresh dinner list json
    public static JSONObject LatlongToJson(double latitude, double longitude) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Command", 6);
        jsonObject.put("latitude",latitude);
        jsonObject.put("longitude",longitude);
        return jsonObject;
    }

    //refresh personal history
    public static JSONObject RequestHistoryJson(String username) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Command", 7);
        jsonObject.put("Username",username);
        return jsonObject;
    }

    //refresh personal future events
    public static JSONObject RequestUpcomingJson(String username) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Command", 8);
        jsonObject.put("Username",username);
        return jsonObject;
    }

    //cancel appointment
    public static JSONObject CancelJson(String username,int appoint_num,int property) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Command", 9);
        jsonObject.put("Username",username);
        jsonObject.put("Appoint_num",appoint_num);
        jsonObject.put("Property",property);
        return jsonObject;
    }


    //exit
    public static JSONObject ExitToJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Command", 20);
        return jsonObject;
    }

    public static Dinner JsonToDinner(JSONObject jsonObject)
    {
        Dinner dinner = new Dinner();

        try {
            dinner.setTime(jsonObject.getString("Time"));
            dinner.setRestaurant(jsonObject.getString("Name"));
            dinner.setAddress(jsonObject.getString("Address"));
            dinner.setUsername(jsonObject.getString("Username"));
            dinner.setNum(jsonObject.getInt("Limit"));
            dinner.setCurrent(jsonObject.getInt("Current"));
            dinner.setAppoinmentID(jsonObject.getInt("Appoint_num"));
            dinner.setThumbnailUrl(jsonObject.getString("url"));
            dinner.setPhoneNum(jsonObject.getString("Telephone"));
            dinner.setMemo(jsonObject.getString("memo"));
            dinner.setMembers(jsonObject.getJSONArray("Member"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dinner;
    }



}
