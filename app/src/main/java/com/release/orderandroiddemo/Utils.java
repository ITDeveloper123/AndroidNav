package com.release.orderandroiddemo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Utils {
    public static void showAlertMessage(View view, String message){
        Snackbar.make(view,message, Snackbar.LENGTH_LONG).show();

    }

    public static boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidMobile(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() == 10) {
                if (phone.charAt(0) == '0'){
                    check = false;
                }else{
                    check = true;
                }
                // if(phone.length() != 10) {
            } else {
                check = false;
            }
        } else {
            check=false;
        }
        return check;
    }

    public static int isSpecialCharContains(String s) {
        if (s == null || s.trim().isEmpty()) {
            System.out.println("Incorrect format of string");
            return -1;
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(s);
        // boolean b = m.matches();
        boolean b = m.find();
        if (b == true)
            return 1;
        else
            return 0;
    }
    public static AlertDialog showProgress (Context context){
        int llPadding = 60;
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(100, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.LEFT);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, 180, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(context);
        tvText.setText("Loading ...");
        tvText.setTextColor(Color.parseColor("#9f9f9f"));
        tvText.setTextSize(16);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(ll);

        AlertDialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }
        return dialog;
    }
    public static void saveStringToSharedPreferences(Context mContext, String key, String value) {
        if(mContext != null) {
            SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.ANDROID_DEMO_SUBHALAXMI_PREF_KEY, 0);
            if(mSharedPreferences != null)
                mSharedPreferences.edit().putString(key, value).commit();
        }
    }
    public static String readStringFromSharedPreferences(Context mContext, String key) {
        if(mContext != null) {
            SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.ANDROID_DEMO_SUBHALAXMI_PREF_KEY, 0);
            if(mSharedPreferences != null)
                return mSharedPreferences.getString(key, null);
        }
        return null;
    }
    public static void removeFromSharedPreferences(Context mContext, String key) {
        if (mContext != null) {
            SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.ANDROID_DEMO_SUBHALAXMI_PREF_KEY, 0);
            if (mSharedPreferences != null)
                mSharedPreferences.edit().remove(key).commit();
        }
    }

}
