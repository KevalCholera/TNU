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
    private JSONObject jsonObject;

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
        etSecurityQuestion2.setOnClickListener(this);
        btSecuritySave.setOnClickListener(this);
        btSecurityBack.setOnClickListener(this);
        getSecurityQuestion();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSecuritySave:
                if (TextUtils.isEmpty(etSecurityAnswer1.getText().toString()) || TextUtils.isEmpty(etSecurityAnswer2.getText().toString()))
                    CommonUtil.showSnackBar(SecurityQuestion.this, getResources().getString(R.string.enter_fields_below), clSecurityQuestion);
                else if (!cbSecurityFromPrivacyPolicy.isChecked())
                    CommonUtil.showSnackBar(SecurityQuestion.this, getResources().getString(R.string.terms_conditions_error), clSecurityQuestion);
                else
                    doSignUp();
                break;
            case R.id.btSecurityBack:
                finish();
                CommonUtil.closeKeyboard(this);
                break;
            case R.id.etSecurityQuestion1:
                if (jsonObject != null) {
                    openQuestionSelectPopup(true, jsonObject);
                } else
                    getSecurityQuestion();
                break;
            case R.id.etSecurityQuestion2:
                if (jsonObject != null) {
                    openQuestionSelectPopup(false, jsonObject);
                } else
                    getSecurityQuestion();
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

    private void getSecurityQuestion() {
        final String tag = "getSecurityQuestion";
        StringBuilder builder = new StringBuilder();

        builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.EVENT_GET_SECURITY_QES);

//        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.errorToastShowing(this);
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
                        case Constants.Events.EVENT_GET_SECURITY_QES:
                            if (response.optJSONObject("json").has("questionList")) {
                                for (int i = 0; i < response.optJSONObject("json").optJSONArray("questionList").length(); i++) {
                                    if (i == 0) {
                                        etSecurityQuestion1.setText(response.optJSONObject("json").optJSONArray("questionList").optJSONObject(i).optString("name"));
                                        etSecurityQuestion1.setTag(response.optJSONObject("json").optJSONArray("questionList").optJSONObject(i).optString("id"));
                                    }
                                    if (i == response.optJSONObject("json").optJSONArray("questionList").length() / 2) {
                                        etSecurityQuestion2.setText(response.optJSONObject("json").optJSONArray("questionList").optJSONObject(i).optString("name"));
                                        etSecurityQuestion2.setTag(response.optJSONObject("json").optJSONArray("questionList").optJSONObject(i).optString("id"));
                                        break;
                                    }
                                }
                            }
                            jsonObject = response;
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

    public void openQuestionSelectPopup(Boolean check, JSONObject jsonObject1) {
        try {
//            String str = "    { \"eventId\" : 123 ,   \"errorCode\" : 0,   \"status\" : 200,   \"message\" : \"country list.\",   \"data\" : { \"countries\" : [ { \"id\" : \"1\",   \"name\" : \"In which city or town did your mother and father meet ?\",   \"code\" : \"+92\" } , { \"id\" : \"2\",   \"name\" : \"What was the last name of your third grade teacher ?\" ,   \"code\" : \"+93\" }, { \"id\" : \"3\",   \"name\" : \"In Which city or town was your first job ?\" ,   \"code\" : \"+93\" }, { \"id\" : \"4\",   \"name\" : \"what was the name of your first stuffed animal ?\" ,   \"code\" : \"+93\" } ]  } }";
            jsonObject = jsonObject1;
            JSONArray question1 = new JSONArray();
            JSONArray question2 = new JSONArray();
            for (int i = 0; i < jsonObject.optJSONObject("json").optJSONArray("questionList").length(); i++) {
                if (i < jsonObject.optJSONObject("json").optJSONArray("questionList").length() / 2) {
                    question1.put(jsonObject.optJSONObject("json").optJSONArray("questionList").optJSONObject(i));
                } else {
                    question2.put(jsonObject.optJSONObject("json").optJSONArray("questionList").optJSONObject(i));
                }
            }
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_select_question, null);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);
            AdapterQuestionSelection adapterQuestionSelection = new AdapterQuestionSelection(this, question1, question2, check);
            list_view.setAdapter(adapterQuestionSelection);
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(true);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class AdapterQuestionSelection extends BaseAdapter {
        private final Boolean check;
        private JSONArray data;
        private LayoutInflater inflater = null;
        Activity a;

        public AdapterQuestionSelection(Activity a, JSONArray data, JSONArray data2, Boolean check) {
            this.check = check;
            if (check)
                this.data = data;
            else
                this.data = data2;
            this.a = a;
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
