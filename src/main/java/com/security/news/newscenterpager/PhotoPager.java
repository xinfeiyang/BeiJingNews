package com.security.news.newscenterpager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.security.news.R;
import com.security.news.activity.PhotoDetailActivity;
import com.security.news.bean.NewsBean;
import com.security.news.bean.PhotoBean;
import com.security.news.util.Constants;
import com.security.news.util.DiskUtil;
import com.security.news.util.LogUtil;
import com.security.news.util.UIUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 组图页面;
 */
public class PhotoPager extends BaseNewsCenterPager {

    private NewsBean.DataBean data;
    private RecyclerView recyclerView;
    private List<PhotoBean.DataBean.NewsBean> photos=new ArrayList<>();
    private Context context;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private RecyclerViewAdapter adapter;
    private boolean isListType=true;

    public PhotoPager(Context context,NewsBean.DataBean data) {
        super(context);
        this.context=context;
        this.data=data;
    }

    @Override
    public View initView() {
        View view=View.inflate(UIUtil.getContext(), R.layout.pager_photo,null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }

    /**
     * 初始化数据；
     */
    @Override
    public void initData() {
        super.initData();
        getDataFromNet();
    }

    /**
     * 从网络获取数据;
     */
    private void getDataFromNet() {
        OkHttpUtils.get().url(Constants.BASE_URL+data.getUrl()).build()
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
                        processData(response);
                    }
                });
    }

    /**
     * 处理数据：
     * @param response
     */
    private void processData(String response) {
        Gson gson=new Gson();
        PhotoBean photoBean= gson.fromJson(response, PhotoBean.class);
        if(photos.size()==0){
            for(int i=0;i<photoBean.getData().getNews().size();i++){
                photos.add(photoBean.getData().getNews().get(i));
            }
        }
        linearLayoutManager = new LinearLayoutManager(context);
        gridLayoutManager = new GridLayoutManager(context,2);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
    }

    /**
     * 按钮点击时切换GridView和ListView;
     * @param btn
     */
    public void swithListAndGridView(ImageButton btn) {
        if(isListType){
            btn.setImageResource(R.drawable.icon_pic_list_type);
            recyclerView.setLayoutManager(gridLayoutManager);
        }else{
            btn.setImageResource(R.drawable.icon_pic_grid_type);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        isListType=!isListType;
    }


    /**
     * RecyclerView的适配器；
     */
    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PhotoViewHolder>{


        @Override
        public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=View.inflate(UIUtil.getContext(),R.layout.item_lv_photo,null);
            PhotoViewHolder holder=new PhotoViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(PhotoViewHolder holder, int position) {
            final PhotoBean.DataBean.NewsBean bean=photos.get(position);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, PhotoDetailActivity.class);
                    intent.putExtra("photo",bean.getLargeimage());
                    context.startActivity(intent);
                }
            });

            holder.tv_titel.setText(bean.getTitle());
            Glide.with(context).load(Constants.BASE_URL+bean.getSmallimage())
                    .placeholder(R.drawable.home_scroll_default)
                    .error(R.drawable.home_scroll_default)
                    .into(holder.iv_icon);
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }

        public class PhotoViewHolder extends RecyclerView.ViewHolder{

            private View view;
            private ImageView iv_icon;
            private TextView tv_titel;

            public PhotoViewHolder(View itemView) {
                super(itemView);
                this.view=itemView;
                iv_icon= (ImageView) view.findViewById(R.id.iv_icon);
                tv_titel = (TextView) view.findViewById(R.id.tv_title);
            }
        }
    }

}
