package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.smartsense.taxinearyou.utill.CommonUtil;

public class PaymentDetails extends AppCompatActivity implements View.OnClickListener {

    TextView tvPaymentTNUCredit, tvPaymentCard, tvPaymentCash, tvPaymentAmount;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvPaymentTNUCredit = (TextView) findViewById(R.id.tvPaymentTNUCredit);
        tvPaymentCard = (TextView) findViewById(R.id.tvPaymentCard);
        tvPaymentCash = (TextView) findViewById(R.id.tvPaymentCash);
        tvPaymentAmount = (TextView) findViewById(R.id.tvPaymentAmount);

        tvPaymentAmount.setText((char) 0x00A3 + "406.00");

        tvPaymentCash.setOnClickListener(this);
        tvPaymentCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPaymentCash:
                CommonUtil.openDialogs(PaymentDetails.this, "Payment Details", R.id.lyPopupBookSuccess, R.id.btPopupBookSuccessOk);
                break;
            case R.id.tvPaymentCard:
                startActivity(new Intent(PaymentDetails.this, Card.class));
                break;
        }

    }
}
