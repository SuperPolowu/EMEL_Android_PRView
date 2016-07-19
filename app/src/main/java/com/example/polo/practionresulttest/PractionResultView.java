package com.example.polo.practionresulttest;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;
import android.util.Log;
import android.widget.ImageView;
import java.util.Date;
import android.widget.RelativeLayout;
import android.os.Bundle;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PractionResultView extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static int[] scoreLevelXPs = {0, 20, 45, 60, 175, 200, 250};
    private static int[] scoreLevelStars = {0, 6, 6, 6, 8, 8, 10};
    private static int[] ScoreLevelCups = {0, 1, 1, 1, 2, 2, 3};
    private Intent intent;
    private int level;
    private int average;
    private int accuracy;
    private int completion;
    public enum FaceEnum
    {
        averageFace,accuracyFace,completionFace;
    }
    @Bind(R.id.xp_Progress_Bg)
            ImageView xp_Progress_Bg;

    @Bind(R.id.xp_Progress)
            ImageView xp_Progress;

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

        setPractionResult(6);
    }


    private void setPractionResult(int p_Star) {

        Log.v(TAG, "setPractionResult");
        setAverage();
        setAccuracy();
        setCompletion();
        setXP(p_Star);
        setStarImage(p_Star,scoreLevelStars[level]);

    }
    private  void setTime(){

//        Date date = new Date(location.getTime());
//        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
//        mTimeText.setText("Time: " + dateFormat.format(date));

    }
    private  void setAverage(){
        String averageStr;
        if(average<50){
            averageStr="50以下";
        }else{
            averageStr=String.valueOf(average);
        }
        lb_Average.setText(averageStr);
        setFace(FaceEnum.averageFace,average);

    }
    private  void setAccuracy(){
        lb_Accuracy.setText(String.valueOf(accuracy));
        setFace(FaceEnum.accuracyFace,accuracy);
    }
    private  void setCompletion(){
        lb_Completion.setText(String.valueOf(completion));
        setFace(FaceEnum.completionFace,completion);
    }
    private  void setFace(FaceEnum p_FaceType,int p_number
    ){

        float score=0;
        if (p_number > 0 && (p_number - 50) > 0) {
            score = (p_number - 50) / 10;
            float remainsOfCompletion = (p_number - 50) % 10;
            if (remainsOfCompletion > 0) {
                score += 1;
            }

        } else {
            score = 0;

        }
       int tag=0;
        switch(p_FaceType) {

            case averageFace:
                tag=0;

                break;
            case accuracyFace:
                tag=10;
                break;
            case completionFace:
                tag=20;
                break;
            default:

        }
        int progress_Bg_Id = getResources().getIdentifier("progress"+(1+tag)+"_Bg", "id", getPackageName());
        int progress_Id = getResources().getIdentifier("progress"+(1+tag), "id", getPackageName());
        ImageView progress_Bg = (ImageView) findViewById(progress_Bg_Id);
        ImageView progress = (ImageView) findViewById(progress_Id);
        FrameLayout.LayoutParams progress_Bg_Params = (FrameLayout.LayoutParams)progress_Bg.getLayoutParams();
        FrameLayout.LayoutParams progress_Params = (FrameLayout.LayoutParams)progress.getLayoutParams();

        progress_Params.width=(progress_Bg_Params.width/5)*(int)score;
        for (int i = 1; i <= 5 ; i++) {
            Log.v("tag",String.valueOf(i+tag));
            int resId = getResources().getIdentifier("face_" + (i+tag), "id", getPackageName());
            ImageView face = (ImageView) findViewById(resId);
            if (score>=i){
                face.setImageResource(getResources().getIdentifier("face_0"+i+"_focus", "drawable", this.getPackageName()));
            }else{
                face.setImageResource(getResources().getIdentifier("face_0"+i+"_normal", "drawable", this.getPackageName()));
            }
        }



    }
    private  void setXP(int p_Star){
        int practiceTime = intent.getIntExtra("practiceTime", 0);
        int total_PracticeTime = intent.getIntExtra("total_practiceTime", 0);
        int totalXPScore=total_PracticeTime+practiceTime;
        int thresholdXPScore=scoreLevelXPs[level]*60;
        if (totalXPScore>thresholdXPScore)totalXPScore=thresholdXPScore;
        int xp_Percentage=(int) ((((float)totalXPScore) / ((float)thresholdXPScore)) * 100);
        Log.v("totalXPScore",String.valueOf(totalXPScore));
        Log.v("thresholdXPScore",String.valueOf(thresholdXPScore));
        Log.v("xp_Percentage",String.valueOf(xp_Percentage));
        TextView lb_XP = (TextView)findViewById(R.id.lb_Xp);
        lb_XP.setText(String.valueOf(totalXPScore) +"/"+String.valueOf(thresholdXPScore));
        FrameLayout.LayoutParams xp_Progress_Bg＿Params = (FrameLayout.LayoutParams)xp_Progress_Bg.getLayoutParams();
        FrameLayout.LayoutParams xp_Progress_Params = (FrameLayout.LayoutParams)xp_Progress.getLayoutParams();
        FrameLayout.LayoutParams xp_Character_Params = (FrameLayout.LayoutParams)xp_Character.getLayoutParams();
        xp_Progress_Params.width=(int)(((float)xp_Progress_Bg＿Params.width/100)*xp_Percentage);
        xp_Character.setX(xp_Progress_Params.width-(xp_Character_Params.width/2+26));
        Log.v("xp_Character X",String.valueOf(xp_Character.getX()));
        Log.v("xp_Progress X",String.valueOf(xp_Progress.getX()));
        int cap_Normal_ID = getResources().getIdentifier("cup_"+ScoreLevelCups[level]+"_hide", "drawable", this.getPackageName());
        int cap_Focus_ID = getResources().getIdentifier("cup_"+ScoreLevelCups[level]+"_normal", "drawable", this.getPackageName());
        if (p_Star == scoreLevelStars[level]&& totalXPScore==thresholdXPScore){
            xp_Cup.setImageResource(cap_Focus_ID);

        }else{
            xp_Cup.setImageResource(cap_Normal_ID);

        }


    }
    private  void setStarImage(int p_Star , int p_Max_Star){
        Log.v(TAG, "setStarImage");
        int star_hide_ID = getResources().getIdentifier("star_hide", "drawable", this.getPackageName());
        int star_normal_ID = getResources().getIdentifier("star_normal", "drawable", this.getPackageName());
        for (int i = 1; i <= 9 ; i++) {
            if (p_Star>=i){
                int resId = getResources().getIdentifier("star_" + i, "id", getPackageName());

                ImageView star = (ImageView) findViewById(resId);
                star.setImageResource(star_normal_ID);
            }else{
                int resId = getResources().getIdentifier("star_" + i, "id", getPackageName());

                ImageView star = (ImageView) findViewById(resId);
                star.setImageResource(star_hide_ID);
                if (p_Max_Star<i){

                    star.setVisibility(View.INVISIBLE);

                }
            }
        }

    }
}
