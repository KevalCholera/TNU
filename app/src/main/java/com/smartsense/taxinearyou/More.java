package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class More extends AppCompatActivity implements View.OnClickListener {

    TextView tvMoreEventBooking, tvMoreContactUs, tvMoreAboutUs,
            tvMorePrivacyPolicy, tvMoreTermsConditions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvMoreEventBooking = (TextView) findViewById(R.id.tvMoreEventBooking);
        tvMoreContactUs = (TextView) findViewById(R.id.tvMoreContactUs);
        tvMoreAboutUs = (TextView) findViewById(R.id.tvMoreAboutUs);
        tvMorePrivacyPolicy = (TextView) findViewById(R.id.tvMorePrivacyPolicy);
        tvMoreTermsConditions = (TextView) findViewById(R.id.tvMoreTermsConditions);

        tvMoreContactUs.setOnClickListener(this);
        tvMoreAboutUs.setOnClickListener(this);
        tvMoreTermsConditions.setOnClickListener(this);
        tvMoreEventBooking.setOnClickListener(this);
        tvMorePrivacyPolicy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvMoreEventBooking:
                startActivity(new Intent(this, EventBooking.class));
                break;
            case R.id.tvMoreContactUs:
                startActivity(new Intent(this, ContactUs.class));
                break;
            case R.id.tvMoreAboutUs:
                startActivity(new Intent(this, AboutUs.class).putExtra("about", "about"));
                break;
            case R.id.tvMorePrivacyPolicy:
                startActivity(new Intent(this, AboutUs.class).putExtra("privacy", "privacy"));
                break;
            case R.id.tvMoreTermsConditions:
                startActivity(new Intent(this, AboutUs.class));
                break;
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
