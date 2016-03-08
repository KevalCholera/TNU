package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.smartsense.taxinearyou.utill.CommonUtil;

public class SignUp extends AppCompatActivity {

    EditText etSignUpFirstName, etSignUpLastName, etSignUpContact, etSignUpEmail,
            etSignUpPassword, etSignUpConfirmPassword, etSignUpAlternateEmail;
    CoordinatorLayout clSignUp;
    Button btSignUpSaveNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etSignUpFirstName = (EditText) findViewById(R.id.etSignUpFirstName);
        etSignUpLastName = (EditText) findViewById(R.id.etSignUpLastName);
        etSignUpContact = (EditText) findViewById(R.id.etSignUpContact);
        etSignUpEmail = (EditText) findViewById(R.id.etSignUpEmail);
        etSignUpPassword = (EditText) findViewById(R.id.etSignUpPassword);
        etSignUpConfirmPassword = (EditText) findViewById(R.id.etSignUpConfirmPassword);
        etSignUpAlternateEmail = (EditText) findViewById(R.id.etSignUpAlternateEmail);
        btSignUpSaveNext = (Button) findViewById(R.id.btSignUpSaveNext);
        clSignUp = (CoordinatorLayout) findViewById(R.id.clSignUp);

        btSignUpSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etSignUpFirstName.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_first_name), clSignUp);
                else if (TextUtils.isEmpty(etSignUpLastName.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_last_name), clSignUp);
                else if (TextUtils.isEmpty(etSignUpContact.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_contact_no), clSignUp);
                else if (etSignUpContact.length() < 7 || etSignUpContact.length() > 13)
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_valid_contact_no), clSignUp);
                else if (TextUtils.isEmpty(etSignUpEmail.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_email_id), clSignUp);
                else if (!CommonUtil.isValidEmail(etSignUpEmail.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_valid_email_id), clSignUp);
                else if (TextUtils.isEmpty(etSignUpAlternateEmail.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_alternate_email), clSignUp);
                else if (!CommonUtil.isValidEmail(etSignUpAlternateEmail.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_valid_alternate_email), clSignUp);
                else if (TextUtils.equals(etSignUpAlternateEmail.getText().toString(), etSignUpEmail.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.alternate_email_not_same), clSignUp);
                else if (TextUtils.isEmpty(etSignUpPassword.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_pass), clSignUp);
                else if (etSignUpPassword.length() < 7 || etSignUpPassword.length() > 15)
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_valid_pass), clSignUp);
                else if (TextUtils.isEmpty(etSignUpConfirmPassword.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_confirm_pass), clSignUp);
                else if (etSignUpConfirmPassword.length() < 7 || etSignUpPassword.length() > 15)
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_valid_confirm_pass), clSignUp);
                else if (!TextUtils.equals(etSignUpConfirmPassword.getText().toString(), etSignUpPassword.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.conpass_pass_same), clSignUp);
                else
                    startActivity(new Intent(SignUp.this, SecurityQuestion.class));
            }
        });
    }
}
