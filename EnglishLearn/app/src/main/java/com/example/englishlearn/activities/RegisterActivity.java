package com.example.englishlearn.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishlearn.R;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.sharedpreferences.DataLocalManage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtName,txtUser,txtPass,txtConfirm;
    private Button btnRegister;
    private TextView lbback;
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mappingElements();
        eventElements();
    }

    private void mappingElements() {
        txtName = findViewById(R.id.ra_txtname);
        txtUser = findViewById(R.id.ra_txtuser);
        txtPass = findViewById(R.id.ra_txtpass);
        txtConfirm = findViewById(R.id.ra_txtconfirm);
        btnRegister = findViewById(R.id.ra_btnregister);
        lbback = findViewById(R.id.ra_lbback);
        mDialog = new ProgressDialog(this);
        mDialog.setTitle(R.string.title_progess);
        mDialog.setMessage(getResources().getString(R.string.content_progess));
        mDialog.setCancelable(false);
    }

    private void eventElements() {
        lbback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toActivity(LoginActivity.class);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerRegister();
            }
        });
    }

    private void handlerRegister() {
        if(!validator(txtName) || !validator(txtUser) ||
                !validator(txtPass) || !validator(txtConfirm)){
            return;
        }
        String password = txtPass.getText().toString().trim();
        String confirm = txtConfirm.getText().toString().trim();

        if(!password.equals(confirm)){
            txtConfirm.setText("");
            Toast.makeText(this, R.string.errorr_edittext_pass_confirm, Toast.LENGTH_LONG).show();
            validator(txtConfirm);
            return;
        }

        String username = txtUser.getText().toString().trim();
        String name = txtName.getText().toString().trim();
        mDialog.show();
        APIServer.api.registerHandler(username,name,password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String resul = response.body();

                if(resul.equals("Success")){
                    showDialogConfirmRemember(username,password);
                }else {
                    Toast.makeText(RegisterActivity.this,R.string.registration_failed, Toast.LENGTH_LONG).show();
                    txtUser.setText("");
                    validator(txtUser);

                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(RegisterActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDialogConfirmRemember(String usern,String pass) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_confirm_remember);

        Button btnRe = dialog.findViewById(R.id.dcr_remember);
        Button btnNo = dialog.findViewById(R.id.dcr_no);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                toActivity(HomeActivity.class);
                DataLocalManage.setIsLogin(true);
                finishAffinity();
            }
        });

        btnRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                DataLocalManage.setUsernameLogined(usern);
                DataLocalManage.setPasswordLogined(pass);
                DataLocalManage.setCheckedRe(true);
                DataLocalManage.setIsLogin(true);
                toActivity(HomeActivity.class);
                finishAffinity();
            }
        });
        DataLocalManage.setUsertoCheck(usern);
        dialog.show();
    }

    private void toActivity( Class<?> to) {
        Intent intent = new Intent(this,to);
        startActivity(intent);
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

    private void resetValues() {
        txtName.setText("");
        txtName.setHint(R.string.enter_your_name);
        txtName.setHintTextColor(getResources().getColor(R.color.gray));
        txtName.requestFocus();
        txtUser.setText("");
        txtUser.setHint(R.string.enter_your_username);
        txtUser.setHintTextColor(getResources().getColor(R.color.gray));
        txtPass.setText("");
        txtPass.setHint(R.string.enter_your_password);
        txtPass.setHintTextColor(getResources().getColor(R.color.gray));
        txtConfirm.setText("");
        txtConfirm.setHint(R.string.confirm_password);
        txtConfirm.setHintTextColor(getResources().getColor(R.color.gray));
    }
}