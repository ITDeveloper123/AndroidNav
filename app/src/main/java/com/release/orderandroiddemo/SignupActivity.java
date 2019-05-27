package com.release.orderandroiddemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignupActivity extends AppCompatActivity {
    private MaterialEditText firstNameEditText;
    private MaterialEditText lastNameEditText;
    private MaterialEditText emailEditText;
    private MaterialEditText passwordEditText;
    private TextView loginTextview;
    private Button registerButton;
    DatabaseReference databaseReference;
    AlertDialog dialog;
    private CheckBox termsCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_signup));


        firstNameEditText = findViewById(R.id.firstNameEditText);
        termsCheckbox = findViewById(R.id.termsCheckbox);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginTextview = findViewById(R.id.loginTextview);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        loginTextview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
            }
        });

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstNameStr = firstNameEditText.getText().toString();
                final String lastNameStr = lastNameEditText.getText().toString();
                final String emailStr = emailEditText.getText().toString();
                String passwordStr = passwordEditText.getText().toString();

                if(firstNameStr == null || firstNameStr.matches("")){
                    Toast.makeText(SignupActivity.this,getResources().getString(R.string.invalid_first_name),Toast.LENGTH_LONG).show();
                    return;
                }
                if(lastNameStr == null || lastNameStr.matches("")){
                    Toast.makeText(SignupActivity.this,getResources().getString(R.string.invalid_last_name),Toast.LENGTH_LONG).show();
                    return;
                }
                if(emailStr == null || emailStr.matches("")){
                    Toast.makeText(SignupActivity.this,getResources().getString(R.string.invalid_email),Toast.LENGTH_LONG).show();
                    return;
                }
                if(passwordStr == null || passwordStr.matches("")){
                    Toast.makeText(SignupActivity.this,getResources().getString(R.string.invalid_password),Toast.LENGTH_LONG).show();
                    return;
                }
                if (Utils.emailValidator(emailStr.trim()) == false) {
                    Toast.makeText(SignupActivity.this,getResources().getString(R.string.invalid_email),Toast.LENGTH_LONG).show();
                    return;
                }
                if(passwordStr.length() < 6)
                {
                    Toast.makeText(SignupActivity.this,getResources().getString(R.string.min_password),Toast.LENGTH_LONG).show();
                    return;
                }
                if(termsCheckbox.isChecked() == false){
                    Toast.makeText(SignupActivity.this,getResources().getString(R.string.terms_alert),Toast.LENGTH_LONG).show();
                    return;
                }
                showLoader();
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailStr.trim(), passwordStr.trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final SignupModel signupModel = new SignupModel();
                                    signupModel.setEmail(emailStr.trim());
                                    signupModel.setFirstName(firstNameStr);
                                    signupModel.setLastName(lastNameStr);
                                    signupModel.setUsername(emailStr);
                                    // Sign in success, update UI with the signed-in user's information
                                    databaseReference.child("users").push().setValue(signupModel, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                            databaseReference.getKey();
                                            if (databaseError !=null){
                                                hideLoader();
                                                Toast.makeText(SignupActivity.this,getResources().getString(R.string.failed_registration),Toast.LENGTH_LONG).show();

                                            }else{
                                                signupModel.setUid(databaseReference.getKey());
                                                databaseReference.setValue(signupModel);
                                                hideLoader();
                                                Toast.makeText(SignupActivity.this,getResources().getString(R.string.successful_register),Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                                                finish();

                                            }
                                        }
                                    });


                                } else {

                                    hideLoader();
                                    Toast.makeText(SignupActivity.this,""+task.getException().getMessage(),Toast.LENGTH_LONG).show();
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
                    dialog = Utils.showProgress(SignupActivity.this);
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
