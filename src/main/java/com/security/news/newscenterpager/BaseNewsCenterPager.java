package com.security.news.newscenterpager;

import android.content.Context;
import android.view.View;
import com.security.news.bean.NewsBean;

/**
 * 新闻中心的基类页面;
 */
public abstract class BaseNewsCenterPager {

    /**
     * 全局上下文；
     */
    public final Context context;
    public View rootView;
    public String simpleName;

    public BaseNewsCenterPager(Context context){
        this.context=context;
        this.rootView=initView();
        this.simpleName=getClass().getSimpleName();
    }

    /**
     * 抽象的初始化View;
     * @return
     */
    public abstract View initView();

    /**
     * 初始化数据;
     */
    public void initData(){

    }
}
