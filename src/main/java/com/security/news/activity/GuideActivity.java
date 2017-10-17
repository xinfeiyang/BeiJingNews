package com.security.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.security.news.R;
import com.security.news.util.DensityUtil;
import com.security.news.util.LogUtil;
import com.security.news.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Feng on 2017/9/19.
 */
public class GuideActivity extends AppCompatActivity {

    private ViewPager vp_guide;
    private LinearLayout guide_dot;//小圆点集合的父控件;
    private RelativeLayout rl_dots;
    private View red_dot;
    private Button btn_start;

    private int dot_dot_width;//小灰 点与小灰点之间的距离;
    private static final int[] guides=new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    private List<View> list=new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        vp_guide=(ViewPager)findViewById(R.id.vp_guide);
        rl_dots=(RelativeLayout)findViewById(R.id.rl_dot);
        guide_dot=(LinearLayout)findViewById(R.id.guide_dot);
        btn_start=(Button) findViewById(R.id.btn_start);

        initViews();

        btn_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferenceUtil.put(GuideActivity.this,"guideShow",true);
                //跳转至主页面;
                Intent intent=new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        vp_guide.setAdapter(new MyAdapter());
        vp_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if(position==list.size()-1){
                    btn_start.setVisibility(View.VISIBLE);
                }else{
                    btn_start.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                int len = (int) (dot_dot_width*positionOffset) + position
                        *dot_dot_width;
                System.out.println("lenth:"+len);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)red_dot
                        .getLayoutParams();// 获取当前红点的布局参数
                System.out.println("params:"+params);
                params.leftMargin = len;// 设置左边距
                red_dot.setLayoutParams(params);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initViews() {
        red_dot=new View(this);
        red_dot.setBackgroundResource(R.drawable.shape_dot_red);
        int width= DensityUtil.dp2px(GuideActivity.this,20);
        RelativeLayout.LayoutParams rlparams=new RelativeLayout.LayoutParams(width,width);
        red_dot.setLayoutParams(rlparams);
        System.out.println("rl_dots:"+rl_dots);
        rl_dots.addView(red_dot);

        for(int i=0;i<guides.length;i++){
            View view=new View(this);
            view.setBackgroundResource(guides[i]);
            list.add(view);
        }

        for(int i=0;i<list.size();i++){
            View view=new View(this);
            view.setBackgroundResource(R.drawable.shape_dot_gray);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,width);
            if(i>0){
                params.leftMargin=width;
            }
            view.setLayoutParams(params);
            guide_dot.addView(view);
        }

        guide_dot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int left=guide_dot.getChildAt(0).getLeft();
                int left1=guide_dot.getChildAt(1).getLeft();
                dot_dot_width=left1-left;
                LogUtil.i("dot_dot_width:"+dot_dot_width);
                guide_dot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
