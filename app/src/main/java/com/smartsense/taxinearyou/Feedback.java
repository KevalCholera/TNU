package com.smartsense.taxinearyou;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.smartsense.taxinearyou.utill.CommonUtil;

public class Feedback extends AppCompatActivity implements View.OnClickListener {

    RatingBar rbFeedbackRatingForDriver;
    TextView tvFeedbackRatingForDriver;
    CheckBox cbFeedbackCommentForDriver, cbFeedbackCommentForTaxinearu;
    EditText etFeedbackCommentForDriver, etFeedbackCommentForTaxinearu;
    ImageView ivFeedbackhappy, ivFeedbacksad;
    Button btFeedBackSubmit;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rbFeedbackRatingForDriver = (RatingBar) findViewById(R.id.rbFeedbackRatingForDriver);
        tvFeedbackRatingForDriver = (TextView) findViewById(R.id.tvFeedbackRatingForDriver);
        cbFeedbackCommentForDriver = (CheckBox) findViewById(R.id.cbFeedbackCommentForDriver);
        cbFeedbackCommentForTaxinearu = (CheckBox) findViewById(R.id.cbFeedbackCommentForTaxinearu);
        etFeedbackCommentForDriver = (EditText) findViewById(R.id.etFeedbackCommentForDriver);
        etFeedbackCommentForTaxinearu = (EditText) findViewById(R.id.etFeedbackCommentForTaxinearu);
        ivFeedbackhappy = (ImageView) findViewById(R.id.ivFeedbackhappy);
        ivFeedbacksad = (ImageView) findViewById(R.id.ivFeedbacksad);
        btFeedBackSubmit = (Button) findViewById(R.id.btFeedBackSubmit);

        rbFeedbackRatingForDriver.getProgressDrawable().setColorFilter(ContextCompat.getColor(this, R.color.Yellow), PorterDuff.Mode.SRC_ATOP);


        cbFeedbackCommentForDriver.setOnClickListener(this);
        cbFeedbackCommentForTaxinearu.setOnClickListener(this);
        ivFeedbacksad.setOnClickListener(this);
        btFeedBackSubmit.setOnClickListener(this);
        ivFeedbackhappy.setOnClickListener(this);

        rbFeedbackRatingForDriver.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                String ratedValue = String.valueOf(ratingBar.getRating());
                tvFeedbackRatingForDriver.setText(ratedValue + "/5");

            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cbFeedbackCommentForDriver:
                if (cbFeedbackCommentForDriver.isChecked())
                    etFeedbackCommentForDriver.setVisibility(View.VISIBLE);
                else {
                    etFeedbackCommentForDriver.setVisibility(View.GONE);
                    etFeedbackCommentForDriver.setText("");
                    CommonUtil.closeKeyboard(this);
                }

                break;
            case R.id.cbFeedbackCommentForTaxinearu:
                if (cbFeedbackCommentForTaxinearu.isChecked())
                    etFeedbackCommentForTaxinearu.setVisibility(View.VISIBLE);
                else {
                    etFeedbackCommentForTaxinearu.setVisibility(View.GONE);
                    etFeedbackCommentForTaxinearu.setText("");
                    CommonUtil.closeKeyboard(this);
                }
                break;

            case R.id.ivFeedbacksad:

                ivFeedbackhappy.setImageResource(R.mipmap.happy_grey_face);
                ivFeedbacksad.setImageResource(R.mipmap.sad_yellow_face);
                cbFeedbackCommentForTaxinearu.setVisibility(View.VISIBLE);
                break;
            case R.id.ivFeedbackhappy:

                ivFeedbacksad.setImageResource(R.mipmap.sad_grey_face);
                ivFeedbackhappy.setImageResource(R.mipmap.happy_yellow_face);
                etFeedbackCommentForTaxinearu.setVisibility(View.GONE);
                cbFeedbackCommentForTaxinearu.setVisibility(View.GONE);
                cbFeedbackCommentForTaxinearu.setChecked(false);
                etFeedbackCommentForTaxinearu.setText("");
                CommonUtil.closeKeyboard(this);
                break;

            case R.id.btFeedBackSubmit:
                openOccasionsPopup();
                break;

        }
    }

    public void openOccasionsPopup() {
        try {

            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialog = inflater.inflate(R.layout.dialog_all, null);
            LinearLayout lyPopUpFeedback = (LinearLayout) dialog.findViewById(R.id.lyPopUpFeedback);
            lyPopUpFeedback.setVisibility(View.VISIBLE);

            Button btPopupFeedbackOk;

            btPopupFeedbackOk = (Button) dialog.findViewById(R.id.btPopupFeedbackOk);

            btPopupFeedbackOk.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {

                    startActivity(new Intent(Feedback.this, Search.class));
                }
            });
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(true);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
