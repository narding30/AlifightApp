package com.example.alihfight.alifightapp.User.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.alihfight.alifightapp.Admin.Fragments.AdminHomeFragment;
import com.example.alihfight.alifightapp.Admin.Fragments.ChatFragment;
import com.example.alihfight.alifightapp.Admin.Fragments.MembersFragment;
import com.example.alihfight.alifightapp.Admin.Fragments.ScheduleFragment;
import com.example.alihfight.alifightapp.Admin.Fragments.StatsFragment;
import com.example.alihfight.alifightapp.MainActivity;
import com.example.alihfight.alifightapp.R;
import com.example.alihfight.alifightapp.User.Fragments.ChatFragmentUser;
import com.example.alihfight.alifightapp.User.Fragments.NewsFragmentUser;
import com.example.alihfight.alifightapp.User.Fragments.ProfileFragmentUser;
import com.example.alihfight.alifightapp.User.Fragments.ScheduleFragmentUser;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UserHomeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton imageButton;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        tabLayout = findViewById(R.id.tabsUser);
        viewPager = findViewById(R.id.viewpagerUser);
        setupViewPager(viewPager);

        imageButton = findViewById(R.id.btnlogoutUser);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                AlertDialog.Builder builder = new AlertDialog.Builder(UserHomeActivity.this);
                builder.setTitle("Log-out");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        firebaseAuth.signOut();
                        startActivity(new Intent(UserHomeActivity.this, MainActivity.class));
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();



            }
        });



        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.newspaper);
        tabLayout.getTabAt(1).setIcon(R.drawable.chat);
        tabLayout.getTabAt(2).setIcon(R.drawable.calendar);
        tabLayout.getTabAt(3).setIcon(R.drawable.profileicon);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewsFragmentUser(), "");
        adapter.addFragment(new ChatFragmentUser(), "");
        adapter.addFragment(new ScheduleFragmentUser(), "");
        adapter.addFragment(new ProfileFragmentUser(), "");
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

    boolean twice;
    @Override
    public void onBackPressed() {
        if (twice == true){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        Toast.makeText(this, "Please press BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
            }
        }, 3000);
        twice = true;
    }
}
