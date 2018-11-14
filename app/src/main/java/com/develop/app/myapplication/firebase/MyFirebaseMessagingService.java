package com.develop.app.myapplication.firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.develop.app.myapplication.API.APIURL;
import com.develop.app.myapplication.Db.Work_order_list;
import com.develop.app.myapplication.Login;
import com.develop.app.myapplication.R;
import com.develop.app.myapplication.WorkOrderList;
import com.develop.app.myapplication.WorkOrderView;
import com.develop.app.myapplication.common.KeyCodes;
import com.develop.app.myapplication.model.SubModule;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
       String userGroup="",moduleName="",subModuleName="",subModules="",
               subModulelistService="",subModulecountService="";
       String TAG;
    SharedPreferences.Editor notification;
    SharedPreferences sharedPreferences;
    JSONObject mData;
    PendingIntent pendingIntent = null;
    String title,body;
    String titles,messages;
    Map<String, String> data;
    KeyCodes keyCodes = KeyCodes.getInstance();
    @SuppressLint("CommitPrefEdits")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {


        sharedPreferences = getSharedPreferences(Login.My_PREF, MODE_PRIVATE);
        try {
            JSONObject jsonObject = new JSONObject(remoteMessage.getData());
            notifications(jsonObject.toString(), remoteMessage.getNotification().getBody()
                                                         ,remoteMessage.getNotification().getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void notifications(String payload,String body,String title)
    {
        String mType = "";
        try {
            mData = new JSONObject(payload);
            userGroup =  mData.getString("dataUrl");
            moduleName=   mData.getString("navigateUrl");

            notification = getSharedPreferences("Notification", MODE_PRIVATE).edit();
            notification.putString("userGroup",userGroup);
            notification.putString("user_type", moduleName);
            Log.i("mohanraj",  moduleName);
            notification.putString("model","0");
            notification.apply();
            final int min = 200;
            final int max = 800;
            final int random = new Random().nextInt((max - min) + 1) + min;
            Intent i = new Intent(getApplicationContext(), WorkOrderList.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(this, random, i, PendingIntent.FLAG_UPDATE_CURRENT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri);
            Notification notification1;
            notificationBuilder.setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.logo);
            notification1 = notificationBuilder.build();
            notification1.flags = Notification.FLAG_AUTO_CANCEL;

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(getRequestCode(), notification1);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    private static int getRequestCode()
    {
        Random rnd = new Random();
        return 100 + rnd.nextInt(900000);
    }
}








