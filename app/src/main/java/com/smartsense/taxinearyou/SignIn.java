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

import com.smartsense.taxinearyou.utill.CommonUtil;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

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
                else
                    startActivity(new Intent(SignIn.this, Search.class));
                break;
            case R.id.btSignInSignUp:
                startActivity(new Intent(SignIn.this, SignUp.class));
                break;
            case R.id.tvSignInForgotPassword:
                startActivity(new Intent(SignIn.this, ResetPassword.class));
                break;
            case R.id.tvSignInForgotEmail:
                startActivity(new Intent(SignIn.this, RecoverEmailAddress.class));
                break;
        }

    }
}
