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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.Fragments.FragmentAvailability;
import com.smartsense.taxinearyou.Fragments.FragmentReview;
import com.smartsense.taxinearyou.utill.CircleImageView1;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PartnerDetails extends AppCompatActivity {

    Button btPartnerBookNow;
    TextView tvPartnerMoney;
    CircleImageView1 cvPartnerPic;
    public FragmentActivity activity;
    public static String partnerName;
    public static String taxiTypeName;
    public static String waitingTime;
    public static String partnerId;
    public static ArrayList<String> rating = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btPartnerBookNow = (Button) findViewById(R.id.btPartnerBookNow);
        tvPartnerMoney = (TextView) findViewById(R.id.tvPartnerMoney);
        cvPartnerPic = (CircleImageView1) findViewById(R.id.cvPartnerPic);

        getSupportActionBar().setTitle(getIntent().getStringExtra("partnerName"));

        if (!TextUtils.isEmpty(getIntent().getStringExtra("logoPath")))
            Picasso.with(this)
                    .load(Constants.BASE_URL_IMAGE_POSTFIX + getIntent().getStringExtra("logoPath"))
                    .error(R.mipmap.imgtnulogo)
                    .placeholder(R.mipmap.imgtnulogo)
                    .into(cvPartnerPic);

        tvPartnerMoney.setText(getIntent().getStringExtra("ETA"));

        partnerName = getIntent().getStringExtra("partnerName");
        taxiTypeName = getIntent().getStringExtra("taxiTypeName");
        waitingTime = getIntent().getStringExtra("waitingTime");
        rating = getIntent().getStringArrayListExtra("rating");
        partnerId = getIntent().getStringExtra("partnerId");

        ViewPager vpPartnerRating = (ViewPager) findViewById(R.id.vpPartnerRating);
        setupViewPager(vpPartnerRating);

        TabLayout tbPartnerRating = (TabLayout) findViewById(R.id.tbPartnerRatingTabLayout);
        tbPartnerRating.setupWithViewPager(vpPartnerRating);

        btPartnerBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_STATUS, "").equalsIgnoreCase("1"))
                    startActivity(new Intent(PartnerDetails.this, BookingInfo.class));
                else
                    CommonUtil.alertBox(PartnerDetails.this, getResources().getString(R.string.msg_activate_account), false, false);
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