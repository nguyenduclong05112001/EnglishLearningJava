package com.example.englishlearn.activities;

import static com.example.englishlearn.handlerothers.ConstSendData.KEY_SEND_NUMBER_QUESTION_FALSE;
import static com.example.englishlearn.handlerothers.ConstSendData.KEY_SEND_OBJECT_TEST_DONE;
import static com.example.englishlearn.handlerothers.ConstSendData.KEY_SEND_TOTAL_NUMBER_QUESTION;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.englishlearn.R;
import com.example.englishlearn.api.APIConst;
import com.example.englishlearn.models.TheTestLocal;
import com.example.englishlearn.models.TheTestOfUsers;

public class CompleteTheTestOfUserActivity extends AppCompatActivity {
    private TheTestOfUsers theTest;
    private int numwrong,total;
    private TextView tvid,tvname,tvdate,tvuser,tvtotal,tvwwrong;
    private ImageView image;
    private Button btnComplete;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_the_test_of_user);

        checkIntentSent();
    }

    private void checkIntentSent() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            theTest = (TheTestOfUsers) bundle.getSerializable(KEY_SEND_OBJECT_TEST_DONE);
            numwrong = bundle.getInt(KEY_SEND_NUMBER_QUESTION_FALSE);
            total = bundle.getInt(KEY_SEND_TOTAL_NUMBER_QUESTION);
            mappingElements();
            setDataOnUI();
            eventElements();
        }
    }

    private void mappingElements() {
        tvid = findViewById(R.id.acttou_lbid);
        tvname = findViewById(R.id.acttou_lbname);
        tvdate = findViewById(R.id.acttou_lbdate);
        tvuser = findViewById(R.id.acttou_lbuser);
        tvtotal = findViewById(R.id.acttou_lbtotal);
        tvwwrong = findViewById(R.id.acttou_lbwrong);
        image = findViewById(R.id.acttou_image);
        btnComplete = findViewById(R.id.acttou_btnNext);
        mediaPlayer = MediaPlayer.create(this, R.raw.complete_learn);
    }

    private void setDataOnUI() {
        String valuesofid = tvid.getText().toString() + theTest.getId();
        String valuesofname = tvname.getText().toString() + theTest.getName();
        String valuesofuser = tvuser.getText().toString() + theTest.getIduser();
        String valuesofdate = tvdate.getText().toString() + theTest.getDatecreate();
        String valuesoftotalText = tvtotal.getText().toString() + total;
        String valuesofwrongText = tvwwrong.getText().toString() + numwrong;
        tvid.setText(valuesofid);
        tvname.setText(valuesofname);
        tvuser.setText(valuesofuser);
        tvdate.setText(valuesofdate);
        Glide.with(this).load(APIConst.baseUrl + APIConst.storeImage + theTest.getAvatar()).error(R.drawable.avatardefault).into(image);
        tvtotal.setText(valuesoftotalText);
        tvwwrong.setText(valuesofwrongText);
        mediaPlayer.start();
    }

    private void eventElements() {
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        if (mediaPlayer.isPlaying() || mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer.isPlaying() || mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onBackPressed();
    }
}