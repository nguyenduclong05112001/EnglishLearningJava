package com.example.englishlearn.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishlearn.R;
import com.example.englishlearn.activities.activitiesforlearn.OptionActivity;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.handlerothers.ConstSendData;
import com.example.englishlearn.models.QuestionOfTest;
import com.example.englishlearn.models.TheTestLocal;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateQuestionOfTestActivity extends AppCompatActivity {
    private RadioButton radioOne, radioTwo, radioThree, radioFour;
    private TextView tvNumQuest;
    private EditText etContentQuest, etanswer1, etanswer2, etanswer3, etanswer4;
    private Button btnNext, btnBack;
    private List<QuestionOfTest> mListQuestofTest;
    private int numQuest = 0, currentQuest = 1;
    private TheTestLocal textSent;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question_of_test);

        mappingElements();
        checkIntentSent();
        eventalement();
    }

    private void checkIntentSent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            textSent = (TheTestLocal) bundle.getSerializable(ConstSendData.KEY_SEND_OBJECT_TEST_FRAGMENT);
            if (textSent != null) {
                numQuest = textSent.getNumberQuestion();
                if (numQuest != 0) {
                    setValuesforNumQuest();
                }
            }
        }
    }

    private void setValuesforNumQuest() {
        if (currentQuest <= numQuest) {
            tvNumQuest.setText(currentQuest + "/" + numQuest);
        }
    }

    private void eventalement() {
        radioOne.setOnClickListener(v -> {
            radioTwo.setChecked(false);
            radioThree.setChecked(false);
            radioFour.setChecked(false);
        });

        radioTwo.setOnClickListener(v -> {
            radioOne.setChecked(false);
            radioThree.setChecked(false);
            radioFour.setChecked(false);
        });

        radioThree.setOnClickListener(v -> {
            radioTwo.setChecked(false);
            radioOne.setChecked(false);
            radioFour.setChecked(false);
        });

        radioFour.setOnClickListener(v -> {
            radioTwo.setChecked(false);
            radioThree.setChecked(false);
            radioOne.setChecked(false);
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRadioChecked()) {
                    Toast.makeText(CreateQuestionOfTestActivity.this, getString(R.string.please_choose_the_correct_answer), Toast.LENGTH_LONG).show();
                    return;
                }

                if (!isEmptyEditText(etContentQuest) ||
                        !isEmptyEditText(etanswer1) ||
                        !isEmptyEditText(etanswer2) ||
                        !isEmptyEditText(etanswer3) ||
                        !isEmptyEditText(etanswer4)) {
                    return;
                }
                if (currentQuest <= numQuest) {
                    mListQuestofTest.add(new QuestionOfTest(
                            currentQuest,
                            textSent.getId(),
                            etContentQuest.getText().toString().trim(),
                            etanswer1.getText().toString().trim(),
                            etanswer2.getText().toString().trim(),
                            etanswer3.getText().toString().trim(),
                            etanswer4.getText().toString().trim(),
                            radioCheced()
                    ));
                    currentQuest++;
                    resetValuesOfActivity();
                } else {
                    callAPIInsertQuestionofTest();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuest == 1) {
                    Toast.makeText(CreateQuestionOfTestActivity.this, getString(R.string.you_are_in_first_place), Toast.LENGTH_LONG).show();
                    return;
                }
                currentQuest--;
                handlerBackTheQuestion();
            }
        });
    }

    private void handlerBackTheQuestion() {
        QuestionOfTest previous = mListQuestofTest.get(currentQuest-1);
        mListQuestofTest.remove(previous);
        etContentQuest.setText(previous.getContent());
        etanswer1.setText(previous.getAnswer1());
        etanswer2.setText(previous.getAnswer2());
        etanswer3.setText(previous.getAnswer3());
        etanswer4.setText(previous.getAnswer4());
        switch (previous.getGoodanswer()){
            case 1:
                radioOne.setChecked(true);
                break;
            case 2:
                radioTwo.setChecked(true);
                break;
            case 3:
                radioThree.setChecked(true);
                break;
            case 4:
                radioFour.setChecked(true);
                break;
        }
        setValuesforNumQuest();
    }

    private void resetValuesOfActivity() {
        if (currentQuest <= numQuest) {
            etContentQuest.setText("");
            etContentQuest.setHintTextColor(getResources().getColor(R.color.gray));
            etContentQuest.requestFocus();
            etanswer1.setText("");
            etanswer1.setHintTextColor(getResources().getColor(R.color.gray));
            etanswer2.setText("");
            etanswer2.setHintTextColor(getResources().getColor(R.color.gray));
            etanswer3.setText("");
            etanswer3.setHintTextColor(getResources().getColor(R.color.gray));
            etanswer4.setText("");
            etanswer4.setHintTextColor(getResources().getColor(R.color.gray));
            radioOne.setChecked(false);
            radioTwo.setChecked(false);
            radioThree.setChecked(false);
            radioFour.setChecked(false);
            setValuesforNumQuest();
        }
    }

    private void callAPIInsertQuestionofTest() {
        mDialog.show();
        for (QuestionOfTest item : mListQuestofTest) {
            APIServer.api.insertAllQuestionOfTest(item.getIdtest(),
                    item.getContent(),
                    item.getAnswer1(),
                    item.getAnswer2(),
                    item.getAnswer3(),
                    item.getAnswer4(),
                    String.valueOf(item.getGoodanswer())).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (!response.body().equalsIgnoreCase("success")) {
                        callAPIDeleteAllUpdated();
                        mDialog.dismiss();
                        Toast.makeText(CreateQuestionOfTestActivity.this, getString(R.string.added_error_data_please_try_again), Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {
                    mDialog.dismiss();
                    Toast.makeText(CreateQuestionOfTestActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    return;
                }
            });
        }
        mDialog.dismiss();
        toActivity();
    }

    private void toActivity() {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void callAPIDeleteAllUpdated() {
        mDialog.show();
        APIServer.api.deleteAllQuestionOfTestErrorr(textSent.getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equalsIgnoreCase("success")) {
                    mDialog.dismiss();
                    return;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                mDialog.dismiss();
                Toast.makeText(CreateQuestionOfTestActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private void mappingElements() {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle(R.string.title_progess);
        mDialog.setMessage(getResources().getString(R.string.content_progess));
        mDialog.setCancelable(false);
        mListQuestofTest = new ArrayList<>();
        radioOne = findViewById(R.id.acqot_radio_btn1);
        radioTwo = findViewById(R.id.acqot_radio_btn2);
        radioThree = findViewById(R.id.acqot_radio_btn3);
        radioFour = findViewById(R.id.acqot_radio_btn4);
        etContentQuest = findViewById(R.id.acqot_question);
        tvNumQuest = findViewById(R.id.acqot_num_quest_curr);
        etanswer1 = findViewById(R.id.acqot_answer1);
        etanswer2 = findViewById(R.id.acqot_answer2);
        etanswer3 = findViewById(R.id.acqot_answer3);
        etanswer4 = findViewById(R.id.acqot_answer4);
        btnBack = findViewById(R.id.acqot_btn_back);
        btnNext = findViewById(R.id.acqot_btn_next);
    }

    private boolean isEmptyEditText(EditText edt) {
        if (edt.getText().toString().trim().isEmpty()) {
            edt.setHintTextColor(getResources().getColor(R.color.red));
            edt.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isRadioChecked() {
        if (!radioOne.isChecked() &&
                !radioThree.isChecked() &&
                !radioFour.isChecked() &&
                !radioTwo.isChecked()) {
            return false;
        }
        return true;
    }

    private int radioCheced() {
        if (radioOne.isChecked()) {
            return 1;
        }
        if (radioTwo.isChecked()) {
            return 2;
        }
        if (radioThree.isChecked()) {
            return 3;
        }
        if (radioFour.isChecked()) {
            return 4;
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        Dialog dialogconfirmexit = new Dialog(CreateQuestionOfTestActivity.this);
        dialogconfirmexit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogconfirmexit.setCanceledOnTouchOutside(false);
        dialogconfirmexit.setContentView(R.layout.dialog_confirm_end_create_test);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogconfirmexit.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button btnNo = dialogconfirmexit.findViewById(R.id.dcect_no);
        Button btnYes = dialogconfirmexit.findViewById(R.id.dcect_yes);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogconfirmexit.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogconfirmexit.dismiss();
                callAPIDeleteTheTestCreated();
                toActivity();
            }
        });
        dialogconfirmexit.show();
        dialogconfirmexit.getWindow().setAttributes(lp);
    }

    private void callAPIDeleteTheTestCreated() {
        mDialog.show();
        APIServer.api.deleteTestCreated(textSent.getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equalsIgnoreCase("success")) {
                    mDialog.dismiss();
                    return;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                mDialog.dismiss();
                Toast.makeText(CreateQuestionOfTestActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                return;
            }
        });
    }
}