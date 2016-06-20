package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.Fragments.FragmentAvailability;
import com.smartsense.taxinearyou.Fragments.FragmentReview;
import com.smartsense.taxinearyou.utill.CircleImageView1;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.TimeActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PartnerDetails extends TimeActivity {

    Button btPartnerBookNow;
    TextView tvPartnerMoney;
    CircleImageView1 cvPartnerPic;
    public FragmentActivity activity;
    public static String partnerName;
    public static String taxiTypeName;
    public static String waitingTime;
    public static String partnerId;
    public static int available;
    public static ArrayList<String> rating = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btPartnerBookNow = (Button) findViewById(R.id.btPartnerBookNow);
        tvPartnerMoney = (TextView) findViewById(R.id.tvPartnerMoney);
        cvPartnerPic = (CircleImageView1) findViewById(R.id.cvPartnerPic);

        getSupportActionBar().setTitle(getIntent().getStringExtra("partnerName"));

        if (!TextUtils.isEmpty(getIntent().getStringExtra("logoPath")))
            Picasso.with(this)
                    .load(Constants.BASE_URL_IMAGE_POSTFIX + getIntent().getStringExtra("logoPath"))
                    .error(R.mipmap.img_place_holder)
                    .placeholder(R.mipmap.img_place_holder)
                    .into(cvPartnerPic);

        tvPartnerMoney.setText(getIntent().getStringExtra("ETA"));

        partnerName = getIntent().getStringExtra("partnerName");
        taxiTypeName = getIntent().getStringExtra("taxiTypeName");
        waitingTime = getIntent().getStringExtra("waitingTime");
        rating = getIntent().getStringArrayListExtra("rating");
        partnerId = getIntent().getStringExtra("partnerId");
        available = getIntent().getIntExtra("available", 0);

        ViewPager vpPartnerRating = (ViewPager) findViewById(R.id.vpPartnerRating);
        setupViewPager(vpPartnerRating);

        TabLayout tbPartnerRating = (TabLayout) findViewById(R.id.tbPartnerRatingTabLayout);
        tbPartnerRating.setupWithViewPager(vpPartnerRating);

        btPartnerBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_STATUS, "").equalsIgnoreCase("1")) {

                    StringBuilder builder = new StringBuilder();
                    JSONObject jsonData = new JSONObject();
                    try {
                        builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                                .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CommonUtil.utf8Convert(builder, Constants.Events.EVENT_CHECK_BOOK), null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (response.optJSONObject("json").optBoolean("bookingAllow")) {

                                startActivity(new Intent(PartnerDetails.this, BookingInfo.class));

                            } else {
                                CommonUtil.alertBox(PartnerDetails.this, response.optJSONObject("json").optString("reason"));
                            }
                            CommonUtil.cancelProgressDialog();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            CommonUtil.cancelProgressDialog();
                            CommonUtil.errorToastShowing(PartnerDetails.this);
                        }
                    });
                    CommonUtil.showProgressDialog(PartnerDetails.this,getResources().getString(R.string.get_data));
                    TaxiNearYouApp.getInstance().addToRequestQueue(request, "");

                } else
                    CommonUtil.alertBox(PartnerDetails.this, getResources().getString(R.string.msg_activate_account));
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_partner_details;
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