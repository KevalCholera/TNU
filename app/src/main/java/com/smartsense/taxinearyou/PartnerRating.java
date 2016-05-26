package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smartsense.taxinearyou.Fragments.FragmentAvailability;
import com.smartsense.taxinearyou.Fragments.FragmentReview;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PartnerRating extends AppCompatActivity {

    Button btPartnerBookNow;
    TextView tvPartnerMoney;

    public FragmentActivity activity;
    public JSONArray jsonArray;
//    Context context;
//    AttributeSet attrs;
//    int defStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btPartnerBookNow = (Button) findViewById(R.id.btPartnerBookNow);
        tvPartnerMoney = (TextView) findViewById(R.id.tvPartnerMoney);

        ViewPager vpPartnerRating = (ViewPager) findViewById(R.id.vpPartnerRating);
        setupViewPager(vpPartnerRating);

        TabLayout tbPartnerRating = (TabLayout) findViewById(R.id.tbPartnerRatingTabLayout);
        tbPartnerRating.setupWithViewPager(vpPartnerRating);

        btPartnerBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PartnerRating.this, BookingInfo.class));
            }
        });

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentAvailability(), "Availability");


        adapter.addFragment(new FragmentReview(), "Reviews");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.ratingforsearch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}