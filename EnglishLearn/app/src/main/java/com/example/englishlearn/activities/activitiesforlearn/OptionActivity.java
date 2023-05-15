package com.example.englishlearn.activities.activitiesforlearn;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishlearn.R;
import com.example.englishlearn.activities.CompleteJobActivity;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.handlerothers.ConstSendData;
import com.example.englishlearn.models.Part;
import com.example.englishlearn.models.Question;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private List<Question> questionList;
    private List<Question> answerList;
    private ProgressDialog mDialog;
    private TextView lbQuestion;
    private FlowLayout contentAnswerSeleted;
    private FlowLayout containChoice;
    private ImageView speakAgain;
    private Button btnComplete;
    private TextToSpeech textToSpeech;
    private int STATUS_LISTEN = 1;
    private int Current_Question = 0;
    private ArrayList<Integer> wrong;
    private MediaPlayer mediaPlayerNotification;
    private int ID_QUESTION_CURRENT = 0;
    private int poinoflearn = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

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
                    answerList.add(item);
                }
                mDialog.dismiss();
                Collections.shuffle(questionList);
                mappingelements();
                eventElements();
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(OptionActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mappingelements() {
        speakAgain = findViewById(R.id.ac_peaker);
        lbQuestion = findViewById(R.id.ac_textquestion);
        contentAnswerSeleted = findViewById(R.id.ac_containAnswer);
        containChoice = findViewById(R.id.ac_containChoise);
        btnComplete = findViewById(R.id.ac_confirmAnswer);
        wrong = new ArrayList<>();
        textToSpeech = new TextToSpeech(this, this);

        setQeestions();
    }

    private void setQeestions() {
        lbQuestion.setVisibility(View.GONE);
        if (Current_Question < questionList.size()) {
            ID_QUESTION_CURRENT = questionList.get(Current_Question).getId();
            lbQuestion.setText(questionList.get(Current_Question).getContent());
            setAnswerofQuestion(ID_QUESTION_CURRENT);
        } else if (wrong.size() > 0) {
            ID_QUESTION_CURRENT = questionList.get(wrong.get(0)).getId();
            lbQuestion.setText(questionList.get(wrong.get(0)).getContent());
            wrong.remove(0);
            setAnswerofQuestion(ID_QUESTION_CURRENT);
        } else {
            completeLearn();
        }
    }

    private void completeLearn() {
        if(mediaPlayerNotification != null){
            mediaPlayerNotification.release();
            mediaPlayerNotification = null;
        }
        Intent intent = new Intent(this, CompleteJobActivity.class);
        intent.putExtra(ConstSendData.INTENT_SEND_POIN_OF_LEARN,poinoflearn);
        startActivity(intent);
        finish();
    }

    private void setAnswerofQuestion(int idQuestionSelected) {
        convertTextToSpeak(lbQuestion.getText().toString());
        StringBuilder stranswer = new StringBuilder();
        for (Question answer : answerList) {
            if (answer.getId() == idQuestionSelected) {
                stranswer.append(answer.getContent().trim() + " ");
            }
        }
        if (stranswer.length() < 50){
            List<Question> listswp = answerList;
            Collections.shuffle(listswp);
            for (int i = 0; i < 3 ; i++){
                stranswer.append(listswp.get(i).getContent().trim() + " ");
                if(stranswer.length() > 80){
                    break;
                }
            }
        }

        String[] arrFirst = stranswer.toString().trim().split(" ");
        String[] arrsComplete = swpAnswers(arrFirst);
        for (String arr : arrsComplete) {
            TextView textView = new TextView(this);
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 10, 20, 10);
            textView.setLayoutParams(params);
            textView.setText(arr);
            textView.setTextSize(20);
            textView.setBackground(getResources().getDrawable(R.drawable.transparent_corner_17));
            textView.setTextColor(getResources().getColor(R.color.darkblue));
            textView.setPadding(30, 10, 30, 10);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setAnswerSeleted(contentAnswerSeleted, textView.getText().toString(), containChoice);
                    containChoice.removeView(textView);
                }
            });
            containChoice.addView(textView);
        }
    }

    private String[] swpAnswers(String[] arrs) {
        ArrayList arrSwp = new ArrayList(Arrays.asList(arrs));
        Collections.shuffle(arrSwp);
        String[] arrswp = new String[arrSwp.size()];

        for (int i = 0; i < arrSwp.size(); i++) {
            arrswp[i] = arrSwp.get(i).toString();
        }

        return arrswp;
    }

    private void setAnswerSeleted(FlowLayout layoutto, String content, FlowLayout layoutfrom) {
        TextView textView = new TextView(this);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 10, 20, 10);
        textView.setLayoutParams(params);
        textView.setText(content);
        textView.setTextSize(20);
        textView.setBackground(getResources().getDrawable(R.drawable.transparent_corner_17));
        textView.setTextColor(getResources().getColor(R.color.darkblue));
        textView.setPadding(30, 10, 30, 10);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAnswerSeleted(layoutfrom, textView.getText().toString(), layoutto);
                layoutto.removeView(textView);
            }
        });
        layoutto.addView(textView);
        isEnabledButtonNext();
    }

    private void isEnabledButtonNext() {
        if (contentAnswerSeleted.getChildCount() > 0) {
            btnComplete.setEnabled(true);
            btnComplete.setBackgroundColor(getResources().getColor(R.color.green));
        } else {
            btnComplete.setEnabled(false);
            btnComplete.setBackgroundColor(getResources().getColor(R.color.gray));
        }
    }

    private void eventElements() {
        speakAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (STATUS_LISTEN == 1) {
                    textToSpeech.setSpeechRate(1f);
                    convertTextToSpeak(lbQuestion.getText().toString());
                    STATUS_LISTEN = 2;
                } else {
                    textToSpeech.setSpeechRate(0.3f);
                    convertTextToSpeak(lbQuestion.getText().toString());
                    STATUS_LISTEN = 1;
                }
            }
        });

        contentAnswerSeleted.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> isEnabledButtonNext());

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAnswerTrue();
            }
        });
    }

    private void resetDislayandStystem() {
        textToSpeech.setSpeechRate(1f);
        STATUS_LISTEN = 1;
        contentAnswerSeleted.removeAllViews();
        containChoice.removeAllViews();
    }

    private void isAnswerTrue() {
        int count = contentAnswerSeleted.getChildCount();
        StringBuilder builder = new StringBuilder();
        boolean ischeck = false;

        for (int i = 0; i < count; i++) {
            TextView view = (TextView) contentAnswerSeleted.getChildAt(i);
            builder.append(view.getText().toString()).append(" ");
        }

        String result = builder.toString().trim();

        for (Question answer : answerList) {
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

            if(poinoflearn >= 10){
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

    private void phayNotificationAudio(int statusAnswer) {
        mediaPlayerNotification = MediaPlayer.create(this, statusAnswer);

        if (mediaPlayerNotification.isPlaying()) {
            mediaPlayerNotification.stop();
        }

        mediaPlayerNotification.start();
    }

    private String getRightAnswer() {
        for (Question answer : answerList) {
            if (answer.getId() == ID_QUESTION_CURRENT) {
                return answer.getContent();
            }
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        Dialog dialogconfirmexit = new Dialog(OptionActivity.this);
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
                if(textToSpeech.isSpeaking() || textToSpeech != null){
                    textToSpeech.shutdown();
                }
                finish();
            }
        });
        dialogconfirmexit.show();
        dialogconfirmexit.getWindow().setAttributes(lp);
    }

    private void convertTextToSpeak(String text) {
        String utteranceId = String.valueOf(this.hashCode());
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.ENGLISH);
            convertTextToSpeak(lbQuestion.getText().toString());
        }
    }
}