package com.example.teodora.employeetaskmanager.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teodora.employeetaskmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userEmail;
    private EditText userPassword;
    private EditText userAddress;
    private EditText userMobilePhone;

    private Button registerBtn;

    private FirebaseAuth mFirebaseAuth;;
    private DatabaseReference mDatabaseUsers;

    private ProgressDialog mProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = (EditText) findViewById(R.id.new_user_name);
        userEmail = (EditText) findViewById(R.id.new_user_email);
        userPassword = (EditText) findViewById(R.id.new_user_pass);
        userAddress = (EditText) findViewById(R.id.new_user_address);
        userMobilePhone = (EditText) findViewById(R.id.new_user_phone);

        registerBtn = (Button) findViewById(R.id.registerBtn);

        // Initialize Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        //Progress Dialog
        mProgress = new ProgressDialog(this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnection()){
                    startRegister();
                }
                else
                    Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void startRegister(){

        final String name = userName.getText().toString().trim();
        final String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        final String address = userAddress.getText().toString().trim();
        final String mobilePhone = userMobilePhone.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage(R.string.signup_error_message)
                    .setTitle(R.string.signup_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            mProgress.setMessage("Signing up... ");
            mProgress.show();
            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String user_id = mFirebaseAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = mDatabaseUsers.child(user_id);
                                current_user_db.child("Name").setValue(name);
                                current_user_db.child("Email").setValue(email);
                                current_user_db.child("Address").setValue(address);
                                current_user_db.child("MobilePhone").setValue(mobilePhone);
                                current_user_db.child("Image").setValue("default");

                                Log.v("E_VALUE", "Editor(SharedPreferences): " + name +" "+ email +" " + "default");

                                mProgress.dismiss();

                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                mProgress.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage(task.getException().getMessage())
                                        .setTitle(R.string.login_error_title)
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
        }

    }

    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected());
    }

}
