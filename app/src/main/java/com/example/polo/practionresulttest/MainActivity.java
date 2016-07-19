package com.example.polo.practionresulttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.content.Intent;
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


        Intent intent = new Intent(this, PractionResultView.class);
        intent.putExtra("practiceTime", 10);
        intent.putExtra("level", 1);
        intent.putExtra("total_practiceTime", 1190);
        intent.putExtra("average", 78);
        intent.putExtra("accuracy", 60);
        intent.putExtra("completion", 100);

        startActivity(intent);
    }
}
