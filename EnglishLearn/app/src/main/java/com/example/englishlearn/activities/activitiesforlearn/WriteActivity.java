package com.example.englishlearn.activities.activitiesforlearn;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishlearn.R;
import com.example.englishlearn.activities.CompleteJobActivity;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.handlerothers.ConstSendData;
import com.example.englishlearn.models.Answer;
import com.example.englishlearn.models.Part;
import com.example.englishlearn.models.Question;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteActivity extends AppCompatActivity {

    private List<Answer> answerList;
    private List<Question> questionList;
    private ProgressDialog mDialog;

    private MediaPlayer mediaPlayerNotification;

    private ArrayList<Integer> wrong;
    private int Current_Question = 0;
    private int ID_QUESTION_CURRENT = 0;
    private int poinoflearn = 15;

    private TextView lbQuestion;
    private EditText txtAnswer;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        chcekIntent();
    }

    private void chcekIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Part part = (Part) bundle.getSerializable(ConstSendData.INTENT_SEND_PART);
            if (part != null) {
                callAPIgetListQuestionandAnswer(part);
            }
        }
    }

    private void callAPIgetListQuestionandAnswer(Part part) {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle(R.string.title_progess);
        mDialog.setMessage(getResources().getString(R.string.content_progess));
        mDialog.setCancelable(false);

        questionList = new ArrayList<>();
        answerList = new ArrayList<>();
        mDialog.show();
        APIServer.api.getListQuestions(part.getId()).enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                for (Question item : response.body()) {
                    questionList.add(item);
                }
                APIServer.api.getListAnswers(part.getId()).enqueue(new Callback<List<Answer>>() {
                    @Override
                    public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
                        for (Answer item : response.body()) {
                            answerList.add(item);
                        }
                        mDialog.dismiss();
                        mappingelements();
                        eventElements();
                    }

                    @Override
                    public void onFailure(Call<List<Answer>> call, Throwable t) {
                        mDialog.dismiss();
                        Toast.makeText(WriteActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(WriteActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mappingelements() {
        lbQuestion = findViewById(R.id.aw_textquestion);
        txtAnswer = findViewById(R.id.aw_txtanswer);
        btnConfirm = findViewById(R.id.aw_confirmAnswer);
        wrong = new ArrayList<>();

        setQeestions();
    }

    private void setQeestions() {
        if (Current_Question < questionList.size()) {
            ID_QUESTION_CURRENT = questionList.get(Current_Question).getId();

            for (Answer item : answerList) {
                if (item.getIdquestion() == questionList.get(Current_Question).getId()) {
                    lbQuestion.setText(item.getContent());
                    break;
                }
            }

        } else if (wrong.size() > 0) {
            ID_QUESTION_CURRENT = questionList.get(wrong.get(0)).getId();

            for (Answer item : answerList) {
                if (item.getIdquestion() == questionList.get(wrong.get(0)).getId()) {
                    lbQuestion.setText(item.getContent());
                    break;
                }
            }
            wrong.remove(0);
        } else {
            completeLearn();
        }
    }

    private void completeLearn() {
        if (mediaPlayerNotification != null) {
            mediaPlayerNotification.release();
            mediaPlayerNotification = null;
        }
        Intent intent = new Intent(this, CompleteJobActivity.class);
        intent.putExtra(ConstSendData.INTENT_SEND_POIN_OF_LEARN, poinoflearn);
        startActivity(intent);
        finish();
    }

    private void isAnswerTrue() {
        boolean ischeck = false;
        String result = txtAnswer.getText().toString().replaceAll("( )+", " ").trim();

        for (Question answer : questionList) {
            if (answer.getId() == ID_QUESTION_CURRENT) {
                if (answer.getContent().trim().equalsIgnoreCase(result)) {
                    ischeck = true;
                    break;
                }
            }
        }
        showDialogNotification(ischeck);
    }

    private void showDialogNotification(boolean status) {
        Dialog dialogstatusAnswer = new Dialog(this);
        dialogstatusAnswer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogstatusAnswer.setCancelable(false);
        dialogstatusAnswer.setContentView(R.layout.dialog_answer_status);

        TextView titleStatus = dialogstatusAnswer.findViewById(R.id.das_status);
        TextView titlebody = dialogstatusAnswer.findViewById(R.id.das_title);
        TextView content = dialogstatusAnswer.findViewById(R.id.das_answer);
        ImageView comment = dialogstatusAnswer.findViewById(R.id.das_answerother);
        Button btnNext = dialogstatusAnswer.findViewById(R.id.das_btn_next);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setQeestions();
                dialogstatusAnswer.dismiss();
            }
        });

        if (status) {
            titleStatus.setText(R.string.good);
            titleStatus.setTextColor(getResources().getColor(R.color.green));
            titlebody.setVisibility(View.GONE);
            content.setVisibility(View.GONE);
            btnNext.setText(R.string.next);
            btnNext.setBackgroundColor(getResources().getColor(R.color.green));
            Current_Question++;
            resetDislayandStystem();
            phayNotificationAudio(R.raw.right_answer);
        } else {
            titleStatus.setText(R.string.wrong);
            titleStatus.setTextColor(getResources().getColor(R.color.red));
            titlebody.setVisibility(View.VISIBLE);
            content.setVisibility(View.VISIBLE);
            content.setText(getRightAnswer());
            btnNext.setText(R.string.understood);
            btnNext.setBackgroundColor(getResources().getColor(R.color.red));
            wrong.add(Current_Question);
            Current_Question++;

            if (poinoflearn >= 10) {
                poinoflearn--;
            }

            resetDislayandStystem();
            phayNotificationAudio(R.raw.wrong_answer);
        }

        dialogstatusAnswer.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogstatusAnswer.getWindow().getAttributes());
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogstatusAnswer.getWindow().setAttributes(lp);
    }

    private String getRightAnswer() {
        for (Question answer : questionList) {
            if (answer.getId() == ID_QUESTION_CURRENT) {
                return answer.getContent();
            }
        }
        return "";
    }

    private void resetDislayandStystem() {
        txtAnswer.setText("");
    }

    private void phayNotificationAudio(int statusAnswer) {
        mediaPlayerNotification = MediaPlayer.create(this, statusAnswer);

        if (mediaPlayerNotification.isPlaying()) {
            mediaPlayerNotification.stop();
        }

        mediaPlayerNotification.start();
    }

    private void eventElements() {
        txtAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isEnabledButtonNext(editable);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAnswerTrue();
            }
        });
    }

    private void isEnabledButtonNext(Editable editable) {
        if (editable.length() > 0) {
            btnConfirm.setEnabled(true);
            btnConfirm.setBackgroundColor(getResources().getColor(R.color.green));
        } else {
            btnConfirm.setEnabled(false);
            btnConfirm.setBackgroundColor(getResources().getColor(R.color.gray));
        }
    }

    @Override
    public void onBackPressed() {
        Dialog dialogconfirmexit = new Dialog(WriteActivity.this);
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
                if (mediaPlayerNotification != null) {
                    mediaPlayerNotification.release();
                    mediaPlayerNotification = null;
                }
                finish();
            }
        });
        dialogconfirmexit.show();
        dialogconfirmexit.getWindow().setAttributes(lp);
    }
}