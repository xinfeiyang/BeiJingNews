package com.security.news.newscenterpager;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.security.news.bean.NewsBean;
import com.security.news.util.UIUtil;

/**
 * 互动页面;
 */
public class IntercoursePager extends BaseNewsCenterPager {

    private NewsBean.DataBean data;

    public IntercoursePager(Context context,NewsBean.DataBean data) {
        super(context);
        this.data=data;
    }

    @Override
    public View initView() {
        TextView tv = new TextView(UIUtil.getContext());
        tv.setGravity(Gravity.CENTER);
        tv.setText(simpleName);
        return tv;
    }

    @Override
    public void initData() {
        super.initData();
    }
}
