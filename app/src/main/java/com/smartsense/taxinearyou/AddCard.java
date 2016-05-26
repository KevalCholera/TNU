package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.smartsense.taxinearyou.utill.CommonUtil;

public class AddCard extends AppCompatActivity implements View.OnClickListener {

    EditText etAddCardCardHolderName, etAddCardCardType, etAddCardCardNumber, etAddCardExpiryDateMonth, etAddCardExpiryDateYear,
            etAddCardSecurityNumber, etAddCardCardFirstName, etAddCardCardLastName, etAddCardCardAddress1, etAddCardCardAddress2,
            etAddCardTown, etAddCardCity, etAddCardCardPostCode;
    Button btAddCardSubmit;
    CheckBox cbAddCardFutureUse;
    CoordinatorLayout clAddCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etAddCardCardHolderName = (EditText) findViewById(R.id.etAddCardCardHolderName);
        etAddCardCardType = (EditText) findViewById(R.id.etAddCardCardType);
        etAddCardCardNumber = (EditText) findViewById(R.id.etAddCardCardNumber);
        etAddCardExpiryDateMonth = (EditText) findViewById(R.id.etAddCardExpiryDateMonth);
        etAddCardExpiryDateYear = (EditText) findViewById(R.id.etAddCardExpiryDateYear);
        etAddCardSecurityNumber = (EditText) findViewById(R.id.etAddCardSecurityNumber);
        etAddCardCardFirstName = (EditText) findViewById(R.id.etAddCardCardFirstName);
        etAddCardCardLastName = (EditText) findViewById(R.id.etAddCardCardLastName);
        etAddCardCardAddress1 = (EditText) findViewById(R.id.etAddCardCardAddress1);
        etAddCardCardAddress2 = (EditText) findViewById(R.id.etAddCardCardAddress2);
        etAddCardTown = (EditText) findViewById(R.id.etAddCardTown);
        etAddCardCity = (EditText) findViewById(R.id.etAddCardCity);
        etAddCardCardPostCode = (EditText) findViewById(R.id.etAddCardCardPostCode);
        btAddCardSubmit = (Button) findViewById(R.id.btAddCardSubmit);
        cbAddCardFutureUse = (CheckBox) findViewById(R.id.cbAddCardFutureUse);
        clAddCard = (CoordinatorLayout) findViewById(R.id.clAddCard);

        btAddCardSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btAddCardSubmit:
                if (TextUtils.isEmpty(etAddCardCardHolderName.getText().toString()))
                    CommonUtil.showSnackBar(AddCard.this, getString(R.string.enter_card_holder_name), clAddCard);
                else if (TextUtils.isEmpty(etAddCardCardType.getText().toString()))
                    CommonUtil.showSnackBar(AddCard.this, getString(R.string.enter_card_type), clAddCard);
                else if (TextUtils.isEmpty(etAddCardCardNumber.getText().toString()))
                    CommonUtil.showSnackBar(AddCard.this, getString(R.string.enter_card_no), clAddCard);
                else if (TextUtils.isEmpty(etAddCardSecurityNumber.getText().toString()))
                    CommonUtil.showSnackBar(AddCard.this, getString(R.string.enter_security_no), clAddCard);
                else if (TextUtils.isEmpty(etAddCardCardFirstName.getText().toString()))
                    CommonUtil.showSnackBar(AddCard.this, getString(R.string.enter_first_name), clAddCard);
                else if (TextUtils.isEmpty(etAddCardCardLastName.getText().toString()))
                    CommonUtil.showSnackBar(AddCard.this, getString(R.string.enter_last_name), clAddCard);
                else if (TextUtils.isEmpty(etAddCardCardAddress1.getText().toString()))
                    CommonUtil.showSnackBar(AddCard.this, getString(R.string.enter_add), clAddCard);
                else if (TextUtils.isEmpty(etAddCardCardAddress2.getText().toString()))
                    CommonUtil.showSnackBar(AddCard.this, getString(R.string.enter_add), clAddCard);
                else if (TextUtils.isEmpty(etAddCardTown.getText().toString()))
                    CommonUtil.showSnackBar(AddCard.this, getString(R.string.enter_town), clAddCard);
                else if (TextUtils.isEmpty(etAddCardCity.getText().toString()))
                    CommonUtil.showSnackBar(AddCard.this, getString(R.string.enter_city), clAddCard);
                else if (TextUtils.isEmpty(etAddCardCardPostCode.getText().toString()))
                    CommonUtil.showSnackBar(AddCard.this, getString(R.string.enter_postcode), clAddCard);
                else
                    startActivity(new Intent(AddCard.this, Card.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.ratingforsearch, menu);
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
