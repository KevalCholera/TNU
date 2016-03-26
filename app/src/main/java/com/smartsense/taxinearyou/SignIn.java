package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.DataRequest;
import com.smartsense.taxinearyou.utill.JsonErrorShow;

import org.json.JSONException;
import org.json.JSONObject;

public class SignIn extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

    Button btSignInSignIn, btSignInSignUp;
    EditText etSignInUserName, etSignInPassword;
    TextView tvSignInForgotPassword, tvSignInForgotEmail;
    CoordinatorLayout rlSignInMain;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btSignInSignIn = (Button) findViewById(R.id.btSignInSignIn);
        btSignInSignUp = (Button) findViewById(R.id.btSignInSignUp);
        etSignInUserName = (EditText) findViewById(R.id.etSignInUserName);
        etSignInPassword = (EditText) findViewById(R.id.etSignInPassword);
        tvSignInForgotPassword = (TextView) findViewById(R.id.tvSignInForgotPassword);
        rlSignInMain = (CoordinatorLayout) findViewById(R.id.rlSignInMain);
        tvSignInForgotEmail = (TextView) findViewById(R.id.tvSignInForgotEmail);

        btSignInSignIn.setOnClickListener(this);
        btSignInSignUp.setOnClickListener(this);
        tvSignInForgotPassword.setOnClickListener(this);
        tvSignInForgotEmail.setOnClickListener(this);

    }

    // validating email id
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menusignin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btSignInSignIn:

                if (TextUtils.isEmpty(etSignInUserName.getText().toString()))
                    CommonUtil.showSnackBar(SignIn.this, getString(R.string.enter_email_id), rlSignInMain);
                else if (!CommonUtil.isValidEmail(etSignInUserName.getText().toString()))
                    CommonUtil.showSnackBar(SignIn.this, getString(R.string.enter_valid_email_id), rlSignInMain);
                else if (TextUtils.isEmpty(etSignInPassword.getText().toString()))
                    CommonUtil.showSnackBar(SignIn.this, getString(R.string.enter_pass), rlSignInMain);
                else if (etSignInPassword.length() < 7 || etSignInPassword.length() > 15)
                    CommonUtil.showSnackBar(SignIn.this, getString(R.string.enter_valid_pass), rlSignInMain);
                else {
//                    startActivity(new Intent(SignIn.this, Search.class));
//                    null_all();
                    doLogin();
                }
                break;
            case R.id.btSignInSignUp:
                startActivity(new Intent(SignIn.this, SignUp.class));
                null_all();
                break;
            case R.id.tvSignInForgotPassword:
                startActivity(new Intent(SignIn.this, ResetPassword.class));
                null_all();
                break;
            case R.id.tvSignInForgotEmail:
                startActivity(new Intent(SignIn.this, RecoverEmailAddress.class));
                null_all();
                break;
        }
    }

    public void null_all() {
        etSignInUserName.setText("");
        etSignInPassword.setText("");
    }

    private void doLogin() {
        final String tag = "login";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.EVENT_LOGIN + "&json="
                    + jsonData.put("password", etSignInPassword.getText().toString().trim()).put("username", etSignInUserName.getText().toString().trim()).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.showProgressDialog(this, "Login...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(this, "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
//                    switch (response.getInt("__eventid")) {
//                        case Constants.Events.EVENT_LOGIN:
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_ACCESS_TOKEN, response.optJSONObject("json").optString("token"));
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.optJSONObject("json").optJSONObject("user").optString("userId"));
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_FULLNAME, response.optJSONObject("json").optJSONObject("user").optString("firstName") + " " + response.optJSONObject("json").getJSONObject("user").getString("lastName"));
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_EMAIL, response.optJSONObject("json").optJSONObject("user").optString("primaryEmailId"));
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_MNO, response.optJSONObject("json").optJSONObject("user").optString("mobileNo"));
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_PROIMG, response.optJSONObject("json").optJSONObject("user").optString("profilePic"));
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_STATUS, response.optJSONObject("json").optJSONObject("user").optString("isActivated"));
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_INFO, response.optJSONObject("json").optJSONObject("user").toString());
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_LUGGAGE, response.optJSONObject("json").optJSONArray("luggageArray").toString());
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_PASSENGER, response.optJSONObject("json").optJSONArray("passengerArray").toString());
                    SharedPreferenceUtil.save();
                    startActivity(new Intent(this, Search.class));
                    finish();
//                            break;
//                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, this, rlSignInMain);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
