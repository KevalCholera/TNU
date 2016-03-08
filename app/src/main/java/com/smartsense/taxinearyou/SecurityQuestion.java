package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.smartsense.taxinearyou.utill.CommonUtil;

public class SecurityQuestion extends AppCompatActivity {

    EditText etSecurityQuestion1, etSecurityAnswer1, etSecurityQuestion2, etSecurityAnswer2;
    CheckBox cbSecurityFromOrganization, cbSecurityFromPrivacyPolicy, cbSecurityAboutTaxinearu;
    Button btSecuritySave;
    TextView tvSecurityTermsConditions;
    CoordinatorLayout clSecurityQuestion;

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

        btSecuritySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etSecurityAnswer1.getText().toString()) || TextUtils.isEmpty(etSecurityAnswer2.getText().toString()))
                    CommonUtil.showSnackBar(SecurityQuestion.this, "Please Answer for Security Question", clSecurityQuestion);
                else if (!cbSecurityFromPrivacyPolicy.isChecked())
                    CommonUtil.showSnackBar(SecurityQuestion.this, "Please Agree to Terms and Conditions ", clSecurityQuestion);
                else
                    startActivity(new Intent(SecurityQuestion.this, SignIn.class));

            }
        });
    }
}
