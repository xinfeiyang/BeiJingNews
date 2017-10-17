package com.security.news.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.security.news.R;
import com.security.news.util.SharedPreferenceUtil;

/**
 * 闪屏页;
 */
public class SplashActivity extends AppCompatActivity {

    private RelativeLayout rl_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rl_root=(RelativeLayout)findViewById(R.id.rl_root);
        //开启动画集合;
        startAnimation();
    }

    /*
     * 开启动画集合;
     */
    private void startAnimation(){
        AnimationSet animationSet=new AnimationSet(false);
        //旋转动画
        RotateAnimation rotate=new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(2000);//动画持续时间;
        rotate.setFillAfter(true);//保持结束时候状态;

        //缩放动画;
        ScaleAnimation scale=new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scale.setDuration(2000);
        scale.setFillAfter(true);

        //渐变动画
        AlphaAnimation alpha=new AlphaAnimation(0, 1);
        alpha.setDuration(2000);
        alpha.setFillAfter(true);

        animationSet.addAnimation(rotate);
        animationSet.addAnimation(scale);
        animationSet.addAnimation(alpha);

        //动画监听器;
        animationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean guideShow= (boolean) SharedPreferenceUtil.get(SplashActivity.this,"guideShow", false);
                //默认引导页没有展示过;
                if(!guideShow){
                    //动画播放结束后,结束当前页面,跳转至引导页页面;
                    Intent intent=new Intent(SplashActivity.this,GuideActivity.class);
                    startActivity(intent);
                }else{
                    //动画播放结束后,结束当前页面,跳转至主页面;
                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });

        rl_root.startAnimation(animationSet);
    }
}
