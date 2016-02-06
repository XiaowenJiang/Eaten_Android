package com.example.w28l30.foursquareexample.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.w28l30.foursquareexample.Activity.ConnectActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketService extends Service {
    public String ipaddress;
    private String preference = "PREFERENCES";
    public String portnum;
    private static final String TAG = "MyService";
    private boolean isConnect=false;
    public static Socket socket; //这里定义了一个静态socket 供其他activity使用
    private final IBinder myBinder = new LocalBinder();

    @Override
    public void onCreate(){
        Log.v(TAG, "onCreate");
    }
    @Override
    public IBinder onBind(Intent intent) {

        // TODO Auto-generated method stub
        return myBinder;
    }

    public class LocalBinder extends Binder {
        public SocketService getService() {
            System.out.println("I am in Localbinder ");
            return SocketService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        new Thread() {
            @Override
            public void run() {
                if(!isConnect)
                    initSocket();
                Log.d(TAG,"receive msg is called");
                BufferedReader in = null;
                String result ="";
                while(socket!=null&&!socket.isClosed()&&socket.isConnected()&&!socket.isOutputShutdown())
                {

                    try {
                        Thread.sleep(500);

                        in = new BufferedReader(new InputStreamReader(socket
                                .getInputStream()));

                        result = in.readLine();
                        //detect server disconnect
                        if(result==null)
                        {
                            Intent restart = new Intent(getApplicationContext(), ConnectActivity.class);
                            restart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            restart.putExtra("disconnect","Server disconnect!");
                            startActivity(restart);
                            isConnect = false;
                            break;
                        }
                        else if(isJSONValid(result))
                        {
                            JSONObject judge = new JSONObject(result);
                            if(judge.has("Big"))
                            {
                                Thread.sleep(1800);
                                result = in.readLine();
                            }
                        }


                        if(result!=null&&!result.equals(""))
                        {
                            Log.d(TAG,result);
                            if(isJSONValid(result))
                            {
                                JSONObject object = new JSONObject(result);
                                if(object.has("join"))
                                {
                                    Log.d(TAG,"send notification");
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Result", result);
                                    Intent broad = new Intent("android.intent.action.Notification");
                                    broad.putExtra("bundle",bundle);
                                    sendBroadcast(broad);
                                    continue;
                                }

                            }
                            Log.d(TAG,"send a broadcast");
                            Bundle bundle = new Bundle();
                            bundle.putString("Result", result);
                            Intent broad = new Intent("android.intent.action.MAIN");
                            broad.putExtra("bundle",bundle);
                            sendBroadcast(broad);
                        }
                    } catch (IOException | InterruptedException | JSONException e) {
                        e.printStackTrace();
                    }

                }
                Log.d(TAG,"socket is closed");
            }
        }.start();
        return START_STICKY;
    }

    //judge the string is a JSONObject or a JSONArray
    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            return false;
        }
        return true;
    }

    private void initSocket() {
        try {
            Log.v(TAG, "initSocket");
            SharedPreferences sharedpreferences = getSharedPreferences(preference, Context.MODE_PRIVATE);
            ipaddress = sharedpreferences.getString("IPADDRESS",null);
            portnum = sharedpreferences.getString("PORTNUM",null);
            socket = new Socket(ipaddress, Integer.parseInt(portnum));
            isConnect=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendMSG(String msg) throws IOException {
        Log.d(TAG,"sendMSG is called");

        if (socket!=null&&!socket.isClosed()) {
            if (socket.isConnected()) {
                if (!socket.isOutputShutdown()) {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream())), true);
                    out.println(msg);
                }
            }
        }
    }

}
