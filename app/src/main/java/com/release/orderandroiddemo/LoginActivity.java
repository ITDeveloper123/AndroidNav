package com.release.orderandroiddemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import static com.release.orderandroiddemo.Constants.ANDROID_DEMO_SUBHALAXMI_UID_KEY;
import static com.release.orderandroiddemo.Constants.PREF_UID_KEY;

public class LoginActivity extends AppCompatActivity {

    private MaterialEditText usernameEditText;
    private MaterialEditText passwordEditText;
    private CheckBox rememberMeCheckbox;
    private TextView registerTextview;
    private Button loginButton;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getResources().getString(R.string.login_btn_text));

        usernameEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);
        registerTextview = findViewById(R.id.registerTextview);
        loginButton = findViewById(R.id.loginButton);
        registerTextview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                finish();
            }
        });
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailStr = usernameEditText.getText().toString();
                String passwordStr = passwordEditText.getText().toString();


                if(emailStr == null || emailStr.matches("")){
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.invalid_email),Toast.LENGTH_LONG).show();
                    return;
                }
                if(passwordStr == null || passwordStr.matches("")){
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.invalid_password),Toast.LENGTH_LONG).show();
                    return;
                }
                if (Utils.emailValidator(emailStr.trim()) == false) {
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.invalid_email),Toast.LENGTH_LONG).show();
                    return;
                }
                if(passwordStr.length() < 6)
                {
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.min_password),Toast.LENGTH_LONG).show();
                    return;
                }
                showLoader();

                    //authenticate user
                     final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(emailStr.trim(),passwordStr.trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                // there was an error
                                hideLoader();
                                Toast.makeText(LoginActivity.this,""+task.getException().getMessage(),Toast.LENGTH_LONG).show();

                            } else {
                                if (firebaseAuth.getCurrentUser() != null) {
                                    ANDROID_DEMO_SUBHALAXMI_UID_KEY = firebaseAuth.getCurrentUser().getUid();
                                    if(rememberMeCheckbox.isChecked() == false){
                                        startActivity(new Intent(LoginActivity.this,OrderListActivity.class));
                                        finish();
                                    }else{
                                        Utils.saveStringToSharedPreferences(LoginActivity.this,PREF_UID_KEY,ANDROID_DEMO_SUBHALAXMI_UID_KEY);
                                        startActivity(new Intent(LoginActivity.this,OrderListActivity.class));
                                        finish();

                                    }

                                }else{
                                    hideLoader();
                                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.failed_login),Toast.LENGTH_LONG).show();


                                }
                            }
                        }
                    });
                }




        });
    }
    public void showLoader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog ==null){
                    dialog = Utils.showProgress(LoginActivity.this);
                }
                dialog.show();
            }
        });

    }

    public void hideLoader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog!=null) {
                    dialog.hide();
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
    }
}
