package com.example.alihfight.alifightapp.Admin.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.alihfight.alifightapp.Admin.Fragments.AdminHomeFragment;
import com.example.alihfight.alifightapp.Admin.Fragments.ChatFragment;
import com.example.alihfight.alifightapp.Admin.Fragments.MembersFragment;
import com.example.alihfight.alifightapp.Admin.Fragments.ScheduleFragment;
import com.example.alihfight.alifightapp.Admin.Fragments.StatsFragment;
import com.example.alihfight.alifightapp.R;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.newspaper);
        tabLayout.getTabAt(1).setIcon(R.drawable.member);
        tabLayout.getTabAt(2).setIcon(R.drawable.chat);
        tabLayout.getTabAt(3).setIcon(R.drawable.stats);
        tabLayout.getTabAt(4).setIcon(R.drawable.calendar);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AdminHomeFragment(), "");
        adapter.addFragment(new MembersFragment(), "");
        adapter.addFragment(new ChatFragment(), "");
        adapter.addFragment(new StatsFragment(), "");
        adapter.addFragment(new ScheduleFragment(), "");
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
