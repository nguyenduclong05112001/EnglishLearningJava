package com.example.englishlearn.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.englishlearn.R;
import com.example.englishlearn.api.APIConst;
import com.example.englishlearn.handlerothers.ConstSendData;
import com.example.englishlearn.models.Story;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContentOfStoryActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private static int STATUS_LISTEN = 1;
    private LinearLayout layoutContent;
    private Button btnNext;
    private ImageView imgAvatar, icSpeak;
    private TextView lbName;
    private Story mStory;
    private TextToSpeech textToSpeech;
    private int Current_Item = 0;
    private List<String> listContent;
    private boolean isSlowAline;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_of_story);

        checkIntent();
    }

    private void checkIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        mStory = (Story) bundle.getSerializable(ConstSendData.KEY_STORY_OBJECT);
        if (mStory == null) {
            return;
        }
        initTextToSpeech();
        mappingElements();
        eventElements();
    }

    private void initTextToSpeech() {
        textToSpeech = new TextToSpeech(this, this, "com.google.android.tts");
    }

    private void mappingElements() {
        layoutContent = findViewById(R.id.cosa_containcontent);

        String[] result = mStory.getContent().trim().split("\\.");
        listContent = new ArrayList<>(Arrays.asList(result));
        mScrollView = findViewById(R.id.cosa_scrollview);

        btnNext = findViewById(R.id.cosa_btnnext);
        imgAvatar = findViewById(R.id.cosa_imgstory);
        lbName = findViewById(R.id.cosa_name);
        icSpeak = findViewById(R.id.cosa_ic_speaker);
        Glide.with(this).load(APIConst.baseUrl + APIConst.storeImage + mStory.getAvatar()).into(imgAvatar);
        lbName.setText(mStory.getName());
    }

    private void eventElements() {
        icSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (STATUS_LISTEN == 1) {
                    textToSpeech.setSpeechRate(1f);
                    convertTextToSpeak(mStory.getName());
                    STATUS_LISTEN = 2;
                } else {
                    textToSpeech.setSpeechRate(0.3f);
                    convertTextToSpeak(mStory.getName());
                    STATUS_LISTEN = 1;
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSlowAline = false;
                textToSpeech.setSpeechRate(1f);
                setViewInLayoutContent();
            }
        });

        layoutContent.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                statusButtonNext(R.color.gray,false);
            }

            @Override
            public void onDone(String s) {
                statusButtonNext(R.color.green,true);
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    private void statusButtonNext(int color, boolean status) {
        btnNext.setBackgroundColor(getResources().getColor(color));
        btnNext.setEnabled(status);
    }

    private void setViewInLayoutContent() {
        if (Current_Item < listContent.size()) {
            convertTextToSpeak(listContent.get(Current_Item).trim());
            LinearLayout layout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) convertDPtoPX(this, 20), (int) convertDPtoPX(this, 10), (int) convertDPtoPX(this, 20), (int) convertDPtoPX(this, 10));
            layout.setLayoutParams(layoutParams);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            TextView content = new TextView(this);
            LinearLayout.LayoutParams layoutParamsItemContent = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParamsItemContent.setMargins((int) convertDPtoPX(this, 10), 0, 0, 0);
            content.setLayoutParams(layoutParamsItemContent);
            content.setText(listContent.get(Current_Item).trim());
            content.setTextColor(getResources().getColor(R.color.black));
            content.setTextSize(convertDPtoPX(this, 10));

            CircleImageView itemSpeacker = new CircleImageView(this);
            LinearLayout.LayoutParams layoutParamsItemSpeak = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParamsItemSpeak.width = (int) convertDPtoPX(this, 40);
            layoutParamsItemSpeak.height = (int) convertDPtoPX(this, 40);
            layoutParamsItemSpeak.setMargins(0, 0, (int) convertDPtoPX(this, 15), 0);
            itemSpeacker.setLayoutParams(layoutParamsItemSpeak);
            itemSpeacker.setImageDrawable(getDrawable(R.drawable.ic_speaker));
            itemSpeacker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isSlowAline) {
                        textToSpeech.setSpeechRate(0.3f);
                        convertTextToSpeak(content.getText().toString());
                        isSlowAline = false;
                    } else {
                        textToSpeech.setSpeechRate(1f);
                        convertTextToSpeak(content.getText().toString());
                        isSlowAline = true;
                    }
                }
            });

            layout.addView(itemSpeacker);
            layout.addView(content);
            layoutContent.addView(layout);
            Current_Item++;
        } else {
            toAcityvityQuestion(QuestionOfStoryActivity.class, mStory.getId());
        }
    }

    private void toAcityvityQuestion(Class<?> to, int idStory) {
        Intent intent = new Intent(this, to);
        intent.putExtra(ConstSendData.KEY_SEND_ID_OF_STORY, idStory);
        startActivity(intent);
        finish();
    }

    private static float convertDPtoPX(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    private void convertTextToSpeak(String text) {
        String utteranceId = String.valueOf(this.hashCode());
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.ENGLISH);
            convertTextToSpeak(mStory.getName());
        }
    }

    @Override
    public void onBackPressed() {
        Dialog dialogconfirmexit = new Dialog(ContentOfStoryActivity.this);
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
                if (textToSpeech.isSpeaking() || textToSpeech != null){
                    textToSpeech.shutdown();
                }
                finish();
            }
        });
        dialogconfirmexit.show();
        dialogconfirmexit.getWindow().setAttributes(lp);
    }
}