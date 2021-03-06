package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.qqtheme.framework.picker.OptionPicker;

public class SecurityQuestion extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    EditText etSecurityQuestion1, etSecurityAnswer1, etSecurityQuestion2, etSecurityAnswer2;
    CheckBox cbSecurityFromOrganization, cbSecurityFromPrivacyPolicy, cbSecurityAboutTaxinearu;
    Button btSecuritySave;
    TextView tvSecurityTermsConditions, tvSecurityPrivacyPolicy;
    CoordinatorLayout clSecurityQuestion;
    ImageView btSecurityBack;
    private AlertDialog alert;
    private JSONObject jsonObject;
    HashMap<String, String> spinnerMap = new HashMap<>();

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
        tvSecurityPrivacyPolicy = (TextView) findViewById(R.id.tvSecurityPrivacyPolicy);
        clSecurityQuestion = (CoordinatorLayout) findViewById(R.id.clSecurityQuestion);
        btSecurityBack = (ImageView) findViewById(R.id.btSecurityBack);

        etSecurityQuestion1.setOnClickListener(this);
        etSecurityQuestion2.setOnClickListener(this);
        btSecuritySave.setOnClickListener(this);
        btSecurityBack.setOnClickListener(this);
        tvSecurityPrivacyPolicy.setOnClickListener(this);
        tvSecurityTermsConditions.setOnClickListener(this);
        getSecurityQuestion();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSecuritySave:
                if (TextUtils.isEmpty(etSecurityAnswer1.getText().toString()) && TextUtils.isEmpty(etSecurityAnswer2.getText().toString()))
                    CommonUtil.showSnackBar(getResources().getString(R.string.enter_fields_below), clSecurityQuestion);
                else if (TextUtils.isEmpty(etSecurityAnswer1.getText().toString()))
                    CommonUtil.showSnackBar(getResources().getString(R.string.enter_ans_1), clSecurityQuestion);
                else if (TextUtils.isEmpty(etSecurityAnswer2.getText().toString()))
                    CommonUtil.showSnackBar(getResources().getString(R.string.enter_ans_2), clSecurityQuestion);
                else if (!cbSecurityFromPrivacyPolicy.isChecked())
                    CommonUtil.showSnackBar(getResources().getString(R.string.check_term), clSecurityQuestion);
                else {
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.RECEIVE_ORG_OFFERS, cbSecurityFromOrganization.isChecked() ? "1" : "0");
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.RECEIVE_TNU_OFFERS, cbSecurityAboutTaxinearu.isChecked() ? "1" : "0");
                    SharedPreferenceUtil.save();

                    CommonUtil.closeKeyboard(SecurityQuestion.this);
                    doSignUp();
                }
                break;
            case R.id.btSecurityBack:
                finish();
                CommonUtil.closeKeyboard(this);
                break;
            case R.id.etSecurityQuestion1:
                CommonUtil.closeKeyboard(this);
                if (jsonObject != null) {
                    selectPassenger(true, jsonObject);
                } else
                    getSecurityQuestion();
                break;
            case R.id.etSecurityQuestion2:
                CommonUtil.closeKeyboard(this);
                if (jsonObject != null) {
                    selectPassenger(false, jsonObject);
                } else
                    getSecurityQuestion();
                break;
            case R.id.tvSecurityTermsConditions:
                startActivity(new Intent(this, AboutUs.class));
                break;
            case R.id.tvSecurityPrivacyPolicy:
                startActivity(new Intent(this, AboutUs.class).putExtra("privacy", "privacy"));
                break;
        }
    }

    private void doSignUp() {
        final String tag = "doSignUp";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("firstName", getIntent().getStringExtra("firstName"))
                    .put("lastName", getIntent().getStringExtra("lastName"))
                    .put("contactNo", getIntent().getStringExtra("contactNo"))
                    .put("emailId", getIntent().getStringExtra("emailId"))
                    .put("password", getIntent().getStringExtra("password"))
                    .put("confPassword", getIntent().getStringExtra("confPassword"))
                    .put("altEmailId", getIntent().getStringExtra("altEmailId"))
                    .put("secQ1", etSecurityQuestion1.getTag())
                    .put("secQ2", etSecurityQuestion2.getTag())
                    .put("ans1", etSecurityAnswer1.getText().toString().trim())
                    .put("ans2", etSecurityAnswer2.getText().toString().trim())
                    .put("termsCond", cbSecurityFromPrivacyPolicy.isChecked() ? 1 : 0)
                    .put("condition2", cbSecurityFromOrganization.isChecked() ? 1 : 0)
                    .put("condition3", cbSecurityAboutTaxinearu.isChecked() ? 1 : 0).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.signing_up), CommonUtil.utf8Convert(builder, Constants.Events.EVENT_SIGNUP), tag, this, this);
    }

    private void getSecurityQuestion() {
        final String tag = "Get Security Question";
        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), (Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.EVENT_GET_SECURITY_QES), tag, this, this);
    }

    public void openQuestionSelectPopup(Boolean check, JSONObject jsonObject1) {
        try {
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
                        etSecurityQuestion1.setTag(cbElementClassName.getTag());
                    } else {
                        etSecurityQuestion2.setText(cbElementClassName.getText().toString());
                        etSecurityQuestion2.setTag(cbElementClassName.getTag());
                    }
                    alert.dismiss();
                }
            });
            return vi;
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.errorToastShowing(this);

        CommonUtil.cancelProgressDialog();
        getSecurityQuestion();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (response.getInt("__eventid")) {
                        case Constants.Events.EVENT_SIGNUP:
//                            AlertDialog.Builder alert = new AlertDialog.Builder(this);
//                            alert.setMessage(response.optString("msg"));
//                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    startActivity(new Intent(SecurityQuestion.this, SignIn.class));
//                                    finish();
//                                }
//                            });
//                            alert.show();
                            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View dialog = inflater.inflate(R.layout.dialog_all, null);
                            LinearLayout linearLayout;
                            linearLayout = (LinearLayout) dialog.findViewById(R.id.lyPopUpAfterRecovery);
                            Button button1;
                            TextView tvPopupLocatedEmail = (TextView) dialog.findViewById(R.id.tvPopupHead);
                            TextView tvPopupDetail = (TextView) dialog.findViewById(R.id.tvPopupDetail);
                            tvPopupLocatedEmail.setText(response.optJSONObject("json").optString("title"));
                            tvPopupDetail.setGravity(Gravity.LEFT);
                            tvPopupDetail.setText(response.optString("msg"));
                            button1 = (Button) dialog.findViewById(R.id.btPopupThankingRecoveryBack);
                            linearLayout.setVisibility(View.VISIBLE);
                            button1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alert.dismiss();
                                    startActivity(new Intent(SecurityQuestion.this, SignIn.class));
                                    finish();

                                }
                            });
                            alertDialogs.setView(dialog);
                            alertDialogs.setCancelable(false);
                            alert = alertDialogs.create();
                            alert.show();
//                            CommonUtil.alertBox(SecurityQuestion.this, response.optString("msg"));
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
                    CommonUtil.conditionAuthentication(this, response);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            CommonUtil.jsonNullError(this);
    }

    JSONArray question1;
    JSONArray question2;

    public void selectPassenger(final Boolean check, JSONObject jsonObject1) {
        jsonObject = jsonObject1;
        question1 = new JSONArray();
        question2 = new JSONArray();
        try {
            for (int i = 0; i < jsonObject.optJSONObject("json").optJSONArray("questionList").length(); i++) {
                if (i < jsonObject.optJSONObject("json").optJSONArray("questionList").length() / 2) {
                    question1.put(jsonObject.optJSONObject("json").optJSONArray("questionList").optJSONObject(i));
                } else {
                    question2.put(jsonObject.optJSONObject("json").optJSONArray("questionList").optJSONObject(i));
                }
            }
            String[] stringArr;
            if (check) {
                stringArr = new String[question1.length()];
                for (int i = 0; i < question1.length(); i++) {
                    stringArr[i] = question1.optJSONObject(i).optString("name");
                    spinnerMap.put(question1.optJSONObject(i).optString("name"), question1.optJSONObject(i).optString("id"));
                }
            } else {
                stringArr = new String[question2.length()];
                for (int i = 0; i < question2.length(); i++) {
                    stringArr[i] = question2.optJSONObject(i).optString("name");
                    spinnerMap.put(question2.optJSONObject(i).optString("name"), question2.optJSONObject(i).optString("id"));
                }
            }

            OptionPicker picker = new OptionPicker(SecurityQuestion.this, stringArr);
//            picker.setOffset(2);
//            picker.setSelectedIndex(1);
            picker.setTextSize(15);

            picker.setTitleText("Select Question");
            picker.setTitleTextColor(ActivityCompat.getColor(SecurityQuestion.this, R.color.white));
            picker.setTopBackgroundColor(ActivityCompat.getColor(SecurityQuestion.this, R.color.colorAccent));
            picker.setTopLineColor(ActivityCompat.getColor(SecurityQuestion.this, R.color.colorAccent));
            picker.setCancelTextColor(ActivityCompat.getColor(SecurityQuestion.this, R.color.white));
            picker.setSubmitTextColor(ActivityCompat.getColor(SecurityQuestion.this, R.color.white));
            picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                @Override
                public void onOptionPicked(String option) {
                    final String id1 = spinnerMap.get(option);
                    if (check) {
                        etSecurityQuestion1.setText(option);
                        etSecurityQuestion1.setTag(id1);
                    } else {
                        etSecurityQuestion2.setText(option);
                        etSecurityQuestion2.setTag(id1);
                    }

                }
            });
            picker.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
