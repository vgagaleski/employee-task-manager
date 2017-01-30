package com.example.teodora.employeetaskmanager.Activities;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teodora.employeetaskmanager.Adapters.ContactsAdapter;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Toolbar toolbar;
    private String taskAssignee;
    private String taskAsigneeEmail;
    private String colorPriority = "#e2e619";
    private String taskAssigneeUid;
    private ProgressDialog mProgress;
    private FirebaseAuth mFirebaseAuth;;
    private DatabaseReference mDatabaseTasks;
    private DatabaseReference mDatabaseUsers;
    private LocalDatabase localDatabase;

    //Date Picker
    Calendar calendar ;
    DatePickerDialog datePickerDialog ;
    int Year, Month, Day ;
    private TextView dateTextView, priorityTextView, descriptionTextView, locationTextView ;
    private EditText assigneeEditText, taskEditText, projectEditText;
    private LinearLayout dueDateLayout, priorityLayout, descriptionLayout, locationLayout;

    //Autocomplete Text
    AutoCompleteTextView txtSearch;
    ContactsAdapter contactsAdapter;
    private String txtSearchTAG = "FALSE";
    private ContactModel contactModelAssignee;

    // Notification
    NotificationCompat.Builder notification;
    private static final int uniqueID = 12345;

    private FloatingActionButton fab;

    //Dialog Priority
    final CharSequence myList[] = { "High", "Medium", "Low" };
    private int i;
    private String txtDescription;
    private String txtLocation;
    private String AssignedByName;
    private String AssigneeId;
    private DatabaseReference newPost;

    private List<ContactModel> contactsList = new ArrayList<>();
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);

        // Adding Toolbar to AssignTask screen
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Assign task");
        setSupportActionBar(toolbar);

        //Notification
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        // Initialize Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseTasks = FirebaseDatabase.getInstance().getReference().child("Tasks");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        newPost = mDatabaseTasks.push();

        localDatabase = new LocalDatabase(getApplicationContext());
        contactsList = localDatabase.getAllContacts();

        //Autocomplete
        txtSearch = (AutoCompleteTextView) findViewById(R.id.etAsigneeName);
        txtSearch.setThreshold(1);
        contactsAdapter = new ContactsAdapter(this, R.layout.activity_assign_task, R.id.assignee_name, contactsList);
        txtSearch.setAdapter(contactsAdapter);

        txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                contactModelAssignee = (ContactModel) parent.getItemAtPosition(position);
                if (contactModelAssignee != null) {

                    txtSearchTAG = "TRUE";
                    taskAssignee = contactModelAssignee.getName();
                    taskAsigneeEmail = contactModelAssignee.getEmail();
                }
                else {

                    txtSearchTAG = "FALSE";

                }
            }
        });



        //Date Picker
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        dateTextView = (TextView) findViewById(R.id.etDueDate);
        priorityTextView = (TextView) findViewById(R.id.etPriority);
        descriptionTextView = (TextView) findViewById(R.id.etDescription);
        locationTextView = (TextView) findViewById(R.id.etLocation);

        dueDateLayout = (LinearLayout) findViewById(R.id.dueDateLayout);
        priorityLayout = (LinearLayout) findViewById(R.id.priorityLayout);
        descriptionLayout = (LinearLayout) findViewById(R.id.descriptionLayout);
        locationLayout = (LinearLayout) findViewById(R.id.locationLayout);

        assigneeEditText = (EditText) findViewById(R.id.etAsigneeName);
        taskEditText = (EditText) findViewById(R.id.etTaskName);
        projectEditText = (EditText) findViewById(R.id.etProjectName);

        mProgress = new ProgressDialog(AssignTaskActivity.this);

        dueDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog = DatePickerDialog.newInstance(AssignTaskActivity.this, Year, Month, Day);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#e2e619"));
                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
            }
        });

        priorityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPriorityDialog();
            }
        });

        descriptionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescriptionDialog();
            }
        });

        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showLocationDialog();
                Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                startActivityForResult(intent,1);
            }
        });

        // Floating Action Button Click Listener
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                addToFirebase();
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                address = data.getStringExtra("address");
                locationTextView.setText(address);
            }
            if (resultCode == Activity.RESULT_CANCELED) { }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int Year, int Month, int Day) {

        String date = "Selected Date : " + Day + "-" + Month+1 + "-" + Year;
        Toast.makeText(AssignTaskActivity.this, date, Toast.LENGTH_LONG).show();
        dateTextView.setText(Day + "-" + Month+1 + "-" + Year);
    }

    public void showPriorityDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AssignTaskActivity.this);
        builder.setTitle("Choose priority");

        //list of items
        final String[] items =  { "High", "Medium", "Low" };
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        i =which;

                    }
                });

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic

                        if (items[i].equals("High")) {
                        priorityTextView.setText("High");
                        }
                        if (items[i].equals("Medium")) {
                            priorityTextView.setText("Medium");
                        }
                        if (items[i].equals("Low")) {
                            priorityTextView.setText("Low");
                        }
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDescriptionDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_description, null);
        dialogBuilder.setView(dialogView);

        final EditText editDescription = (EditText) dialogView.findViewById(R.id.DescriptionAlert);
        final TextInputLayout textInputLayout = (TextInputLayout) dialogView.findViewById(R.id.DescriptionTIL);

        dialogBuilder.setTitle("Add description");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                //Do nothing here because we override this button later to change the close behaviour.
                //However, we still need this because on older versions of Android unless we
                //pass a handler the button doesn't get instantiated

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;

                TextWatcher inputTextWatcher = new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        editDescription.setError(null);
                    }
                };
                editDescription.addTextChangedListener(inputTextWatcher);

                txtDescription = editDescription.getText().toString();
                if (txtDescription.length() <= textInputLayout.getCounterMaxLength()) {

                    descriptionTextView.setText(txtDescription);
                    wantToCloseDialog = true;

                } else editDescription.setError("Maximum number of characters exceeded");


                if (wantToCloseDialog)
                    dialog.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });

    }

    public void showLocationDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_location, null);
        dialogBuilder.setView(dialogView);

        final EditText editLocation = (EditText) dialogView.findViewById(R.id.LocationAlert);
        final TextInputLayout textInputLayout = (TextInputLayout) dialogView.findViewById(R.id.LocationTIL);

        dialogBuilder.setTitle("Add location");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                //Do nothing here because we override this button later to change the close behaviour.
                //However, we still need this because on older versions of Android unless we
                //pass a handler the button doesn't get instantiated

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean wantToCloseDialog = false;

                TextWatcher inputTextWatcher = new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                    }
                    public void beforeTextChanged(CharSequence s, int start, int count, int after){
                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        editLocation.setError(null);
                    }
                };
                editLocation.addTextChangedListener(inputTextWatcher);

                txtLocation = editLocation.getText().toString();
                if (txtLocation.length() <= textInputLayout.getCounterMaxLength())
                {

                    locationTextView.setText(txtLocation);
                    wantToCloseDialog = true;

                }
                else  editLocation.setError("Maximum number of characters exceeded");


                if(wantToCloseDialog)
                    dialog.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });

    }


    public void addToFirebase()
    {
        final String Assignee = assigneeEditText.getText().toString().trim();
        final String Task = taskEditText.getText().toString().trim();
        final String Project = projectEditText.getText().toString().trim();
        final String DueDate = dateTextView.getText().toString().trim();
        final String Priority = priorityTextView.getText().toString().trim();
        final String Description = descriptionTextView.getText().toString().trim();
        final String Location = locationTextView.getText().toString().trim();

        if (Priority.equals("High")) colorPriority =  "#e91e63";
        else if  (Priority.equals("Medium")) colorPriority =  "#66bb6a";
        else if  (Priority.equals("Low")) colorPriority =  "#e2e619";

        final String currentUserId = mFirebaseAuth.getCurrentUser().getUid();
        final FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (Assignee.isEmpty() || Task.isEmpty() || Project.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AssignTaskActivity.this);
            builder.setMessage(R.string.assign_task_error_message)
                    .setTitle(R.string.signup_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (txtSearchTAG.equals("FALSE"))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(AssignTaskActivity.this);
            builder.setMessage(R.string.assign_task_error_message2)
                    .setTitle(R.string.signup_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        else if (checkInternetConnection()) {

            // Notification
            notification.setSmallIcon(R.drawable.ic_event_note_white_24dp);
            notification.setWhen(System.currentTimeMillis());
            notification.setContentTitle("New task assigned");
            notification.setContentText("You have assigned a new task");

            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(uniqueID, notification.build());

        final DatabaseReference currentUserName = mDatabaseUsers.child(mCurrentUser.getUid()).child("Name");
        currentUserName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("dataSnapshot", ":" + dataSnapshot);
                AssignedByName = dataSnapshot.getValue(String.class);
                Log.v("AssignedByName", ":" + AssignedByName);
                newPost.child("taskAssignedBy").setValue(AssignedByName);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Query getContactsQuery = mDatabaseUsers.orderByChild("Email").equalTo(taskAsigneeEmail);
        getContactsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("dataSnapshot", ":" + dataSnapshot);
                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                String[] keys = new String[map.size()];
                Object[] values = new Object[map.size()];
                if (map != null) {
                    int index = 0;
                    for (Map.Entry<String, Object> mapEntry : map.entrySet()) {
                        keys[index] = mapEntry.getKey();
                        Log.v("E_VALUE", "MAP:" + keys[index]);
                        values[index] = mapEntry.getValue();
                        Log.v("E_VALUE", "values[index]:" + values[index]);
                        index++;
                    }
                }
                AssigneeId = keys[0];
                newPost.child("taskAssigneeId").setValue(AssigneeId);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


            mProgress.setMessage("Adding data to Firebase... ");
            mProgress.show();

            newPost.child("taskAssignedById").setValue(currentUserId);
            newPost.child("taskAssignee").setValue(taskAssignee);
            newPost.child("taskDescription").setValue(Description);
            newPost.child("taskDueDate").setValue(DueDate);
            newPost.child("taskLocation").setValue(Location);
            newPost.child("taskName").setValue(Task);
            newPost.child("taskPercentage").setValue("0");
            newPost.child("taskPriority").setValue(colorPriority);
            newPost.child("taskProject").setValue(Project);

            mProgress.dismiss();
            Intent i = new Intent (AssignTaskActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected());
    }

}



