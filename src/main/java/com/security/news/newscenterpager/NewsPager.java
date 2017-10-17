package com.security.news.newscenterpager;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.security.news.R;
import com.security.news.activity.MainActivity;
import com.security.news.bean.NewsBean;
import com.security.news.tab.TabPager;
import com.security.news.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻页面;
 */
public class NewsPager extends BaseNewsCenterPager {

    private NewsBean.DataBean data;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<NewsBean.DataBean.ChildrenBean> datas;
    private List<TabPager> pagers=new ArrayList<>();

    public NewsPager(Context context,NewsBean.DataBean data) {
        super(context);
        this.data=data;
    }

    @Override
    public View initView() {
        View view=View.inflate(context,R.layout.pager_news,null);
        tabLayout = (TabLayout)view.findViewById(R.id.tl_tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.vp_viewPager);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        datas=data.getChildren();
        //设置12个Tab页面;
        for(int i=0;i<datas.size();i++){
            pagers.add(new TabPager(context,datas.get(i)));
        }
        //tabLayout与ViewPager设置关联关系；
        viewPager.setAdapter(new MyAdapter());
        tabLayout.setupWithViewPager(viewPager);
        pagers.get(0).initData();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //设置只有第一个Tab可以划出;
                int position=tab.getPosition();
                MainActivity activity= (MainActivity) context;
                if(position==0){
                    activity.setDrawerDragged(true);
                }else{
                    activity.setDrawerDragged(false);
                }

                //初始化数据；
                TabPager pager = pagers.get(position);
                pager.initData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            if(pagers!=null&&pagers.size()>0){
                return pagers.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabPager pager = pagers.get(position);
            View view=pager.rootView;
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.i("LOG", "当前位置:"+position);
            position=position%datas.size();
            return datas.get(position).getTitle();
        }
    }
}
