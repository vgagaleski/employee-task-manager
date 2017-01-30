package com.example.teodora.employeetaskmanager.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.teodora.employeetaskmanager.Fragments.FifthDayFragment;
import com.example.teodora.employeetaskmanager.Fragments.FirstDayFragment;
import com.example.teodora.employeetaskmanager.Fragments.ForthDayFragment;
import com.example.teodora.employeetaskmanager.Fragments.SecondDayFragment;
import com.example.teodora.employeetaskmanager.Fragments.SeventhDayFragment;
import com.example.teodora.employeetaskmanager.Fragments.SixthDayFragment;
import com.example.teodora.employeetaskmanager.Fragments.ThirdDayFragment;
import com.example.teodora.employeetaskmanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NextDaysActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_days);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Next 7 Days");

        //Calendar
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd"); // Set your date format
        String currentData = sdf.format(d); // Get Date String according to date format

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        //Calendar
        Calendar calendar = Calendar.getInstance();
        String[] array = new String[7];
        // Set date format
        SimpleDateFormat sdf = new SimpleDateFormat("EE dd");
        Date d = calendar.getTime();
        array[0] = sdf.format(d);

        for (int i = 1; i < 7 ; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            d = calendar.getTime();
            array[i] = sdf.format(d);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FirstDayFragment(), array[0]);
        adapter.addFragment(new SecondDayFragment(),  array[1]);
        adapter.addFragment(new ThirdDayFragment(), array[2]);
        adapter.addFragment(new ForthDayFragment(),  array[3]);
        adapter.addFragment(new FifthDayFragment(), array[4]);
        adapter.addFragment(new SixthDayFragment(),  array[5]);
        adapter.addFragment(new SeventhDayFragment(),  array[6]);
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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
}
