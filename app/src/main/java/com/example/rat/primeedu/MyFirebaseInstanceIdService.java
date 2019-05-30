package com.example.rat.primeedu;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    public static final String TOKEN_BROADCAST = "tokenbroadcast";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        getApplication().sendBroadcast(new Intent(TOKEN_BROADCAST));
        storeTokens(refreshedToken);

    }

    private void storeTokens(String refreshedToken) {
        SharedPred.getmInstance(getApplicationContext()).storeToken(refreshedToken);
    }
}
