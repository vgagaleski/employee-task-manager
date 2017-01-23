package com.example.teodora.employeetaskmanager.Adapters;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.teodora.employeetaskmanager.Fragments.AssignedTasksFragment;
import com.example.teodora.employeetaskmanager.Fragments.ContactsFragment;
import com.example.teodora.employeetaskmanager.Fragments.MyTasksFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

//    public MyPagerAdapter(FragmentManager fm) {
//        super(fm);
//        this.fragments = new ArrayList<Fragment>();
//        fragments.add(new MyTasksFragment());
//        fragments.add(new AssignedTasksFragment());
//        fragments.add(new ContactsFragment());
//    }



    public MyPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}




