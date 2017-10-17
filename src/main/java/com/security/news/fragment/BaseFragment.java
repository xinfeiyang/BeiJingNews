package com.security.news.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 基类Fragment;
 */
public abstract class BaseFragment extends Fragment {

    public Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
    }

    //处理fragement的布局;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initViews();
    }

    //fragment所依附的activity创建完成;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //activity加载完成时初始化数据;
        initData();
    }

    //activity加载完成时初始化数据;
    public void initData() {

    }

    //子类必须实现初始化布局的方法;
    public abstract View initViews();

}
