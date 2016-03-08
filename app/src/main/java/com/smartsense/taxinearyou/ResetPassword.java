package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smartsense.taxinearyou.utill.CommonUtil;

public class ResetPassword extends AppCompatActivity {

    EditText etResetPasswordEmailAddress;
    Button btResetPasswordSend;
    TextView tvResetPasswordForgotEmail;
    CoordinatorLayout clResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvResetPasswordForgotEmail = (TextView) findViewById(R.id.tvResetPasswordForgotEmail);
        btResetPasswordSend = (Button) findViewById(R.id.btResetPasswordSend);
        etResetPasswordEmailAddress = (EditText) findViewById(R.id.etResetPasswordEmailAddress);
        clResetPassword = (CoordinatorLayout) findViewById(R.id.clResetPassword);

        btResetPasswordSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etResetPasswordEmailAddress.getText().toString()))
                    CommonUtil.showSnackBar(ResetPassword.this, getString(R.string.enter_email_id), clResetPassword);
                else if (!CommonUtil.isValidEmail(etResetPasswordEmailAddress.getText().toString()))
                    CommonUtil.showSnackBar(ResetPassword.this, getString(R.string.enter_valid_email_id), clResetPassword);
                else
                    CommonUtil.openDialogs(ResetPassword.this, "Reset Password", R.id.lyPopUpLocatedAccount, R.id.btPopupLocatedBack);
            }
        });
        tvResetPasswordForgotEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPassword.this, RecoverEmailAddress.class));
            }
        });

    }
}
