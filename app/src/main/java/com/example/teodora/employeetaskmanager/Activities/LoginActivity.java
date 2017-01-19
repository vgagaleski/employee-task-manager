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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teodora.employeetaskmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText userEmail;
    private EditText userPassword;
    private Button loginBtn;

    private FirebaseAuth mFirebaseAuth;;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUsers;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = (EditText) findViewById(R.id.user_email);
        userPassword = (EditText) findViewById(R.id.user_pass);

        loginBtn = (Button) findViewById(R.id.loginBtn);

        // Initialize Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);

        mProgress = new ProgressDialog(this);

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){

                if(firebaseAuth.getCurrentUser() != null){

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                }
            }
        };

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnection()){
                    checkLogin();
                }
                else
                    Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_LONG).show();

            }
        });
    }


    private void checkLogin(){

        final String email = userEmail.getText().toString().trim();
        final String password = userPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage(R.string.login_error_message)
                    .setTitle(R.string.login_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {

            mProgress.setMessage("Checking login... ");
            mProgress.show();

            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

//                                 We should check if the email entered in SharedPref previously is the same with the email
//                                 that is currently logging in
//                                if (email.equals(pref.getString("KEY_EMAIL",null))){
//
//                                    mProgress.dismiss();
//                                    checkUserExist();
//
//                                }
//                                else {
//
//                                    editor.clear();
//                                    editor.commit();
//                                    // We should take all user elements from the Firebase Database and put them in editor (Shared Preferences)
//                                    editor.putString("KEY_EMAIL", email);
//                                    editor.commit();
                                    mProgress.dismiss();
                                    checkUserExist();

                                    final String user_id = mFirebaseAuth.getCurrentUser().getUid();

//                                    mDatabaseUsers.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                            ContactModel contactModel = dataSnapshot.getValue(ContactModel.class);
//                                            if (contactModel.getName().equals(null)){
//                                                editor.putString("KEY_NAME", contactModel.getName());
//                                                editor.putString("KEY_PHONE", contactModel.getPhone());
//                                            }
//
//                                        }
//                                        @Override
//                                        public void onCancelled(DatabaseError databaseError) {
//
//                                        }
//                                    });

                               // }
                            } else {
                                mProgress.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

    private void checkUserExist(){

        final String user_id = mFirebaseAuth.getCurrentUser().getUid();
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(user_id)){

                    Intent mainIntent = new Intent (LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
                else {

                    // MyProfileActivity.class should be switched with EditProfileActivity for users that are logged in with
                    // Google, Facebook.. FURTHER IMPLEMENTATION
                    Intent setupIntent = new Intent (LoginActivity.this, MyProfileActivity.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Sign Up
    public void onSignUpPressed(View view) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }

    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
