package com.example.englishlearn.activities;

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
import com.example.englishlearn.models.QuestionOfStory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionOfStoryActivity extends AppCompatActivity {
    private ProgressDialog mDialog;
    private List<QuestionOfStory> questionOfStoryList;
    private List<AnswerOfStory> answerOfStoryList;
    private List<AnswerOfStory> listAnswerOfQuestionCurrent;
    private TextView numberQuestion;
    private TextView Question;
    private TextView Answer1, Answer2, Answer3, Answer4;
    private int CURRENT_QUESTION = 0;
    private int poinoflearn = 15;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_of_story);

        checkIdOfStory();
    }

    private void checkIdOfStory() {
        int idStory = getIntent().getIntExtra(ConstSendData.KEY_SEND_ID_OF_STORY, -1);
        if (idStory != -1) {
            callAPIsetValuesList(idStory);
        }
    }

    private void callAPIsetValuesList(int idStory) {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle(R.string.title_progess);
        mDialog.setMessage(getResources().getString(R.string.content_progess));
        mDialog.setCancelable(false);

        questionOfStoryList = new ArrayList<>();
        answerOfStoryList = new ArrayList<>();

        mDialog.show();
        APIServer.api.getQuestionOfStory(idStory).enqueue(new Callback<List<QuestionOfStory>>() {
            @Override
            public void onResponse(Call<List<QuestionOfStory>> call, Response<List<QuestionOfStory>> response) {
                for (QuestionOfStory item : response.body()) {
                    questionOfStoryList.add(item);
                }

                APIServer.api.getAnswerOfStory(idStory).enqueue(new Callback<List<AnswerOfStory>>() {
                    @Override
                    public void onResponse(Call<List<AnswerOfStory>> call, Response<List<AnswerOfStory>> response) {
                        for (AnswerOfStory item : response.body()) {
                            answerOfStoryList.add(item);
                        }
                        mDialog.dismiss();
                        mappingElements();
                        eventElemets();
                    }

                    @Override
                    public void onFailure(Call<List<AnswerOfStory>> call, Throwable t) {
                        Toast.makeText(QuestionOfStoryActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                        mDialog.dismiss();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<QuestionOfStory>> call, Throwable t) {
                Toast.makeText(QuestionOfStoryActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }
        });

    }

    private void mappingElements() {
        listAnswerOfQuestionCurrent = new ArrayList<>();
        numberQuestion = findViewById(R.id.aqos_questionnumber);
        Question = findViewById(R.id.aqos_question);
        Answer1 = findViewById(R.id.aqos_answer1);
        Answer2 = findViewById(R.id.aqos_answer2);
        Answer3 = findViewById(R.id.aqos_answer3);
        Answer4 = findViewById(R.id.aqos_answer4);

        setContentforElements();
    }

    private void setContentforElements() {
        resetAnswers();
        if (CURRENT_QUESTION < questionOfStoryList.size()) {

            if (listAnswerOfQuestionCurrent.size() > 0) {
                listAnswerOfQuestionCurrent = new ArrayList<>();
            }

            for (AnswerOfStory item : answerOfStoryList) {
                if (item.getIdquestion() ==
                        questionOfStoryList.get(CURRENT_QUESTION).getId()) {
                    listAnswerOfQuestionCurrent.add(item);
                }
            }

            String numberquestion = getString(R.string.question);
            numberQuestion.setText(numberquestion + (CURRENT_QUESTION + 1));
            Question.setText(questionOfStoryList.get(CURRENT_QUESTION).getContent());

            Answer1.setText(listAnswerOfQuestionCurrent.get(0).getContent());
            Answer2.setText(listAnswerOfQuestionCurrent.get(1).getContent());
            Answer3.setText(listAnswerOfQuestionCurrent.get(2).getContent());
            Answer4.setText(listAnswerOfQuestionCurrent.get(3).getContent());
        } else {
            completeQuestion();
        }
    }

    private void completeQuestion() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        Intent intent = new Intent(this,CompleteJobActivity.class);
        intent.putExtra(ConstSendData.INTENT_SEND_POIN_OF_LEARN,poinoflearn);
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

    private void eventElemets() {
        Answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(Answer1, listAnswerOfQuestionCurrent.get(0));
            }
        });

        Answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(Answer2, listAnswerOfQuestionCurrent.get(1));
            }
        });

        Answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(Answer3, listAnswerOfQuestionCurrent.get(2));
            }
        });

        Answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(Answer4, listAnswerOfQuestionCurrent.get(3));
            }
        });
    }

    private void checkAnswer(TextView item, AnswerOfStory answer) {
        Answer1.setEnabled(false);
        Answer2.setEnabled(false);
        Answer3.setEnabled(false);
        Answer4.setEnabled(false);
        item.setBackground(getResources().getDrawable(R.drawable.layout_orange_corner_35));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (answer.getStatusanswer() == 1) {
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
                    int point = poinoflearn / questionOfStoryList.size();
                    poinoflearn = (poinoflearn - point);
                }
                CURRENT_QUESTION++;
                setContentforElements();
            }
        }, 1500);
    }

    private void showAnswerTrue() {
        if (listAnswerOfQuestionCurrent.get(0).getStatusanswer() == 1) {
            Answer1.setBackground(getResources().getDrawable(R.drawable.layout_green_corner_35));
        } else if (listAnswerOfQuestionCurrent.get(1).getStatusanswer() == 1) {
            Answer2.setBackground(getResources().getDrawable(R.drawable.layout_green_corner_35));
        } else if (listAnswerOfQuestionCurrent.get(2).getStatusanswer() == 1) {
            Answer3.setBackground(getResources().getDrawable(R.drawable.layout_green_corner_35));
        } else if (listAnswerOfQuestionCurrent.get(3).getStatusanswer() == 1) {
            Answer4.setBackground(getResources().getDrawable(R.drawable.layout_green_corner_35));
        }
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

    @Override
    public void onBackPressed() {
        Dialog dialogconfirmexit = new Dialog(QuestionOfStoryActivity.this);
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