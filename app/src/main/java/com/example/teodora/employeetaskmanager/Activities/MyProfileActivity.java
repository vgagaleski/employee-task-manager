package com.example.teodora.employeetaskmanager.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.teodora.employeetaskmanager.Models.ContactModel;
import com.example.teodora.employeetaskmanager.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {

    private TextView userProfileName, userAddress, userEmail, userMobilePhone;
    private CircleImageView userProfilePhoto;
    private ProgressDialog mProgress;


    private Uri ImageUri = null;

    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private StorageReference mStorageImage;
    private String user_id;

    private static int GALLERY_REQUEST = 1;

    private ContactModel contactModel;
    private StorageReference storageRef;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        userProfileName = (TextView) findViewById(R.id.user_profile_name);
        userAddress = (TextView) findViewById(R.id.userAddress);
        userEmail = (TextView) findViewById(R.id.userEmail);
        userMobilePhone =(TextView) findViewById(R.id.userMobilePhone) ;

        userProfilePhoto = (CircleImageView) findViewById(R.id.user_profile_photo);

        mProgress = new ProgressDialog(this);


        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_images");
        user_id = mAuth.getCurrentUser().getUid();

        storage = FirebaseStorage.getInstance();

        setUserContent();


        userProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST  && resultCode == RESULT_OK && data != null && data.getData() != null) {
            ImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ImageUri);
                userProfilePhoto.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void setUserContent(){

        if (checkInternetConnection()){
        DatabaseReference mDatabaseCurrentUser =  mDatabaseUsers.child(user_id);

        mDatabaseCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.v("DataSnapshot: ", " " + dataSnapshot);
                    contactModel = dataSnapshot.getValue(ContactModel.class);
                    userProfileName.setText(contactModel.getName());
                    userAddress.setText(contactModel.getAddress());
                    userEmail.setText(contactModel.getEmail());
                    userMobilePhone.setText(contactModel.getMobilePhone());
                    if (!(contactModel.getImage().equals("default"))) {
                        storageRef = storage.getReferenceFromUrl(contactModel.getImage());
                        Glide.with(getApplicationContext())
                                .using(new FirebaseImageLoader())
                                .load(storageRef)
                                .into(userProfilePhoto);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("Database Error: " ,"No databaseSnapshot caused by" + databaseError);


                }
            });






        }
        else
            Toast.makeText(this, "No Internet connection", Toast.LENGTH_LONG).show();

    }


    public void uploadImage(){
        if (checkInternetConnection()){
//            mProgress.setMessage("Finishing setup...");
//            mProgress.show();
            StorageReference filepath = null;
            filepath = mStorageImage.child(ImageUri.getLastPathSegment());
            filepath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String downloadUri = taskSnapshot.getDownloadUrl().toString();

                    mDatabaseUsers.child(user_id).child("Image").setValue(downloadUri);

//                    mProgress.dismiss();

                    Intent mainIntent = new Intent (MyProfileActivity.this, MyProfileActivity.class);
//                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            });
        }
        else
            Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_LONG).show();
    }



    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected());
    }




//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
//    }
//
}
