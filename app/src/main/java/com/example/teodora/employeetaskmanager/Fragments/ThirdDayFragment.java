package com.example.teodora.employeetaskmanager.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teodora.employeetaskmanager.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ThirdDayFragment extends Fragment {

    public ThirdDayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_day, container, false);


        //Calendar
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd"); // Set your date format
        String currentData = sdf.format(d); // Get Date String according to date format

        TextView showDate = (TextView) view.findViewById(R.id.calendarTextView);
        showDate.setText(currentData);

        return view;
    }
}
