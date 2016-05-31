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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.DataRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Feedback extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    RatingBar rbFeedbackRatingForDriver;
    TextView tvFeedbackRatingForDriver;
    CheckBox cbFeedbackCommentForDriver, cbFeedbackCommentForTaxinearu;
    EditText etFeedbackCommentForDriver, etFeedbackCommentForTaxinearu;
    RadioButton ivFeedbackhappy, ivFeedbacksad;
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
        ivFeedbackhappy = (RadioButton) findViewById(R.id.ivFeedbackhappy);
        ivFeedbacksad = (RadioButton) findViewById(R.id.ivFeedbacksad);
        btFeedBackSubmit = (Button) findViewById(R.id.btFeedBackSubmit);

//        rbFeedbackRatingForDriver.getProgressDrawable().setColorFilter(ContextCompat.getColor(this, R.color.Yellow), PorterDuff.Mode.SRC_ATOP);

        cbFeedbackCommentForDriver.setOnClickListener(this);
        cbFeedbackCommentForTaxinearu.setOnClickListener(this);
        ivFeedbacksad.setOnClickListener(this);
        btFeedBackSubmit.setOnClickListener(this);
        ivFeedbackhappy.setOnClickListener(this);

        rbFeedbackRatingForDriver.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
//                rbFeedbackRatingForDriver.getProgressDrawable().setColorFilter(ContextCompat.getColor(Feedback.this, R.color.Yellow), PorterDuff.Mode.SRC_ATOP);
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
                cbFeedbackCommentForTaxinearu.setVisibility(View.VISIBLE);
                break;

            case R.id.ivFeedbackhappy:
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

    private void feedBack() {
        final String tag = "Feed Back";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.FEED_BACK
                    + "&json=").append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("rideId", getIntent().getStringExtra("rideId"))
                    .put("description", etFeedbackCommentForDriver.getText().toString())
                    .put("rating", rbFeedbackRatingForDriver.getRating() + "")
                    .put("orgDescription", etFeedbackCommentForTaxinearu.getText().toString())
                    .put("orgRating", ivFeedbackhappy.isChecked() ? 1 : 0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.showProgressDialog(this, "getting data...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.errorToastShowing(this);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        CommonUtil.cancelProgressDialog();
        if (jsonObject.length() > 0 && jsonObject.optInt("status") == Constants.STATUS_SUCCESS && jsonObject.optJSONObject("json").optJSONArray("lostItemInfoArray").length() > 0)
            CommonUtil.successToastShowing(this, jsonObject);
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
                    feedBack();
                    finish();
                }
            });
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(false);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
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
