package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.DataRequest;
import com.smartsense.taxinearyou.utill.JsonErrorShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SecurityQuestion extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    EditText etSecurityQuestion1, etSecurityAnswer1, etSecurityQuestion2, etSecurityAnswer2;
    CheckBox cbSecurityFromOrganization, cbSecurityFromPrivacyPolicy, cbSecurityAboutTaxinearu;
    Button btSecuritySave;
    TextView tvSecurityTermsConditions;
    CoordinatorLayout clSecurityQuestion;
    ImageButton btSecurityBack;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);

        etSecurityQuestion1 = (EditText) findViewById(R.id.etSecurityQuestion1);
        etSecurityAnswer1 = (EditText) findViewById(R.id.etSecurityAnswer1);
        etSecurityQuestion2 = (EditText) findViewById(R.id.etSecurityQuestion2);
        etSecurityAnswer2 = (EditText) findViewById(R.id.etSecurityAnswer2);
        cbSecurityFromOrganization = (CheckBox) findViewById(R.id.cbSecurityFromOrganization);
        cbSecurityFromPrivacyPolicy = (CheckBox) findViewById(R.id.cbSecurityFromPrivacyPolicy);
        cbSecurityAboutTaxinearu = (CheckBox) findViewById(R.id.cbSecurityAboutTaxinearu);
        btSecuritySave = (Button) findViewById(R.id.btSecuritySave);
        tvSecurityTermsConditions = (TextView) findViewById(R.id.tvSecurityTermsConditions);
        clSecurityQuestion = (CoordinatorLayout) findViewById(R.id.clSecurityQuestion);
        btSecurityBack = (ImageButton) findViewById(R.id.btSecurityBack);
        etSecurityQuestion1.setOnClickListener(this);
        etSecurityQuestion1.setText("In which city or town did your mother and father meet ?");
        etSecurityQuestion1.setTag("1");
        etSecurityQuestion2.setText("In which city or town did your mother and father meet ?");
        etSecurityQuestion2.setTag("1");
        etSecurityQuestion2.setOnClickListener(this);
        btSecuritySave.setOnClickListener(this);
        btSecurityBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSecuritySave:
                if (TextUtils.isEmpty(etSecurityAnswer1.getText().toString()) || TextUtils.isEmpty(etSecurityAnswer2.getText().toString()))
                    CommonUtil.showSnackBar(SecurityQuestion.this, "Please Answer for Security Question", clSecurityQuestion);
                else if (!cbSecurityFromPrivacyPolicy.isChecked())
                    CommonUtil.showSnackBar(SecurityQuestion.this, "Please Agree to Terms and Conditions ", clSecurityQuestion);
                else
                    doSignUp();
//                    startActivity(new Intent(SecurityQuestion.this, SignIn.class));
                break;
            case R.id.btSecurityBack:
                finish();
                break;
            case R.id.etSecurityQuestion1:
                openQuestionSelectPopup(true);
                break;
            case R.id.etSecurityQuestion2:
                openQuestionSelectPopup(false);
                break;
        }
    }

    private void doSignUp() {
        final String tag = "doSignUp";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.EVENT_SIGNUP + "&json="
                    + jsonData.put("firstName", getIntent().getStringExtra("firstName")).put("lastName", getIntent().getStringExtra("lastName")).put("contactNo", getIntent().getStringExtra("contactNo")).put("emailId", getIntent().getStringExtra("emailId")).put("password", getIntent().getStringExtra("password")).put("confPassword", getIntent().getStringExtra("confPassword")).put("altEmailId", getIntent().getStringExtra("altEmailId")).put("secQ1", (String) etSecurityQuestion1.getTag()).put("secQ2", (String) etSecurityQuestion2.getTag()).put("ans1", etSecurityAnswer1.getText().toString().trim()).put("ans2", etSecurityAnswer2.getText().toString().trim()).put("termsCond", cbSecurityFromPrivacyPolicy.isChecked() ? "1" : "0").put("condition2", cbSecurityFromOrganization.isChecked() ? "1" : "0").put("condition3", cbSecurityAboutTaxinearu.isChecked() ? "1" : "0").toString());
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
                    switch (response.getInt("__eventid")) {
                        case Constants.Events.EVENT_SIGNUP:
                            AlertDialog.Builder alert = new AlertDialog.Builder(this);
//                            alert.setTitle("Empty Cart?");
                            alert.setMessage(response.optString("msg"));
                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //Do something here where "ok" clicked
                                    startActivity(new Intent(SecurityQuestion.this, SignIn.class));
                                    finish();
                                }
                            });
                            alert.show();
// SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONObject("data").getString("userId"));
//                            SharedPreferenceUtil.save();
//                            startActivity(new Intent(this, OTPActivity.class).putExtra("mobile", etMobileNo.getText().toString()).putExtra("code", etCountryCode.getText().toString()).putExtra("tag", (String) etCountryCode.getTag()));
//                            finish();
                            break;
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, this, clSecurityQuestion);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void openQuestionSelectPopup(Boolean check) {
        try {
            String str = "    { \"eventId\" : 123 ,   \"errorCode\" : 0,   \"status\" : 200,   \"message\" : \"country list.\",   \"data\" : { \"countries\" : [ { \"id\" : \"1\",   \"name\" : \"In which city or town did your mother and father meet ?\",   \"code\" : \"+92\" } , { \"id\" : \"2\",   \"name\" : \"What was the last name of your third grade teacher ?\" ,   \"code\" : \"+93\" }, { \"id\" : \"3\",   \"name\" : \"In Which city or town was your first job ?\" ,   \"code\" : \"+93\" }, { \"id\" : \"4\",   \"name\" : \"what was the name of your first stuffed animal ?\" ,   \"code\" : \"+93\" } ]  } }";
            JSONObject jsonObject = new JSONObject(str);
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_select_question, null);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);
            AdapterClass adapterClass = new AdapterClass(this, jsonObject.optJSONObject("data").optJSONArray("countries"), check);
            list_view.setAdapter(adapterClass);
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(true);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class AdapterClass extends BaseAdapter {
        private final Boolean check;
        private JSONArray data;
        private LayoutInflater inflater = null;
        Activity a;

        public AdapterClass(Activity a, JSONArray data, Boolean check) {
            this.data = data;
            this.a = a;
            this.check = check;
            inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return data.length();
        }


        public Object getItem(int position) {
            return data.optJSONObject(position);
        }


        public long getItemId(int position) {
            return position;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.element_select_question, null);
            final TextView cbElementClassName = (TextView) vi.findViewById(R.id.tvElementQuestionName);
            JSONObject object = data.optJSONObject(position);
            cbElementClassName.setText(object.optString("name"));
            cbElementClassName.setTag(object.optString("id"));
            cbElementClassName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (check) {
                        etSecurityQuestion1.setText(cbElementClassName.getText().toString());
                        etSecurityQuestion1.setTag((String) cbElementClassName.getTag());
                    } else {
                        etSecurityQuestion2.setText(cbElementClassName.getText().toString());
                        etSecurityQuestion2.setTag((String) cbElementClassName.getTag());
                    }
                    alert.dismiss();
                }
            });
            return vi;
        }
    }
}
