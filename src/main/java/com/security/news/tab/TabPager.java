package com.security.news.tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.security.news.R;
import com.security.news.activity.NewsDetailActivity;
import com.security.news.bean.NewsBean;
import com.security.news.bean.TabBean;
import com.security.news.newscenterpager.BaseNewsCenterPager;
import com.security.news.util.Constants;
import com.security.news.util.DiskUtil;
import com.security.news.util.LogUtil;
import com.security.news.util.SharedPreferenceUtil;
import com.security.news.util.UIUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;

/**
 * 新闻详情标签页
 */
public class TabPager extends BaseNewsCenterPager {

    private static final String READ_ARRAY_ID ="READ_ARRAY";
    private Context context;
    private NewsBean.DataBean.ChildrenBean childrenBean;
    private ViewPager viewPager;
    private ListView listView;
    private String url;
    private List<TabBean.DataBean.TopnewsBean> topNews;//顶部新闻;
    private Banner banner;
    private List<TabBean.DataBean.NewsBean> news;//ListView的新闻;
    private List<String> images=new ArrayList<>();//图片的集合;
    private List<String> titles=new ArrayList<>();//标题的集合;
    private MaterialRefreshLayout materialRefreshLayout;
    private TabBean tab;
    private MyListViewAdapter adapter;

    public TabPager(Context context, NewsBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.context = context;
        this.childrenBean = childrenBean;
        url= Constants.BASE_URL+childrenBean.getUrl();
    }

    @Override
    public View initView() {
        View view = View.inflate(UIUtil.getContext(), R.layout.pager_tab, null);
        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.materialRefreshLayout);
        //viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        //banner = (Banner) view.findViewById(R.id.banner);
        View topNews=View.inflate(UIUtil.getContext(),R.layout.topnews,null);
        banner = (Banner)topNews.findViewById(R.id.banner);

        listView = (ListView) view.findViewById(R.id.listView);
        listView.addHeaderView(topNews);
        return view;
    }

    /**
     * 初始化数据;
     */
    @Override
    public void initData() {
        super.initData();
        String fileName=childrenBean.getTitle();
        String json=DiskUtil.readTextFromCacheFile(UIUtil.getContext(),fileName);
        if(!TextUtils.isEmpty(json)){
            Log.i("LOG", "TabPager:本地");
            processData(json);
        }else{
            Log.i("LOG", "TabPager:网络");
            //联网获取数据;
            getDataFromNet();
        }

        /**
         * 下拉刷新控件的
         */
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            /**
             * 下拉刷新;
             * @param materialRefreshLayout
             */
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                 /*materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialRefreshLayout.finishRefresh();
                    }
                },3000);*/
                //网络连接;
                OkHttpUtils.get().url(Constants.BASE_URL+tab.getData().getMore()).build()
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
                                //处理数据;
                                TabBean tabBean = new Gson().fromJson(response, TabBean.class);
                                List<TabBean.DataBean.NewsBean> refreshNews = tabBean.getData().getNews();
                                news.addAll(0,refreshNews);
                                adapter.notifyDataSetChanged();
                                materialRefreshLayout.finishRefresh();
                            }
                        });

            }

            /**
             * 上拉加载更多;
             * @param materialRefreshLayout
             */
            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                 materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialRefreshLayout.finishRefreshLoadMore();
                    }
                },3000);
            }
        });
    }

    /**
     * 从网络获数据;
     */
    private void getDataFromNet() {
        //网络连接;
        OkHttpUtils.get().url(url).build()
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
                        LogUtil.i("当前线程:"+Thread.currentThread().getName());
                        //将从网络上下载的数据保存到本地；
                        DiskUtil.saveTextToCacheFile(UIUtil.getContext(),childrenBean.getTitle(),response);
                        Log.i("LOG", "网络返回:"+childrenBean.getTitle());
                        //处理数据;
                        processData(response);
                    }
                });
    }

    /**
     * 处理网络数据;
     * @param response
     */
    private void processData(String response) {
        Gson gson=new Gson();
        tab = gson.fromJson(response, TabBean.class);
        topNews = tab.getData().getTopnews();
        news= tab.getData().getNews();

        //Banner的使用;
        //设置图片和标题;
        if(images.size()==0||titles.size()==0){
            for(int i=0;i<topNews.size();i++) {
                images.add(Constants.BASE_URL + topNews.get(i).getTopimage());
                titles.add(topNews.get(i).getTitle());
            }
        }

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        /* MyAdapter adapter=new MyAdapter();
        viewPager.setAdapter(adapter);*/

        //ListView的操作;
        adapter = new MyListViewAdapter();
        listView.setAdapter(adapter);

        //单个事件的点击操作;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position=position-1;
                //如果页面点击,则刷新页面,更改字体颜色;
                String arrays= (String) SharedPreferenceUtil.get(UIUtil.getContext(),READ_ARRAY_ID,"");
                if(!arrays.contains(news.get(position).getId()+"")){
                    SharedPreferenceUtil.put(UIUtil.getContext(),READ_ARRAY_ID,arrays+news.get(position).getId()+",");
                    adapter.notifyDataSetChanged();
                }

                //进入新闻详情页;
                Intent intent=new Intent(context, NewsDetailActivity.class);
                intent.putExtra("url",Constants.BASE_URL+news.get(position).getUrl());
                context.startActivity(intent);

            }
        });
    }

    /**
     * 自定义的GlideImageLoader,配合Banner使用;
     */
    private class GlideImageLoader extends ImageLoader{

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path)
                    .placeholder(R.drawable.home_scroll_default)
                    .error(R.drawable.home_scroll_default)
                    .into(imageView);
        }
    }


    /**
     * ListView的适配器；
     */
    private class MyListViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(news!=null&&news.size()>0){
                return news.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if(news!=null&&news.size()>0){
                return news.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //position=position-1;
            ViewHolder holder=null;
            if(convertView==null){
                convertView=View.inflate(UIUtil.getContext(),R.layout.item_lv_tab,null);
                holder=new ViewHolder();
                holder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_title= (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            //设置数据：
            holder.tv_title.setText(news.get(position).getTitle());
            holder.tv_time.setText(news.get(position).getPubdate());
            Glide.with(context).load(Constants.BASE_URL+news.get(position).getListimage())
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .into(holder.iv_icon);
            String arrays= (String) SharedPreferenceUtil.get(UIUtil.getContext(),READ_ARRAY_ID,"");
            if(arrays.contains(news.get(position).getId()+"")){
                holder.tv_title.setTextColor(Color.GRAY);
                holder.tv_time.setTextColor(Color.GRAY);
            }else{
                holder.tv_title.setTextColor(Color.BLACK);
                holder.tv_time.setTextColor(Color.BLACK);
            }

            return convertView;
        }

        private class ViewHolder{
            private ImageView iv_icon;
            private TextView tv_title;
            private TextView tv_time;
        }
    }


    /**
     * ViewPager的适配器;
     */
    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if(topNews!=null&&topNews.size()>0){
                return topNews.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView=new ImageView(UIUtil.getContext());
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            //x轴和Y轴拉伸
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //把图片添加到容器(ViewPager)中
            container.addView(imageView);
            Glide.with(context).load(Constants.BASE_URL+topNews.get(position).getTopimage()).into(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
