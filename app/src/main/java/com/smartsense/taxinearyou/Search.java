package com.smartsense.taxinearyou;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private final List<Drawable> imageViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        registerReceiver(tripMessageReceiver, new IntentFilter(String.valueOf(Constants.Events.UPDATE_EMAIL)));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.vpSearchPager);
        setupViewPager(viewPager);

        clSearch = (CoordinatorLayout) findViewById(R.id.clSearch);
        tbSearchTab = (TabLayout) findViewById(R.id.tbSearchTab);
        tbSearchTab.setupWithViewPager(viewPager);
//        setupTabIcons();

        imageViews.add(ContextCompat.getDrawable(this, R.mipmap.car_white));
        imageViews.add(ContextCompat.getDrawable(this, R.mipmap.ic_baggage));
        imageViews.add(ContextCompat.getDrawable(this, R.mipmap.coins2));
        imageViews.add(ContextCompat.getDrawable(this, R.mipmap.hamburger));

        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Search");
        arrayList.add("My Trips");
        arrayList.add("Credit");
        arrayList.add("Menu");

        tbSearchTab.setupWithViewPager(viewPager);
        for (int i = 0; i < tbSearchTab.getTabCount(); i++) {
            tbSearchTab.getTabAt(i).setIcon(imageViews.get(i));
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

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
        adapter.addFragment(new FragmentBook(), getResources().getString(R.string.book));
        adapter.addFragment(new FragmentMyTrips(), getResources().getString(R.string.my_trip));
        adapter.addFragment(new FragmentCredit(), getResources().getString(R.string.credit));
        adapter.addFragment(new FragmentMenu(), getResources().getString(R.string.menu));
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.element_search, null);
        tabOne.setText(R.string.book);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.car_white, 0, 0);
        tbSearchTab.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.element_search, null);
        tabTwo.setText(R.string.my_trip);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_baggage, 0, 0);
        tbSearchTab.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.element_search, null);
        tabThree.setText(R.string.credit);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.coins2, 0, 0);
        tbSearchTab.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.element_search, null);
        tabFour.setText(R.string.menu);
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.hamburger, 0, 0);
        tbSearchTab.getTabAt(3).setCustomView(tabFour);
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
                CommonUtil.storeUserData(pushData.optJSONObject("user"));
                if (pushData.optInt("reqType") == 1 ) {
                    Toast.makeText(Search.this, Search.this.getResources().getString(R.string.session_expire), Toast.LENGTH_SHORT).show();
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