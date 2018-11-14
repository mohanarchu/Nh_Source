package com.develop.app.myapplication.firebase;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.develop.app.myapplication.common.KeyCodes;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ITSoftSupport on 17/05/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "FCM Service";
    SharedPreferences sharedPreferences;
    KeyCodes keyCodes=KeyCodes.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        sharedPreferences = getSharedPreferences("firebase", MODE_PRIVATE);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
     Log.d(TAG, keyCodes.REFRESHTOKEN + " - " + refreshedToken);
        if (refreshedToken != null) {
            SharedPreferences.Editor e = sharedPreferences.edit();
            e.putString(keyCodes.REFRESHTOKEN, refreshedToken);
            e.commit();
        }

    }

    /*private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }*/
}
