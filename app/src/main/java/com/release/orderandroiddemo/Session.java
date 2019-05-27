package com.release.orderandroiddemo;


import android.content.Context;
import android.content.SharedPreferences;



public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;





    public String getUid() {
        return prefs.getString(Constants.ANDROID_DEMO_SUBHALAXMI_UID_KEY, null);
    }

    public void setUid(String uid) {
        editor.putString(Constants.ANDROID_DEMO_SUBHALAXMI_UID_KEY, uid);
        editor.commit();
    }



    public void clear(){
        prefs = ctx.getSharedPreferences(Constants.ANDROID_DEMO_SUBHALAXMI_PREF_KEY, Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.clear().commit();
    }


    public Session(Context ctx) {
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences(Constants.ANDROID_DEMO_SUBHALAXMI_PREF_KEY, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }


}