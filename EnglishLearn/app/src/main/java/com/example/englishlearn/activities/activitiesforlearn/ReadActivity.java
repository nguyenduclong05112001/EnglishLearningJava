package com.example.englishlearn.activities.activitiesforlearn;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishlearn.R;
import com.example.englishlearn.activities.CompleteJobActivity;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.handlerothers.ConstSendData;
import com.example.englishlearn.models.Part;
import com.example.englishlearn.models.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private LinearLayout StartListen, Listening;
    private ImageView btnSpeak, imgGit;
    private TextView question;
    private Button btnNext;
    private ProgressDialog mDialog;
    private List<Question> questionList;
    private MediaPlayer mediaPlayerNotification;
    private int poinoflearn = 15;

    //TextToSpeech
    private TextToSpeech mTextToSpeech;
    private int Current_Question = 0;
    private ArrayList<Integer> wrong;
    private int STATUS_LISTEN = 1;

    // SpeechRecognizer
    private SpeechRecognizer mSpeechRecognizer;
    private Intent SpeechRecognizerIntent;
    private String LANGUAGE = "en";
    private long TIMEOUT = 2500;
    private int numberReadWrong = 1;
    private boolean isFailbutNext = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

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

        mDialog.show();
        APIServer.api.getListQuestions(part.getId()).enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                for (Question item : response.body()) {
                    questionList.add(item);
                }
                mDialog.dismiss();
                mappingElements();
                eventElements();
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(ReadActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void eventElements() {
        StartListen.setOnClickListener(v -> {
            STATUS_LISTEN = 1;
            mTextToSpeech.setSpeechRate(1f);
            mSpeechRecognizer.startListening(SpeechRecognizerIntent);
        });

        Listening.setOnClickListener(view -> {
            mSpeechRecognizer.cancel();
        });

        btnSpeak.setOnClickListener(view -> {
            if (STATUS_LISTEN == 1) {
                mTextToSpeech.setSpeechRate(1f);
                convertTextToSpeak(question.getText().toString());
                STATUS_LISTEN = 2;
            } else {
                mTextToSpeech.setSpeechRate(0.3f);
                convertTextToSpeak(question.getText().toString());
                STATUS_LISTEN = 1;
            }
        });

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                hideStartListen();
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                showStartListen();
            }

            @Override
            public void onError(int i) {
                showStartListen();
            }

            @Override
            public void onResults(Bundle bundle) {
                String data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
                checkReaded(data);
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    //cần sửa lại thuật toán nhận dạng hơi chán
    private void checkReaded(String data) {
        if (data.trim().equalsIgnoreCase(
                question.getText().toString().trim())) {
            showDialogNotification(true, 100);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            String[] arrReaded = data.trim().split(" ");
            String[] arrQestion = question.getText().toString().trim().split(" ");
            //here good morning
            for (int i = 0; i < arrReaded.length; i++) {
                if (i < arrQestion.length) {
                    for (int j = i; j < arrQestion.length; j++) {
                        if (arrReaded[i].trim().equalsIgnoreCase(arrQestion[j].trim())) {
                            stringBuilder.append(arrReaded[i].trim()).append(" ");
                        }
                    }
                }
            }
            checkPercentofReaded(stringBuilder.toString().trim());
        }
    }

    private void checkPercentofReaded(String item) {
        String[] arrQestion = question.getText().toString().trim().split(" ");
        int elementQuestion = arrQestion.length;
        int elementReaded;
        if (!item.trim().isEmpty()) {
            String[] arrReaded = item.split(" ");
            elementReaded = arrReaded.length;
        } else {
            elementReaded = 0;
        }
        int percent = elementReaded * 100 / elementQuestion;

        if (percent >= 70.0) {
            showDialogNotification(true, percent);
        } else {
            showDialogNotification(false, percent);
        }
    }

    private void showDialogNotification(boolean status, int percent) {
        Dialog dialogstatusAnswer = new Dialog(this);
        dialogstatusAnswer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogstatusAnswer.setCancelable(false);
        dialogstatusAnswer.setContentView(R.layout.dialog_answer_status_read);

        TextView titleStatus = dialogstatusAnswer.findViewById(R.id.dasr_status);
        TextView titlebody = dialogstatusAnswer.findViewById(R.id.dasr_title);
        TextView content = dialogstatusAnswer.findViewById(R.id.dasr_answer);
        Button btnNext = dialogstatusAnswer.findViewById(R.id.dasr_btn_next);

        if (status) {
            titleStatus.setText(R.string.good);
            titleStatus.setTextColor(getResources().getColor(R.color.green));
            titlebody.setTextColor(getResources().getColor(R.color.green));
            if (percent == 100) {
                titlebody.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);
                titlebody.setText(getString(R.string.you_perfecte));
            } else {
                titlebody.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);
                String great_you = getString(R.string.great_you) + percent + "%";
                titlebody.setText(great_you);
            }
            btnNext.setBackgroundColor(getResources().getColor(R.color.green));
            btnNext.setText(R.string.next);
            numberReadWrong = 1;
            phayNotificationAudio(R.raw.right_answer);
        } else {
            titleStatus.setText(R.string.wrong);
            titleStatus.setTextColor(getResources().getColor(R.color.red));
            titlebody.setVisibility(View.VISIBLE);
            titlebody.setTextColor(getResources().getColor(R.color.red));
            content.setTextColor(getResources().getColor(R.color.red));
            content.setVisibility(View.VISIBLE);
            btnNext.setBackgroundColor(getResources().getColor(R.color.red));
            if (numberReadWrong >= 3) {
                btnNext.setText(R.string.next);
                isFailbutNext = true;
                numberReadWrong = 1;
                wrong.add(Current_Question);

                if (poinoflearn >= 10) {
                    poinoflearn--;
                }
            } else {
                titlebody.setText(R.string.try_again);
                String Just_a_little = getString(R.string.Just_a_little) + percent + "%";
                content.setText(Just_a_little);
                btnNext.setText(R.string.retry);
                isFailbutNext = false;
                numberReadWrong++;
            }
            phayNotificationAudio(R.raw.wrong_answer);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status) {
                    Current_Question++;
                    setQeestions();
                } else {
                    if (isFailbutNext) {
                        Current_Question++;
                        isFailbutNext = false;
                        setQeestions();
                    } else {
                        setQeestions();
                    }
                }
                dialogstatusAnswer.dismiss();
            }
        });

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

    private void mappingElements() {
        StartListen = findViewById(R.id.ar_listen);
        Listening = findViewById(R.id.ar_listening);
        btnSpeak = findViewById(R.id.ar_peaker);
        question = findViewById(R.id.ar_textquestion);
        btnNext = findViewById(R.id.ar_confirmAnswer);
        imgGit = findViewById(R.id.ar_icgit);

        // TextToSpeech
        mTextToSpeech = new TextToSpeech(this, this);
        wrong = new ArrayList<>();

        // mapping SpeechRecognizer and init Intent
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        SpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        SpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, LANGUAGE);
        SpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, TIMEOUT);
        SpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        setQeestions();
    }

    private void setQeestions() {
        if (Current_Question < questionList.size()) {
            question.setText(questionList.get(Current_Question).getContent());
            mTextToSpeech.setSpeechRate(1f);
            convertTextToSpeak(question.getText().toString());
        } else if (wrong.size() > 0) {
            question.setText(questionList.get(wrong.get(0)).getContent());
            wrong.remove(0);
            mTextToSpeech.setSpeechRate(1f);
            convertTextToSpeak(question.getText().toString());
        } else {
            completeLearn();
        }
        if (question.getText().length() > 30) {
            imgGit.setVisibility(View.GONE);
        } else {
            imgGit.setVisibility(View.VISIBLE);
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

    private void hideStartListen() {
        StartListen.setVisibility(View.GONE);
        Listening.setVisibility(View.VISIBLE);
    }

    private void showStartListen() {
        StartListen.setVisibility(View.VISIBLE);
        Listening.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        Dialog dialogconfirmexit = new Dialog(ReadActivity.this);
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
                if(mTextToSpeech.isSpeaking() || mTextToSpeech != null){
                    mTextToSpeech.shutdown();
                }
                finish();
            }
        });
        dialogconfirmexit.show();
        dialogconfirmexit.getWindow().setAttributes(lp);
    }

    private void convertTextToSpeak(String text) {
        String utteranceId = String.valueOf(this.hashCode());
        mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTextToSpeech.setLanguage(Locale.ENGLISH);
            convertTextToSpeak(question.getText().toString());
        }
    }
}