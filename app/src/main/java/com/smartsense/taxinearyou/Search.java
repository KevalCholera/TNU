package com.smartsense.taxinearyou;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartsense.taxinearyou.Fragments.FragmentBook;
import com.smartsense.taxinearyou.Fragments.FragmentCredit;
import com.smartsense.taxinearyou.Fragments.FragmentMenu;
import com.smartsense.taxinearyou.Fragments.FragmentMyTrips;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    private TabLayout tbSearchTab;
    CoordinatorLayout clSearch;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.vpSearchPager);
        setupViewPager(viewPager);

        clSearch = (CoordinatorLayout) findViewById(R.id.clSearch);
        tbSearchTab = (TabLayout) findViewById(R.id.tbSearchTab);
        tbSearchTab.setupWithViewPager(viewPager);
        setupTabIcons();

        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Search");
        arrayList.add("My Trips");
        arrayList.add("Credit");
        arrayList.add("Menu");

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
}