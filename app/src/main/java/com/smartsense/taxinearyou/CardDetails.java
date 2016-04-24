package com.smartsense.taxinearyou;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smartsense.taxinearyou.utill.CommonUtil;

public class CardDetails extends AppCompatActivity {

    TextView tvCardDetailsCardNumber;
    EditText etCardDetailsExpiryDate, etCardDetailsCSC, etCardDetailsBillingAddress;
    Button btCardDetailsSave;
    CoordinatorLayout clCardDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvCardDetailsCardNumber = (TextView) findViewById(R.id.tvCardDetailsCardNumber);
        btCardDetailsSave = (Button) findViewById(R.id.btCardDetailsSave);
        etCardDetailsExpiryDate = (EditText) findViewById(R.id.etCardDetailsExpiryDate);
        etCardDetailsCSC = (EditText) findViewById(R.id.etCardDetailsCSC);
        etCardDetailsBillingAddress = (EditText) findViewById(R.id.etCardDetailsBillingAddress);
        clCardDetails = (CoordinatorLayout) findViewById(R.id.clCardDetails);

        btCardDetailsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etCardDetailsExpiryDate.getText().toString()))
                    CommonUtil.showSnackBar(CardDetails.this, getString(R.string.enter_expiry), clCardDetails);
                else if (TextUtils.isEmpty(etCardDetailsCSC.getText().toString()))
                    CommonUtil.showSnackBar(CardDetails.this, getString(R.string.enter_csc), clCardDetails);
                else if (TextUtils.isEmpty(etCardDetailsBillingAddress.getText().toString()))
                    CommonUtil.showSnackBar(CardDetails.this, getString(R.string.enter_billing_add), clCardDetails);
                else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            CardDetails.this);

                    alertDialogBuilder.setTitle("Saved!!!");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("You have been Saved the Card!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(CardDetails.this, CardList.class));
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ratingforsearch, menu);
        return false;
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
