package com.security.news.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.security.news.R;
import com.security.news.fragment.ContentFragment;
import com.security.news.fragment.MenuFragment;

/**
 * 主页Activity;
 */
public class MainActivity extends FragmentActivity{

    public DrawerLayout drawerLayout;
    public static final String FRAMELAYOUT_MENU="framelayout_menu";
    public static final String FRAMELAYOUT_CONTENT="framelayout_content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        initFragment();
    }

    /**
     * 初始化Fragment,将fragment数据填充布局文件;
     */
    private void initFragment() {
        //获取fragment管理器
        FragmentManager fm = getSupportFragmentManager();
        //打开事务
        FragmentTransaction transaction = fm.beginTransaction();
        //把内容显示至帧布局
        transaction.replace(R.id.fl_content, new ContentFragment(),FRAMELAYOUT_CONTENT);
        transaction.replace(R.id.fl_menu,new MenuFragment(),FRAMELAYOUT_MENU);
        //提交
        transaction.commit();
    }
    /*
	 * 根据Tag获取到相应的Fragment;
	 */
    public Fragment getFragmentFromTag(String tag){
        //获取fragment管理器
        FragmentManager fm = getSupportFragmentManager();
        return fm.findFragmentByTag(tag);
    }

    /**
     * 控制开关窗口的打开、关闭;
     */
    public void toggleDrawer(){
        if(drawerLayout.isDrawerOpen(Gravity.START)){
            drawerLayout.closeDrawer(Gravity.START);
        }else{
            drawerLayout.openDrawer(Gravity.START);
        }
    }

    /**
     * 设定DrawerLayer是否可以划出侧框;
     * @param isDrag:true 打开手势滑动;false 禁止手势滑动;
     */
    public void setDrawerDragged(boolean isDrag){
        if(isDrag){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }else{
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

}
