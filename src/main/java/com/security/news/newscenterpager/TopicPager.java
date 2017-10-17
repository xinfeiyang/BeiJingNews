package com.security.news.newscenterpager;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.security.news.bean.NewsBean;
import com.security.news.util.UIUtil;

/**
 * 专题页面;
 */
public class TopicPager extends BaseNewsCenterPager {

    private NewsBean.DataBean data;

    public TopicPager(Context context,NewsBean.DataBean data) {
        super(context);
        this.data=data;
    }

    @Override
    public View initView() {
       TextView textView=new TextView(UIUtil.getContext());
        textView.setText(simpleName);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
    }
}
