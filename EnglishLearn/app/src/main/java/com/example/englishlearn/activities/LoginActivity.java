package com.example.englishlearn.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishlearn.R;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.models.User;
import com.example.englishlearn.sharedpreferences.DataLocalManage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText txtUser, txtPass;
    private Button btnLogin;
    private TextView lbRegister, lbFoget;
    private CheckBox ckRemenber;
    private long timeBackPressed;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mappingElements();
        checkisCheckRemember();
        eventElements();
    }

    private void checkisCheckRemember() {
        if(DataLocalManage.getCheckedRe()){
            txtUser.setText(DataLocalManage.getUsernameLogined());
            txtPass.setText(DataLocalManage.getPasswordLogined());
            ckRemenber.setChecked(true);
        }
    }

    private void mappingElements() {
        txtUser = findViewById(R.id.la_txtuser);
        txtPass = findViewById(R.id.la_txtpass);
        btnLogin = findViewById(R.id.la_btnlogin);
        lbRegister = findViewById(R.id.la_lbregister);
        lbFoget = findViewById(R.id.la_lbfoget);
        ckRemenber = findViewById(R.id.la_ckremenber);
        mDialog = new ProgressDialog(this);
        mDialog.setTitle(R.string.title_progess);
        mDialog.setMessage(getResources().getString(R.string.content_progess));
        mDialog.setCancelable(false);
    }

    private void eventElements() {
        lbRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toActivity(RegisterActivity.class);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerLogin();
            }
        });
    }

    private void handlerLogin() {
        if(!validator(txtUser) || !validator(txtPass)){
            return;
        }
        String username = txtUser.getText().toString().trim();
        String password = txtPass.getText().toString().trim();
        mDialog.show();
        APIServer.api.loginHandler(username,password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                mDialog.dismiss();
                User resul = response.body();
                if (resul != null) {
                    if(ckRemenber.isChecked()){
                        DataLocalManage.setCheckedRe(true);
                        DataLocalManage.setUsernameLogined(username);
                        DataLocalManage.setPasswordLogined(password);
                    }else {
                        DataLocalManage.setCheckedRe(false);
                        DataLocalManage.setUsernameLogined("");
                        DataLocalManage.setPasswordLogined("");
                    }
                    DataLocalManage.setIsLogin(true);
                    DataLocalManage.setUsertoCheck(username);
                    toActivity(HomeActivity.class);
                    finishAffinity();
                } else {
                    dialogLoginFalse();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void toActivity(Class<?> to) {
        Intent intent = new Intent(this, to);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (timeBackPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(LoginActivity.this, R.string.press_back_again, Toast.LENGTH_LONG).show();
        }
        timeBackPressed = System.currentTimeMillis();
    }

    private boolean validator(EditText txtCheck) {
        if (txtCheck.getText().length() < 1) {
            txtCheck.setText("");
            txtCheck.setHint(R.string.hint_errorr_edittext);
            txtCheck.setHintTextColor(getResources().getColor(R.color.red));
            txtCheck.requestFocus();
            return false;
        }
        return true;
    }

    private void dialogLoginFalse() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_login_fail);

        Button btnTry = dialog.findViewById(R.id.dlf_btntry);
        Button btnRegister = dialog.findViewById(R.id.dlf_btnregister);

        btnTry.setOnClickListener(view -> {
            dialog.dismiss();
            resetValues();
        });
        btnRegister.setOnClickListener(view -> {
            dialog.dismiss();
            toActivity(RegisterActivity.class);
        });

        dialog.show();
    }

    private void resetValues() {
        txtUser.setText("");
        txtUser.setHint(R.string.enter_your_username);
        txtUser.setHintTextColor(getResources().getColor(R.color.gray));
        txtUser.requestFocus();
        txtPass.setText("");
        txtPass.setHint(R.string.enter_your_password);
        txtPass.setHintTextColor(getResources().getColor(R.color.gray));
    }
}