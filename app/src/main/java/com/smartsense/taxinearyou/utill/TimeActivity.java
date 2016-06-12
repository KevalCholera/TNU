package com.smartsense.taxinearyou.utill;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.smartsense.taxinearyou.Adapters.AdapterCard;
import com.smartsense.taxinearyou.AddCard;
import com.smartsense.taxinearyou.Fragments.FragmentBook;
import com.smartsense.taxinearyou.PaymentDetails;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.Search;

import org.json.JSONArray;
import org.json.JSONException;

public abstract class TimeActivity extends AppCompatActivity {

    Toolbar toolbarAll;
    TextView toolbarText;

    CountDownTimer countDownTimer = null;


    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        configureToolbar();

    }

    protected abstract int getLayoutResource();

    private void configureToolbar() {
        toolbarAll = (Toolbar) findViewById(R.id.toolbarAll);
        toolbarText = (TextView) findViewById(R.id.toolbarText);
        setSupportActionBar(toolbarAll);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        countDownTimer = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (countDownTimer == null)
            countDownStart(FragmentBook.timeRemaining);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menutime, menu);
//        actionTime = menu.findItem(R.id.action_time);

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

    public void countDownStart(long timeRemaining1) {
        countDownTimer = new CountDownTimer(timeRemaining1, 1000) {
            public void onTick(long millisUntilFinished) {
                FragmentBook.timeRemaining = millisUntilFinished;
//                long minutes = (millisUntilFinished % 3600) / 60;
//                long seconds = millisUntilFinished % 60;
                long seconds = (millisUntilFinished / 1000) % 60;

                long minutes = ((millisUntilFinished - seconds) / 1000) / 60;
                if (toolbarText != null)
                    toolbarText.setText(String.format("%02d:%02d", minutes, seconds));
//                tvOtpCountDown.setText(String.format("%02d:%02d", minutes, seconds));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(TimeActivity.this);
                builder.setCancelable(false);
                builder.setMessage(TimeActivity.this.getResources().getString(R.string.session_expire1));
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(TimeActivity.this, Search.class));
                        finish();
                    }
                });
                builder.create();
                builder.show();
            }

        }.start();
    }




}
