package com.smartsense.taxinearyou;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.smartsense.taxinearyou.utill.Constants;

public class AboutUs extends AppCompatActivity {

    LinearLayout lyAboutUs;
    WebView wvPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lyAboutUs = (LinearLayout) findViewById(R.id.lyAboutUs);
        wvPrivacyPolicy = (WebView) findViewById(R.id.wvPrivacyPolicy);
        wvPrivacyPolicy.setVisibility(View.VISIBLE);

        wvPrivacyPolicy.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                setTitle(getResources().getString(R.string.get_data));
                setProgress(progress * 100);
                if (progress == 100)
                    setTitle(R.string.app_name);
            }
        });
        if (getIntent() != null && getIntent().hasExtra("about") && getIntent().getStringExtra("about").equalsIgnoreCase("about")) {
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//            params.setMargins(30, 30, 30, 30);
//            lyAboutUs.setLayoutParams(params);
            getSupportActionBar().setTitle("About Us");
            wvPrivacyPolicy.loadUrl(Constants.BASE_URL_IMAGE_POSTFIX + Constants.BASE_ABOUT_US);
//            lyAboutUs.setVisibility(View.VISIBLE);

        } else if (getIntent() != null && getIntent().hasExtra("privacy") && getIntent().getStringExtra("privacy").equalsIgnoreCase("privacy")) {
            getSupportActionBar().setTitle("Privacy Policy");

            wvPrivacyPolicy.loadUrl(Constants.BASE_URL_IMAGE_POSTFIX + Constants.BASE_PRIVACY_POLICY);

        } else {
            getSupportActionBar().setTitle("Terms & Conditions");



            wvPrivacyPolicy.loadUrl(Constants.BASE_URL_IMAGE_POSTFIX + Constants.BASE_TERMS_AND_CONDITION);
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
