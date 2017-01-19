package com.example.teodora.employeetaskmanager.Activities;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import com.example.teodora.employeetaskmanager.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

public class AssignTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Toolbar toolbar;

    private String taskAssignee;
    private String taskName;
    private String taskProject;
    private String taskDueDate;
    private String taskPriority;
    private String taskDescription;
    private String taskAddress;

    private String taskAssigneeUid;

    //Date Picker
    Calendar calendar ;
    DatePickerDialog datePickerDialog ;
    int Year, Month, Day ;
    private TextView dateTextView, priorityTextView, descriptionTextView, locationTextView ;
    private LinearLayout dueDateLayout, priorityLayout, descriptionLayout, locationLayout;

    //Dialog Priority
    final CharSequence myList[] = { "High", "Medium", "Low" };
    private int i;
    private String txtDescription;

//    RelativeLayout rl;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);

        // Adding Toolbar to AssignTask screen
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Assign task");
        setSupportActionBar(toolbar);

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
                showLocationDialog();
            }
        });

    }

    @Override
    public void onDateSet(DatePickerDialog view, int Year, int Month, int Day) {

        String date = "Selected Date : " + Day + "-" + Month+1 + "-" + Year;
        Toast.makeText(AssignTaskActivity.this, date, Toast.LENGTH_LONG).show();
        dateTextView.setText(Day + "-" + Month+1 + "-" + Year);
    }

    public void showPriorityDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AssignTaskActivity.this);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
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

                txtDescription = editLocation.getText().toString();
                if (txtDescription.length() <= textInputLayout.getCounterMaxLength())
                {

                    descriptionTextView.setText(txtDescription);
                    wantToCloseDialog = true;

                }
                else  editLocation.setError("Maximum number of characters exceeded");


                if(wantToCloseDialog)
                    dialog.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });


    }




}



