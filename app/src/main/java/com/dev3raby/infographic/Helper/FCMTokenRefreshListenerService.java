package com.dev3raby.infographic.Helper;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Ahmed Yehya on 04/07/2017.
 */

public class FCMTokenRefreshListenerService extends FirebaseInstanceIdService {
    private SessionManager session;

    @Override
    public  void onTokenRefresh() {
        session = new SessionManager(getApplicationContext());

            if(session.isLoggedIn()) {
                Intent intent = new Intent(this, FCMRegistrationService.class);
                intent.putExtra("refreshed", true);
                startService(intent);
            }
            else {}

    }
}
