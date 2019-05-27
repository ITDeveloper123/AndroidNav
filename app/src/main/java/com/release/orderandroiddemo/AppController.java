package com.release.orderandroiddemo;

/**
 * Created by rakeshacharya on 09/12/17.
 */


import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;


public class AppController extends Application {

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        FirebaseApp.initializeApp(this);

    }
    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }
    public static synchronized AppController getInstance() {
        return mInstance;
    }
}
