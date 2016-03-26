package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.smartsense.taxinearyou.utill.LocationSettingsHelper;


public class ActivitySplash extends AppCompatActivity {
    private LocationSettingsHelper mSettingsHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub


        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                window.setStatusBarColor(getResources().getColor(
//                        R.color.red1));
            }
            View decorView = getWindow().getDecorView();
// Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

            decorView.setSystemUiVisibility(uiOptions);

//            getSupportActionBar().hide();


        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startApp();
    }


    public void startApp() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
//                    if (CommonUtil.isInternet(SplashActivity.this)) {
//                        if (SharedPreferenceUtil.contains(Constants.PrefKeys.PREF_ACCESS_TOKEN))
//                            if (SharedPreferenceUtil.contains(Constants.PrefKeys.PREF_AREA_PIN_CODE))
//                                startActivity(new Intent(getBaseContext(), GifkarActivity.class));
//                            else
//                                startActivity(new Intent(getBaseContext(), CitySelectActivity.class));
//                        else {
//                            if (SharedPreferenceUtil.contains(Constants.PrefKeys.PREF_AREA_PIN_CODE))
//                                startActivity(new Intent(getBaseContext(), GifkarActivity.class));
//                            else
//                                startActivity(new Intent(getBaseContext(), StartActivity.class));
//                        }
//                    } else
//                    startActivity(new Intent(getBaseContext(), NoInternetConnection.class));
                    startActivity(new Intent(getBaseContext(), SignIn.class));
                    finish();

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LocationSettingsHelper.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
//				Log.i("TAG", "User agreed to make required location settings changes.");
                        startApp();
                        break;
                    case Activity.RESULT_CANCELED:
//				Log.i("TAG", "User chose not to make required location settings changes.");
                        finish();
                        break;
                    default:
                        finish();
                        break;
                }
                break;

        }
    }
}