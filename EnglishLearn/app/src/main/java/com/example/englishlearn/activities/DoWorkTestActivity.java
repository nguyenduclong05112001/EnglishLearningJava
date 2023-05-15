package com.example.englishlearn.activities;

import static com.example.englishlearn.handlerothers.ConstSendData.KEY_SEND_NUMBER_QUESTION_FALSE;
import static com.example.englishlearn.handlerothers.ConstSendData.KEY_SEND_OBJECT_TEST_DONE;
import static com.example.englishlearn.handlerothers.ConstSendData.KEY_SEND_TOTAL_NUMBER_QUESTION;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishlearn.R;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.handlerothers.ConstSendData;
import com.example.englishlearn.models.AnswerOfStory;
import com.example.englishlearn.models.QuestionOfTest;
import com.example.englishlearn.models.TheTestOfUsers;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoWorkTestActivity extends AppCompatActivity {
    private TheTestOfUsers theTestSent;
    private List<QuestionOfTest> mListQuestionOfTest;
    private ProgressDialog mDialog;
    private TextView Question, Answer1, Answer2, Answer3, Answer4, numberQuestion;
    private int CURRENT_QUESTION = 1;
    private MediaPlayer mMediaPlayer;
    private int numberAnswerFalse = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_work_test);

        checkIntentSent();
    }

    private void checkIntentSent() {
        theTestSent = (TheTestOfUsers) getIntent().getExtras().getSerializable(ConstSendData.KEY_SEND_OBJECT_TEST);

        if (theTestSent != null) {
            callAPIGetAllQuestionOfTest();
        }
    }

    private void callAPIGetAllQuestionOfTest() {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle(R.string.title_progess);
        mDialog.setMessage(getResources().getString(R.string.content_progess));
        mDialog.setCancelable(false);

        mListQuestionOfTest = new ArrayList<>();

        mDialog.show();
        APIServer.api.getQuestionOfTestSelected(theTestSent.getId()).enqueue(new Callback<List<QuestionOfTest>>() {
            @Override
            public void onResponse(Call<List<QuestionOfTest>> call, Response<List<QuestionOfTest>> response) {
                for (QuestionOfTest item : response.body()) {
                    mListQuestionOfTest.add(item);
                }
                mDialog.dismiss();
                mappingElements();
                eventElemets();
            }

            @Override
            public void onFailure(Call<List<QuestionOfTest>> call, Throwable t) {
                Toast.makeText(DoWorkTestActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }
        });
    }

    private void mappingElements() {
        numberQuestion = findViewById(R.id.adwt_questionnumber);
        Question = findViewById(R.id.adwt_question);
        Answer1 = findViewById(R.id.adwt_answer1);
        Answer2 = findViewById(R.id.adwt_answer2);
        Answer3 = findViewById(R.id.adwt_answer3);
        Answer4 = findViewById(R.id.adwt_answer4);

        setContentForUI();
    }

    private void eventElemets() {
        Answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(Answer1, 1, mListQuestionOfTest.get(CURRENT_QUESTION - 1).getGoodanswer());
            }
        });

        Answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(Answer2, 2, mListQuestionOfTest.get(CURRENT_QUESTION - 1).getGoodanswer());
            }
        });

        Answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(Answer3, 3, mListQuestionOfTest.get(CURRENT_QUESTION - 1).getGoodanswer());
            }
        });

        Answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(Answer4, 4, mListQuestionOfTest.get(CURRENT_QUESTION - 1).getGoodanswer());
            }
        });
    }

    private void setContentForUI() {
        resetAnswers();
        if (CURRENT_QUESTION <= mListQuestionOfTest.size()) {

            String numberquestion = getString(R.string.question);
            numberQuestion.setText(numberquestion + CURRENT_QUESTION + "/" + mListQuestionOfTest.size());
            Question.setText(mListQuestionOfTest.get(CURRENT_QUESTION - 1).getContent());

            Answer1.setText(mListQuestionOfTest.get(CURRENT_QUESTION - 1).getAnswer1());
            Answer2.setText(mListQuestionOfTest.get(CURRENT_QUESTION - 1).getAnswer2());
            Answer3.setText(mListQuestionOfTest.get(CURRENT_QUESTION - 1).getAnswer3());
            Answer4.setText(mListQuestionOfTest.get(CURRENT_QUESTION - 1).getAnswer4());
        } else {
            completeQuestion();
        }
    }

    private void completeQuestion() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        Intent intent = new Intent(this, CompleteTheTestOfUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_SEND_OBJECT_TEST_DONE,theTestSent);
        bundle.putInt(KEY_SEND_NUMBER_QUESTION_FALSE,numberAnswerFalse);
        bundle.putInt(KEY_SEND_TOTAL_NUMBER_QUESTION,mListQuestionOfTest.size());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void resetAnswers() {
        Answer1.setBackground(getResources().getDrawable(R.drawable.layout_blue_corner_35));
        Answer2.setBackground(getResources().getDrawable(R.drawable.layout_blue_corner_35));
        Answer3.setBackground(getResources().getDrawable(R.drawable.layout_blue_corner_35));
        Answer4.setBackground(getResources().getDrawable(R.drawable.layout_blue_corner_35));
        Answer1.setEnabled(true);
        Answer2.setEnabled(true);
        Answer3.setEnabled(true);
        Answer4.setEnabled(true);
    }

    private void checkAnswer(TextView item, int current, int answer) {
        Answer1.setEnabled(false);
        Answer2.setEnabled(false);
        Answer3.setEnabled(false);
        Answer4.setEnabled(false);
        item.setBackground(getResources().getDrawable(R.drawable.layout_orange_corner_35));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (current == answer) {
                    playSoundAnnounce(R.raw.right_answer);
                    item.setBackground(getResources().getDrawable(R.drawable.layout_green_corner_35));
                    nextToQuestion(true);
                } else {
                    showAnswerTrue();
                    playSoundAnnounce(R.raw.wrong_answer);
                    item.setBackground(getResources().getDrawable(R.drawable.layout_red_corner_35));
                    nextToQuestion(false);
                }
            }
        }, 1500);
    }

    private void nextToQuestion(boolean status) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!status) {
                    numberAnswerFalse++;
                }
                CURRENT_QUESTION++;
                setContentForUI();
            }
        }, 1500);
    }

    private void playSoundAnnounce(int status) {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
        }

        mMediaPlayer = MediaPlayer.create(this, status);
        mMediaPlayer.start();
    }

    private void showAnswerTrue() {
        switch (mListQuestionOfTest.get(CURRENT_QUESTION - 1).getGoodanswer()) {
            case 1:
                Answer1.setBackground(getResources().getDrawable(R.drawable.layout_green_corner_35));
                break;
            case 2:
                Answer2.setBackground(getResources().getDrawable(R.drawable.layout_green_corner_35));
                break;
            case 3:
                Answer3.setBackground(getResources().getDrawable(R.drawable.layout_green_corner_35));
                break;
            case 4:
                Answer4.setBackground(getResources().getDrawable(R.drawable.layout_green_corner_35));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Dialog dialogconfirmexit = new Dialog(DoWorkTestActivity.this);
        dialogconfirmexit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogconfirmexit.setCanceledOnTouchOutside(false);
        dialogconfirmexit.setContentView(R.layout.dialog_confirm_end_learn);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogconfirmexit.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button btnNo = dialogconfirmexit.findViewById(R.id.dcel_no);
        Button btnYes = dialogconfirmexit.findViewById(R.id.dcel_yes);

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
                if (mMediaPlayer != null) {
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }
                finish();
            }
        });
        dialogconfirmexit.show();
        dialogconfirmexit.getWindow().setAttributes(lp);
    }
}