package com.security.news.fragment;

import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.security.news.R;
import com.security.news.activity.MainActivity;
import com.security.news.pager.BasePager;
import com.security.news.pager.ShoppingCartPager;
import com.security.news.pager.HomePager;
import com.security.news.pager.NewsCenterPager;
import com.security.news.pager.SettingPager;
import com.security.news.pager.ShopingCenterPager;
import com.security.news.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页Fragment;
 */
public class ContentFragment extends BaseFragment {

    private NoScrollViewPager viewPager;
    private RadioGroup radioGroup;
    private List<BasePager> pagers=new ArrayList<>();

    @Override
    public View initViews() {
        View view = View.inflate(activity, R.layout.fragment_content, null);
        viewPager = (NoScrollViewPager) view.findViewById(R.id.viewPager);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        return view;
    }


    @Override
    public void initData() {
        super.initData();
        //默认勾选首页;
        radioGroup.check(R.id.home);
        //禁用侧边栏可以划出;
        ((MainActivity)activity).setDrawerDragged(false);

        //RadioGroup切换;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.home:
                        //设置当前选中页面,并去除更换页面时候的动画;
                        viewPager.setCurrentItem(0,false);
                        ((MainActivity)activity).setDrawerDragged(false);
                        break;

                    case R.id.news:
                        viewPager.setCurrentItem(1,false);
                        ((MainActivity)activity).setDrawerDragged(true);
                        break;

                    case R.id.smartservice:
                        viewPager.setCurrentItem(2,false);
                        ((MainActivity)activity).setDrawerDragged(false);
                        break;

                    case R.id.govaffairs:
                        viewPager.setCurrentItem(3,false);
                        ((MainActivity)activity).setDrawerDragged(false);
                        break;

                    case R.id.setting:
                        viewPager.setCurrentItem(4,false);
                        ((MainActivity)activity).setDrawerDragged(false);
                        break;
                }
            }
        });

        //初始化各个页面;
        pagers.add(new HomePager(activity));
        pagers.add(new NewsCenterPager(activity));
        pagers.add(new ShopingCenterPager(activity));
        pagers.add(new ShoppingCartPager(activity));
        pagers.add(new SettingPager(activity));

        //ViewPager设置适配器；
        viewPager.setAdapter(new MyAdapter());
        //初始化首页数据;
        pagers.get(0).initData();
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                pagers.get(position).initData();
            }
        });
    }

    /**
     *返回NewsCenterPager;
     * @return
     */
    public NewsCenterPager getNewsCenterPager(){
        if(pagers!=null){
            return (NewsCenterPager) pagers.get(1);
        }
        return null;
    }

    /**
     * NoScrollerView的适配器;
     */
    private class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            if(pagers!=null&&pagers.size()>0){
                return pagers.size();
            }
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager=pagers.get(position);
            View view=pager.rootView;
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }

}
