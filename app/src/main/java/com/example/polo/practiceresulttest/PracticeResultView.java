package com.example.polo.practiceresulttest;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;
import android.widget.ImageView;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import java.util.Date;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.media.MediaPlayer;

import com.example.polo.practionresulttest.R;

import java.text.SimpleDateFormat;

public class PracticeResultView extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static int[] scoreLevelXPs = {0, 20, 45, 60, 175, 200, 250};
    private static int[] scoreLevelStars = {0, 6, 6, 6, 8, 8, 10};
    private static int[] ScoreLevelCups = {0, 1, 1, 1, 2, 2, 3};
    private Intent intent;
    private int level;
    private int average;
    private int accuracy;
    private int completion;
    private boolean isFocused;
    private float characterDistance;
    private MediaPlayer media;
    private AnimationDrawable animationCharacter;
    public enum FaceEnum {
        averageFace, accuracyFace, completionFace;
    }

    @Bind(R.id.xp_Progress_Bg)
    ImageView xp_Progress_Bg;

    @Bind(R.id.xp_Progress)
    FrameLayout xp_Progress;

    @Bind(R.id.xp_Character)
    ImageView xp_Character;

    @Bind(R.id.xp_Cup)
    ImageView xp_Cup;

    @Bind(R.id.lb_Average)
    TextView lb_Average;

    @Bind(R.id.lb_Accuracy)
    TextView lb_Accuracy;

    @Bind(R.id.lb_Completion)
    TextView lb_Completion;

    @Bind(R.id.icon_LV)
    ImageView icon_LV;
    @Nullable
    @Bind(R.id.btn_play)
    ImageButton btn_play;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praction_result_view);
        ButterKnife.bind(this);
        intent = getIntent();
        level = intent.getIntExtra("level", 0);
        average = intent.getIntExtra("average", 0);
        accuracy = intent.getIntExtra("accuracy", 0);
        completion = intent.getIntExtra("completion", 0);

        startPracticeResultSeting();
    }

    @Nullable
    @OnClick(R.id.btn_Close)
    void onbtn_CloseClick() {

        finish();
    }

    @Nullable
    @OnClick(R.id.btn_play)
    void onbtn_playClick() {
        isFocused = !isFocused;
        if (isFocused == true) {
            btn_play.setImageResource(R.drawable.icon_pause);
        } else {
            btn_play.setImageResource(R.drawable.icon_play);
        }

    }

    private void startPracticeResultSeting(){

        Log.v(TAG, "setPracticeResult");
        ((TextView) findViewById(R.id.lb_Score_Title)).setText(intent.getStringExtra("score_Title"));
        ((TextView) findViewById(R.id.lb_Composer)).setText(intent.getStringExtra("composer"));
        setTime();
        //設定旗子
        int lv_ID = getResources().getIdentifier("icon_lv_" + level, "drawable", this.getPackageName());
        icon_LV.setImageResource(lv_ID);
        setXP(intent.getIntExtra("star", 0));
        setStarImage(intent.getIntExtra("star", 0), scoreLevelStars[level]);
        //設定播放按鈕

        if (intent.getStringExtra("record_path").equals("")) {
            btn_play.setImageResource(R.drawable.icon_play_off);
        } else {
            btn_play.setImageResource(R.drawable.icon_play);
        }

        initMediaPlay();

        //設定Duration
        TextView lb_Duration = (TextView) findViewById(R.id.lb_Duration);
        lb_Duration.setText(formatDuration(intent.getIntExtra("duration", 0)));
        //設定BPM
        TextView lb_BPM = (TextView) findViewById(R.id.lb_BPM);
        lb_BPM.setText(String.valueOf(intent.getIntExtra("bpm", 0)));

        setAccuracy();
        setCompletion();
        setAverage();
    }

    private void initMediaPlay() {
        try{
            media.reset();
            media.setDataSource(intent.getStringExtra("record_path"));
            media.prepare();
        }catch(Exception e){

        }
    }

    public static String formatDuration(int duration) {
        long seconds = duration;
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%02d:%02d",

                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    private void setTime() {


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd  a hh:mm");
        String now = formatter.format(new Date());
        ((TextView) findViewById(R.id.lb_Time)).setText(now);

    }

    private void setAverage() {

        if (average < 50) {
            lb_Average.setText(getString(R.string.below50));
        } else {
            lb_Average.setText(String.valueOf(average));
        }




        setFace(FaceEnum.averageFace, average);
        setProgress(FaceEnum.averageFace, average);



    }

    private void setAccuracy() {

        if (accuracy<50){
            lb_Accuracy.setText(getString(R.string.below50));
        }else{
            lb_Accuracy.setText(String.valueOf(accuracy));
        }

        setFace(FaceEnum.accuracyFace, accuracy);
        setProgress(FaceEnum.accuracyFace, accuracy);
    }

    private void setCompletion() {
        if (completion<50){
            lb_Completion.setText(getString(R.string.below50));
        }else{
            lb_Completion.setText(String.valueOf(completion));
        }

        setFace(FaceEnum.completionFace, completion);
        setProgress(FaceEnum.completionFace, completion);
    }

    private void setProgress(FaceEnum p_FaceType , int p_number){
        float score = 0;
        if (p_number > 0 && (p_number - 50) > 0) {
            score = (p_number - 50) / 10;
            float remainsOfCompletion = (p_number - 50) % 10;
            if (remainsOfCompletion > 0) {
                score += 1;
            }

        } else {
            score = 0;

        }
        int tag = 0;
        switch (p_FaceType) {

            case averageFace:
                tag = 0;
                break;
            case accuracyFace:
                tag = 10;
                break;
            case completionFace:
                tag = 20;
                break;
            default:

        }
        final int time =(int)score*500;
        final int length =(int)score;
        int progress_Bg_Id = getResources().getIdentifier("progress" + (1 + tag) + "_Bg", "id", getPackageName());
        int progress_Id = getResources().getIdentifier("progress" + (1 + tag), "id", getPackageName());
        final ImageView progress_Bg = (ImageView) findViewById(progress_Bg_Id);
        final ImageView progress = (ImageView) findViewById(progress_Id);
        ViewTreeObserver vto = progress_Bg.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width  = progress_Bg.getMeasuredWidth();
                ResizeWidthAnimation anim = new ResizeWidthAnimation(progress, (width / 5) * length);
                anim.setDuration(time);
                progress.startAnimation(anim);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    progress_Bg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    progress_Bg.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

    }

    private void setFace(FaceEnum p_FaceType, int p_number) {
        String[] scoreStringsArray=new String[]{
                getString(R.string.rating1),
                getString(R.string.rating2),
                getString(R.string.rating3),
                getString(R.string.rating4),
                getString(R.string.rating5),
                getString(R.string.rating6)};
        float score = 0;
        if (p_number > 0 && (p_number - 50) > 0) {
            score = (p_number - 50) / 10;
            float remainsOfCompletion = (p_number - 50) % 10;
            if (remainsOfCompletion > 0) {
                score += 1;
            }

        } else {
            score = 0;

        }
        int tag = 0;
        switch (p_FaceType) {

            case averageFace:
                tag = 0;
                TextView lb_Meessage =(TextView)  findViewById(R.id.lb_Meessage);
                lb_Meessage.setText(scoreStringsArray[(int) score]);
                break;
            case accuracyFace:
                tag = 10;
                break;
            case completionFace:
                tag = 20;
                break;
            default:

        }




        for (int i = 1; i <= 5; i++) {

            int resId = getResources().getIdentifier("face_" + (i + tag), "id", getPackageName());
            ImageView face = (ImageView) findViewById(resId);
            if (score >= i) {
                face.setImageResource(getResources().getIdentifier("face_0" + i + "_focus", "drawable", this.getPackageName()));
            } else {
                face.setImageResource(getResources().getIdentifier("face_0" + i + "_normal", "drawable", this.getPackageName()));
            }
        }


    }

    private void setXP(int p_Star) {
        int practiceTime = intent.getIntExtra("duration", 0);
        int total_PracticeTime = intent.getIntExtra("total_practiceTime", 0);
        int totalXPScore = total_PracticeTime + practiceTime;
        int thresholdXPScore = scoreLevelXPs[level] * 60;
        if (totalXPScore > thresholdXPScore) totalXPScore = thresholdXPScore;
        int xp_Percentage = (int) ((((float) totalXPScore) / ((float) thresholdXPScore)) * 100);

        TextView lb_XP = (TextView) findViewById(R.id.lb_Xp);
        lb_XP.setText(String.valueOf(totalXPScore) + "/" + String.valueOf(thresholdXPScore));
        FrameLayout.LayoutParams xp_Progress_Bg＿Params = (FrameLayout.LayoutParams) xp_Progress_Bg.getLayoutParams();
        FrameLayout.LayoutParams xp_Character＿Params = (FrameLayout.LayoutParams) xp_Character.getLayoutParams();
        ResizeWidthAnimation anim = new ResizeWidthAnimation(xp_Progress, (int) (((float) xp_Progress_Bg＿Params.width / 100) * xp_Percentage));
        anim.setDuration(1000);
        xp_Progress.startAnimation(anim);
        if (thresholdXPScore==1){
            characterDistance=xp_Progress_Bg＿Params.width-(xp_Character＿Params.width+5);
        }else{

            characterDistance = (((float) xp_Progress_Bg＿Params.width / 100) * xp_Percentage )-(xp_Character＿Params.width+5);
        }

        if (characterDistance < 0) characterDistance = 0;

        xp_Character.setBackgroundResource(R.drawable.character_progress_animation);
        animationCharacter = (AnimationDrawable) xp_Character.getBackground();
        Log.d("xp_Percentage",String.valueOf(xp_Percentage));
        Log.d("width",String.valueOf(xp_Progress_Bg＿Params.width));
        Log.d("鱷魚跑哪去",String.valueOf(characterDistance));
        Animation am = new TranslateAnimation(0.0f, characterDistance, 0.0f, 0.0f);
        am.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                animationCharacter.stop();
            }
        });
        am.setDuration(2000);
        am.setFillAfter(true);
        xp_Character.setAnimation(am);
        am.startNow();
        animationCharacter.start();


        int cap_Normal_ID = getResources().getIdentifier("cup_" + ScoreLevelCups[level] + "_hide", "drawable", this.getPackageName());
        int cap_Focus_ID = getResources().getIdentifier("cup_" + ScoreLevelCups[level] + "_normal", "drawable", this.getPackageName());
        if (p_Star == scoreLevelStars[level] && totalXPScore == thresholdXPScore) {
            xp_Cup.setImageResource(cap_Focus_ID);

        } else {
            xp_Cup.setImageResource(cap_Normal_ID);

        }


    }

    private void setStarImage(int p_Star, int p_Max_Star) {
        Log.v(TAG, "setStarImage");
        int star_hide_ID = getResources().getIdentifier("star_hide", "drawable", this.getPackageName());
        int star_normal_ID = getResources().getIdentifier("star_normal", "drawable", this.getPackageName());
        for (int i = 1; i <=10; i++) {
            if (p_Star >= i) {
                int resId = getResources().getIdentifier("star_" + i, "id", getPackageName());
                ImageView star = (ImageView) findViewById(resId);
                star.setImageResource(star_normal_ID);
            } else {
                int resId = getResources().getIdentifier("star_" + i, "id", getPackageName());

                ImageView star = (ImageView) findViewById(resId);
                star.setImageResource(star_hide_ID);
                if (p_Max_Star < i) {
                    star.setVisibility(View.GONE);

                }
            }
        }

    }

    //改變寬度的動畫效果
    public class ResizeWidthAnimation extends Animation {
        private int mWidth;
        private int mStartWidth;
        private View mView;

        public ResizeWidthAnimation(View view, int width) {
            mView = view;
            mWidth = width;
            mStartWidth = view.getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            mView.getLayoutParams().width = mStartWidth + (int) ((mWidth - mStartWidth) * interpolatedTime);
            mView.requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

}
