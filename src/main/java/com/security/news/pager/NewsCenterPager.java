package com.security.news.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.security.news.R;
import com.security.news.activity.MainActivity;
import com.security.news.bean.NewsBean;
import com.security.news.fragment.MenuFragment;
import com.security.news.newscenterpager.BaseNewsCenterPager;
import com.security.news.newscenterpager.IntercoursePager;
import com.security.news.newscenterpager.NewsPager;
import com.security.news.newscenterpager.PhotoPager;
import com.security.news.newscenterpager.TopicPager;
import com.security.news.newscenterpager.VotePager;
import com.security.news.util.Constants;
import com.security.news.util.DiskUtil;
import com.security.news.util.LogUtil;
import com.security.news.util.TextUtil;
import com.security.news.util.UIUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * 新闻中心Pager ;
 */
public class NewsCenterPager extends BasePager implements View.OnClickListener {

    //此处的context就是MainActivity；
    private Context context;
    public static String fileName="categories.json";
    private List<BaseNewsCenterPager> pagers= new ArrayList<>();;
    private List<NewsBean.DataBean> data;

    public NewsCenterPager(Context context) {
        super(context);
        this.context = context;
        ib_swich_list_grid.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();

        ib_menu.setVisibility(View.VISIBLE);
        LogUtil.i(simpleName);

        String json=DiskUtil.readTextFromCacheFile(UIUtil.getContext(),fileName);
        if(!TextUtils.isEmpty(json)){
            processData(json);
        }else{
            getDataFromNet();
        }

       //切换页面;
        MainActivity activity= (MainActivity) context;
        MenuFragment menuFragment= (MenuFragment) activity.getFragmentFromTag(MainActivity.FRAMELAYOUT_MENU);
        menuFragment.setOnSwitchPageListener(new MenuFragment.OnSwitchPageListener() {
            @Override
            public void switchPage(int position) {
                switchPager(position);
            }
        });

    }

    private int selectedPostion;//当前选中的位置;

    /**
     * 切换页面;
     * @param position
     */
    public void switchPager(int position) {
        if(pagers!=null && pagers.size()>0){
            selectedPostion=position;
            BaseNewsCenterPager pager=pagers.get(position);
            //1、设置标题;
            tv_title.setText(data.get(position).getTitle());
            if(position==2){//组图;
                ib_swich_list_grid.setVisibility(View.VISIBLE);
                ib_swich_list_grid.setImageResource(R.drawable.icon_pic_grid_type);
            }else{
                ib_swich_list_grid.setVisibility(View.GONE);
            }
            //2.清空页面所有视图;
            if(fl_content!=null){
                fl_content.removeAllViews();
            }
            //4、初始化子视图的数据;
            pager.initData();
            //3.把子视图添加到BasePager的FrameLayout中
            fl_content.addView(pager.rootView);
        }

    }

    /**
     * 从网络获取数据;
     */
    private void getDataFromNet(){
        OkHttpUtils.get().url(Constants.NEWSCENTER_PAGER_URL).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        UIUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UIUtil.getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i(response);
                        //将从网络上下载的数据保存到本地；
                        DiskUtil.saveTextToCacheFile(UIUtil.getContext(),fileName,response);
                        //处理数据;
                        processData(response);
                    }
                });

    }

    /**
     * 处理从网络上或者本地储存的获数据;
     * @param response
     */
    private void processData(String response) {
        Gson gson=new Gson();
        NewsBean news=gson.fromJson(response, NewsBean.class);
        data = news.getData();

        MainActivity activity= (MainActivity) context;
        //为新闻中心详情页设置页面;
        pagers.add(new NewsPager(context,data.get(0)));
        pagers.add(new TopicPager(context,data.get(0)));
        pagers.add(new PhotoPager(context,data.get(2)));
        pagers.add(new IntercoursePager(context,data.get(2)));
        pagers.add(new VotePager(context,data.get(2)));

        //默认显示第一个页面;
        switchPager(selectedPostion);

        setDataToLeftMenu(data);
    }

    /**
     * 为左侧菜单设置数据;
     * @param data
     */
    private void setDataToLeftMenu(List<NewsBean.DataBean> data) {
        MainActivity activity= (MainActivity) context;
        MenuFragment menuFragment= (MenuFragment) activity.getFragmentFromTag(MainActivity.FRAMELAYOUT_MENU);
        menuFragment.setMenuData(data);
    }


    @Override
    public void onClick(View v) {
        PhotoPager photoPager= (PhotoPager) pagers.get(2);
        photoPager.swithListAndGridView(ib_swich_list_grid);
    }
}
