package com.security.news.newscenterpager;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.security.news.bean.NewsBean;
import com.security.news.util.UIUtil;

/**
 * 投票页面;
 */
public class VotePager extends BaseNewsCenterPager {

    private NewsBean.DataBean data;

    public VotePager(Context context,NewsBean.DataBean data) {
        super(context);
        this.data=data;
    }

    @Override
    public View initView() {
        TextView tv = new TextView(UIUtil.getContext());
        tv.setText(simpleName);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    @Override
    public void initData() {
        super.initData();
    }
}
