package com.example.teodora.employeetaskmanager.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teodora.employeetaskmanager.Models.ContactModel;
import com.example.teodora.employeetaskmanager.Other.LocalDatabase;
import com.example.teodora.employeetaskmanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mUserEmail;
    private Button saveBtn;
    private ProgressDialog mProgress;
    private ContactModel contactModel = new ContactModel();
    private String userName;
    private String userEmail;
    private Boolean isInDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mCurrentUserEmail;
    private DatabaseReference mDatabaseUserEmail;
    private DatabaseReference mDatabaseContacts;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseCurrentUser;
    private LocalDatabase localDatabase;
    private ArrayList<ContactModel> contactsList = new ArrayList<>();
    private String currentUserId;
    private String idToBeAdded;
    private boolean flagNoSuchEmail = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        localDatabase = new LocalDatabase(this);

        mUserName = (EditText) findViewById(R.id.userNameText);
        mUserEmail = (EditText) findViewById(R.id.userEmailText);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        mProgress = new ProgressDialog(AddContactActivity.this);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mCurrentUserEmail = mCurrentUser.getEmail();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnection()) {
                    addContact();
                } else
                    Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_LONG).show();
            }
        });
    }

        public void addContact () {

            mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
            mDatabaseContacts = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid()).child("Contacts");
            mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

            if (validateFields() && checkInternetConnection()) {

                mProgress.setMessage("Adding contact... ");
                mProgress.show();
                final String email = mUserEmail.getText().toString().trim();
                final String name = mUserName.getText().toString().trim();
                final Query getContactsQuery = mDatabaseUsers.orderByChild("Email").equalTo(email);

                getContactsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot usersSnapshot : dataSnapshot.getChildren()) {

                            flagNoSuchEmail = false;
                            idToBeAdded = usersSnapshot.getKey();

                            if (idToBeAdded != null) {
                                mProgress.setMessage("Posting to database...");
                                mProgress.show();
                                mDatabaseContacts.child(idToBeAdded).setValue("true");

                                // Inserting Contact
                                Log.d("Insert: ", "Inserting contact into Local database..");
                                localDatabase.addContact(new ContactModel(name,email,idToBeAdded));


                                mProgress.dismiss();
                                Intent intent = new Intent (AddContactActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }

                        if (flagNoSuchEmail)
                        {
                            Toast.makeText(getBaseContext(), "There is no user with that e-mail in the database, please try again", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                        Toast.makeText(getBaseContext(), "onCancelled", Toast.LENGTH_LONG).show();
                    }

                });

                mProgress.dismiss();
            }
        }


    public boolean validateFields (){
        boolean valid = false;

        if (mUserName.getText().toString().isEmpty()) {
            mUserName.setError("User name is empty!");
            valid = false;
        }
        else {
            userName = String.valueOf(mUserName.getText().toString());
            Log.v("E_VALUE", "userName:" + userName);
            valid = true;
        }

        if ((mUserEmail.getText().toString().isEmpty())) {
            mUserEmail.setError("Email address is empty!");
            valid = false;
        }

        else if (mCurrentUserEmail.equals(mUserEmail.getText().toString().trim())){
            mUserEmail.setError("You cannot add your own Email Address as a contact");
            valid = false;
        }

        else {
            userEmail = String.valueOf(mUserEmail.getText().toString());
            valid = true;
        }

        if (valid){
            contactModel.setName(userName);
        }

        return valid;
    }

    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected());
    }

}
