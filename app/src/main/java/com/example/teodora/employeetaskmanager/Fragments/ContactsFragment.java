package com.example.teodora.employeetaskmanager.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.teodora.employeetaskmanager.Activities.AddContactActivity;
import com.example.teodora.employeetaskmanager.Activities.AssignTaskActivity;
import com.example.teodora.employeetaskmanager.Activities.TaskDetailsActivity;
import com.example.teodora.employeetaskmanager.Adapters.ContactsRecyclerViewAdapter;
import com.example.teodora.employeetaskmanager.Adapters.TasksRecyclerViewAdapter;
import com.example.teodora.employeetaskmanager.Models.ContactModel;
import com.example.teodora.employeetaskmanager.Models.TaskModel;
import com.example.teodora.employeetaskmanager.Other.FragmentLifecycle;
import com.example.teodora.employeetaskmanager.Other.LocalDatabase;
import com.example.teodora.employeetaskmanager.Other.RecyclerTouchListener;
import com.example.teodora.employeetaskmanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, FragmentLifecycle {
    public ContactsFragment() {
        // Required empty public constructor
    }
    private static final String TAG = ContactsFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ContactsRecyclerViewAdapter contactsRecyclerViewAdapter;
    private List <ContactModel> contactsList = new ArrayList<>();
    private List <String> contactIDs = new ArrayList<>();
    private ProgressBar progressBar;

    private DatabaseReference mDatabaseContacts;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseUser;

    private FirebaseAuth mAuth;
    private String currentUserId;

    private LocalDatabase localDatabase;

    private FloatingActionButton fabAddContact;

    private boolean isFirstTime = false;
    private boolean canAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("onCreate Contacts", "" + "");

        isFirstTime = true;

//        setHasOptionsMenu(true);
//
//        Firebase.setAndroidContext(getContext());

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseContacts = mDatabaseUsers.child(currentUserId).child("Contacts");


    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        fetchContacts();
//        Log.v("onResume in Contacts", "" + "");
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        Log.v("onCreateView Contacts", "" + "");

        localDatabase = new LocalDatabase(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.contactsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));



        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        if (isFirstTime){ fetchContacts(); }

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        onRefresh();
                                    }
                                }
        );







        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ContactModel contactModel = contactsList.get(position);
                Toast.makeText(getContext(), contactModel.getName() + " is selected!", Toast.LENGTH_SHORT).show();

//                Intent taskDetailsIntent = new Intent(getContext(),TaskDetailsActivity.class);
//                taskDetailsIntent.putExtra("taskDetails",tasksList.get(position));
//                startActivity(taskDetailsIntent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // Floating Action Button Click Listener
        fabAddContact = (FloatingActionButton) view.findViewById(R.id.fab);

        fabAddContact.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getContext(), AddContactActivity.class);
//                intent.putExtra(AddContactActivity.SELECTED_TAB_EXTRA_KEY, AddContactActivity.NEW_POSTS_TAB);
                startActivity(intent);
            }
        });


        return view;
    }


    @Override
    public void onRefresh() {
        fetchContacts();

    }


    @Override
    public void onPauseFragment() {
        Log.i(TAG, "onPauseFragment()");
//        Toast.makeText(getActivity(), "onPauseFragment():" + TAG, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment() {
        Log.i(TAG, "onResumeFragment()");
//        Toast.makeText(getActivity(), "onResumeFragment():" + TAG, Toast.LENGTH_SHORT).show();
        fetchContacts();
    }

    private void fetchContacts() {
        isFirstTime = false;
        Log.v("contactsListClear: ", " " + contactsList);


        if (checkInternetConnection()){


            // Reading all contacts
            if (localDatabase.getAllContacts()!=null){
                Log.d("Reading: ", "Reading all contacts..");
                contactsList = localDatabase.getAllContacts();
                for (ContactModel cn : contactsList) {
                    String log =  " ,Name: " + cn.getName() + " ,Email: " + cn.getEmail() + " ,Phone: " + cn.getMobilePhone();
                    // Writing Contacts to log
                    Log.d("DatabaseRow: ", log);
                }
                contactsRecyclerViewAdapter = new ContactsRecyclerViewAdapter(getContext(), contactsList);
                recyclerView.setAdapter(contactsRecyclerViewAdapter);
            }

//            mDatabaseCurrentUser = mDatabaseUsers.child(currentUserId);

//            mDatabaseContacts.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    contactIDs.clear();
//                    for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()){
//                        contactIDs.add(contactSnapshot.getKey());
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                    Log.e("Database Error: " ,"No databaseSnapshot caused by" + databaseError);
//                    swipeRefreshLayout.setRefreshing(false);
//
//                }
//            });
//
//            for (String id : contactIDs)
//            {
//                Log.v("id: ", " " + id);
//                mDatabaseUser = mDatabaseUsers.child(id);
//
//                mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        Log.v("DataSnapshot: ", " " + dataSnapshot);
//                            ContactModel contactModel = dataSnapshot.getValue(ContactModel.class);
//
//                        if (contactsList.isEmpty()){ Log.v("tuka: ", " " + ""); contactsList.add(contactModel); }
//
//                        else {
//                            canAdd = true;
//                            for (int i = 0; i< contactsList.size(); i++) {
//                                if (contactsList.get(i).getEmail().equals(contactModel.getEmail().toString())) {
//                                    canAdd = false;
//                                }
//                            }
//                                if (canAdd) { contactsList.add(contactModel); }
//
//                        }
//
//
//
//
////                        }
//                    }
//
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                        Log.e("Database Error: " ,"No databaseSnapshot caused by" + databaseError);
//                        swipeRefreshLayout.setRefreshing(false);
//
//                    }
//                });
//
//
//                contactsRecyclerViewAdapter = new ContactsRecyclerViewAdapter(getContext(), contactsList);
//                swipeRefreshLayout.setRefreshing(false);
//                recyclerView.setAdapter(contactsRecyclerViewAdapter);
//
//
//
//            }
//
////            contactsRecyclerViewAdapter = new ContactsRecyclerViewAdapter(getContext(), contactsList);
////            swipeRefreshLayout.setRefreshing(false);
////            recyclerView.setAdapter(contactsRecyclerViewAdapter);
//
//
        }
        else
            Toast.makeText(getContext(), "No Internet connection", Toast.LENGTH_LONG).show();

        //contactsList.clear();
        swipeRefreshLayout.setRefreshing(false);
    }



    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected());
    }


}
