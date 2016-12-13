package com.smartsense.taxinearyou;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactUs extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    EditText etContactUsName, etContactUsMobile, etContactUsEmail, etContactUsMessage, etContactUsSubject;
    Button btContactSubmit;
    CoordinatorLayout clContactUs;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etContactUsName = (EditText) findViewById(R.id.etContactUsName);
        etContactUsMobile = (EditText) findViewById(R.id.etContactUsMobile);
        etContactUsSubject = (EditText) findViewById(R.id.etContactUsSubject);
        etContactUsEmail = (EditText) findViewById(R.id.etContactUsEmail);
        etContactUsMessage = (EditText) findViewById(R.id.etContactUsMessage);
        btContactSubmit = (Button) findViewById(R.id.btContactSubmit);
        clContactUs = (CoordinatorLayout) findViewById(R.id.clContactUs);

    }

    public void submitContact(View view) {
        if (TextUtils.isEmpty(etContactUsName.getText().toString()) && TextUtils.isEmpty(etContactUsMobile.getText().toString()) && TextUtils.isEmpty(etContactUsEmail.getText().toString()) && TextUtils.isEmpty(etContactUsEmail.getText().toString()) && TextUtils.isEmpty(etContactUsSubject.getText().toString()) && TextUtils.isEmpty(etContactUsMessage.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.enter_fields_below), clContactUs);
        else if (TextUtils.isEmpty(etContactUsName.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.enter_first_name), clContactUs);
        else if (etContactUsName.getText().toString().length() < 5 || etContactUsName.getText().toString().length() > 20)
            CommonUtil.showSnackBar(getResources().getString(R.string.enter_name_validation_in_contact_us), clContactUs);
        else if (etContactUsMobile.length() != 10)
            CommonUtil.showSnackBar(getResources().getString(R.string.enter_con), clContactUs);
        else if (!CommonUtil.isValidEmail(etContactUsEmail.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.enter_email_id), clContactUs);
        else if (TextUtils.isEmpty(etContactUsSubject.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.enter_sub), clContactUs);
        else if (TextUtils.isEmpty(etContactUsMessage.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.enter_message), clContactUs);
        else
            contactUs();
    }

    private void contactUs() {
        final String tag = "Contact Us";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("mobileNo", etContactUsMobile.getText().toString().trim())
                    .put("subject", etContactUsSubject.getText().toString().trim())
                    .put("message", etContactUsMessage.getText().toString().trim())
                    .put("emailId", etContactUsEmail.getText().toString().trim())
                    .put("name", etContactUsName.getText().toString().trim()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.CONTACT_US), tag, this, this);
    }

    public void alertBox(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(msg);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create();
        builder.show();
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

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.cancelProgressDialog();
        CommonUtil.errorToastShowing(this);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {

        CommonUtil.cancelProgressDialog();
        if (jsonObject != null)
            if (jsonObject.optInt("status") == Constants.STATUS_SUCCESS) {
//                if (!jsonObject.optJSONObject("json").optString("isAvailable").equalsIgnoreCase("1"))
//                    CommonUtil.showSnackBar(jsonObject.optString("msg"), clContactUs);
//                else
                if (jsonObject.optString("__eventid").equalsIgnoreCase(Constants.Events.CONTACT_US + ""))
                    openDialog();//alertBox(jsonObject.optString("msg"));
            } else
                CommonUtil.conditionAuthentication(this, jsonObject);
        else
            CommonUtil.jsonNullError(this);
    }

    public void openDialog() {

        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View dialog = inflater.inflate(R.layout.dialog_all, null);
            LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.lyPopUpYourDetail);
            linearLayout.setVisibility(View.VISIBLE);
            TextView tvPopupLocatedEmail = (TextView) dialog.findViewById(R.id.tvPopupYourDetail);
            tvPopupLocatedEmail.setText("We aim to reply all enquiries within 24/48 hours of receiving your email. We will get back to you as soon as we can.");
            Button button1 = (Button) dialog.findViewById(R.id.btPopupYourDetailOk);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    finish();
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
}
