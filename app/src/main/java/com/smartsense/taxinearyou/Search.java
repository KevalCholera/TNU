package com.smartsense.taxinearyou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.onesignal.OneSignal;
import com.smartsense.taxinearyou.Fragments.FragmentBook;
import com.smartsense.taxinearyou.Fragments.FragmentCredit;
import com.smartsense.taxinearyou.Fragments.FragmentMenu;
import com.smartsense.taxinearyou.Fragments.FragmentMyTrips;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.WakeLocker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    private TabLayout tbSearchTab;
    CoordinatorLayout clSearch;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private LinearLayout llToolbarAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        registerReceiver(tripMessageReceiver, new IntentFilter(String.valueOf(Constants.Events.UPDATE_EMAIL)));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        llToolbarAll = (LinearLayout) findViewById(R.id.llToolbarAll);
        ViewPager viewPager = (ViewPager) findViewById(R.id.vpSearchPager);
        setupViewPager(viewPager);
        llToolbarAll.setVisibility(View.VISIBLE);
        clSearch = (CoordinatorLayout) findViewById(R.id.clSearch);
        tbSearchTab = (TabLayout) findViewById(R.id.tbSearchTab);
        tbSearchTab.setupWithViewPager(viewPager);
        setupTabIcons();

        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Search");
        arrayList.add("My Trips");
        arrayList.add("Wallet");
        arrayList.add("Menu");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    llToolbarAll.setVisibility(View.VISIBLE);
                } else
                    llToolbarAll.setVisibility(View.GONE);
                getSupportActionBar().setTitle(arrayList.get(position));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int pos) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentBook().newInstance(getIntent().getBooleanExtra("checkFlag", false)), getResources().getString(R.string.book));
        adapter.addFragment(new FragmentMyTrips(), getResources().getString(R.string.my_trip));
        adapter.addFragment(new FragmentCredit(), getResources().getString(R.string.credit));
        adapter.addFragment(new FragmentMenu(), getResources().getString(R.string.menu));
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {


        RelativeLayout linearLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.element_search, null);
        ImageView tabIconOne = (ImageView) linearLayout.findViewById(R.id.ivElementSearch);
        tabIconOne.setImageResource(R.mipmap.ic_imgtab1_3x);
        TextView tabOne = (TextView) linearLayout.findViewById(R.id.tab);
        tabOne.setText(R.string.book);
        tbSearchTab.getTabAt(0).setCustomView(linearLayout);

        RelativeLayout linearLayout1 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.element_search, null);
        ImageView tabIconTwo = (ImageView) linearLayout1.findViewById(R.id.ivElementSearch);
        tabIconTwo.setImageResource(R.mipmap.ic_baggage);
        TextView tabTwo = (TextView) linearLayout1.findViewById(R.id.tab);
        tabTwo.setText(R.string.my_trip);
        tbSearchTab.getTabAt(1).setCustomView(linearLayout1);

        RelativeLayout linearLayout2 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.element_search, null);
        ImageView tabIconThree = (ImageView) linearLayout2.findViewById(R.id.ivElementSearch);
        tabIconThree.setImageResource(R.mipmap.coins2);
        TextView tabThree = (TextView) linearLayout2.findViewById(R.id.tab);
        tabThree.setText(R.string.credit);
        tbSearchTab.getTabAt(2).setCustomView(linearLayout2);

        RelativeLayout linearLayout3 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.element_search, null);
        ImageView tabIconFour = (ImageView) linearLayout3.findViewById(R.id.ivElementSearch);
        tabIconFour.setImageResource(R.mipmap.hamburger);
        TextView tabFour = (TextView) linearLayout3.findViewById(R.id.tab);
        tabFour.setText(R.string.menu);
        tbSearchTab.getTabAt(3).setCustomView(linearLayout3);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

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
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private final BroadcastReceiver tripMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WakeLocker.acquire(context);
            Log.i("Push ", intent.getStringExtra(Constants.EXTRAS));
            try {
                JSONObject pushData = new JSONObject(intent.getStringExtra(Constants.EXTRAS));
//                CommonUtil.storeUserData(pushData.optJSONObject("user"));
                if (pushData.optInt("reqType") == 1) {
                    CommonUtil.byToastMessage(Search.this, getResources().getString(R.string.session_expire));
                    SharedPreferenceUtil.clear();
                    SharedPreferenceUtil.save();
                    OneSignal.sendTag("emailId", "");
                    startActivity(new Intent(Search.this, SignIn.class));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            WakeLocker.release();
        }
    };

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(tripMessageReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}