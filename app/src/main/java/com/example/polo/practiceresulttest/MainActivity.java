package com.example.polo.practiceresulttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import com.example.polo.practionresulttest.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.button_1)
    void onButtonClick() {

        Intent intent = new Intent(this, PracticeResultView.class);
        intent.putExtra("level", 6);
        intent.putExtra("total_practiceTime", 11900);
        intent.putExtra("average", 77);
        intent.putExtra("accuracy", 77);
        intent.putExtra("completion", 100);
        intent.putExtra("bpm", 128);
        intent.putExtra("duration", 96);
        intent.putExtra("score_Title", "巴赫初步鋼琴曲集上冊No.1小步舞曲");
        intent.putExtra("composer", "巴赫");
        intent.putExtra("record_path", "....");
        intent.putExtra("star",10);
        startActivity(intent);

    }
}
