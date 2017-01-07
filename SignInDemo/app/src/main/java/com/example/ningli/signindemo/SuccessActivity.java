package com.example.ningli.signindemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import static com.example.ningli.signindemo.R.id.ViewPager;

public class SuccessActivity extends AppCompatActivity {
    private String USER_ID;
    public  PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Intent intent = getIntent();
        USER_ID = intent.getStringExtra("UserId");

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(ViewPager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.view_pager_tab);
        tabLayout.setupWithViewPager(viewPager);
        //tabLayout.setBackgroundResource(R.color.tab_bg);


    }

    protected class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return AddFragment.newInstance(position, USER_ID);
            else if (position == 1)
                return ShowFragment.newInstance(position, USER_ID);
            else
                return DoneFragment.newInstance(position, USER_ID);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public CharSequence getPageTitle(int position) {
            if (position == 0)
                return "Add";
            else if (position == 1)
                return "Show";
            else
                return "Done";
        }

    }
}
