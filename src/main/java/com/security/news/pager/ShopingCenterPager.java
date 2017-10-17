package com.security.news.pager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.security.news.R;
import com.security.news.bean.CartHelper;
import com.security.news.bean.ShoppingBean;
import com.security.news.bean.ShoppingCart;
import com.security.news.util.DiskUtil;
import com.security.news.util.UIUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import java.util.Arrays;
import java.util.List;

import okhttp3.Call;


/**
 * 商城中心;
 */
public class ShopingCenterPager extends BasePager {

    private Context context;
    private String fileName="shopping.json";
    private ProgressBar pb_loading;
    private RecyclerView recyclerView;
    private List<ShoppingBean.Wares> datas;
    private ShoppingAdapter adapter;
    private CartHelper cartHelper;

    public ShopingCenterPager(Context context) {
        super(context);
        this.context=context;
        cartHelper=CartHelper.getInstance();
    }

    @Override
    public void initData() {
        super.initData();
        //1、设置标题;
        tv_title.setText("商城热卖");
        //2.联网请求，得到数据，创建视图
        View view=View.inflate(UIUtil.getContext(), R.layout.pager_shopping,null);
        recyclerView = (RecyclerView) view.findViewById(R.id.rl_recyclerView);
        pb_loading = (ProgressBar) view.findViewById(R.id.progressBar);
        //3.把子视图添加到BasePager的FrameLayout中
        if(fl_content!=null){
            fl_content.removeAllViews();
        }
        fl_content.addView(view);
        //4.绑定数据
        String json=DiskUtil.readTextFromCacheFile(context,fileName);
        if(!TextUtils.isEmpty(json)){
            pb_loading.setVisibility(View.GONE);
            processData(json);
        }else{
            getDataFromNet();
        }
    }

    /**
     * 从网络上获取数据;
     */
    private void getDataFromNet() {
        OkHttpUtils.get().url("http://112.124.22.238:8081/course_api/wares/hot?pageSize=20&curPage=1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        UIUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb_loading.setVisibility(View.GONE);
                                Toast.makeText(UIUtil.getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        pb_loading.setVisibility(View.GONE);
                        //将从网络上下载的数据保存到本地；
                        DiskUtil.saveTextToCacheFile(UIUtil.getContext(),fileName,response);
                        //处理数据;
                        processData(response);
                    }
                });
    }

    /**
     * 解析数据;
     * @param json
     */
    private void processData(String json) {
        ShoppingBean shoppingBean = new Gson().fromJson(json, ShoppingBean.class);
        datas = shoppingBean.getList();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ShoppingAdapter();
        recyclerView.setAdapter(adapter);

    }


    /**
     * 适配器;
     */
    private class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder>{


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=View.inflate(UIUtil.getContext(),R.layout.item_lv_shopping,null);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final ShoppingBean.Wares bean=datas.get(position);
            Glide.with(context).load(bean.getImgUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .into(holder.iv_icon);
            holder.tv_name.setText(bean.getName());
            holder.tv_price.setText(bean.getPrice()+"");
            //点击立即购买按钮;
            holder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCart cart=cartHelper.convert(bean);
                    cartHelper.addData(cart);
                    Toast.makeText(context, "商品已经放入购物车", Toast.LENGTH_SHORT).show();
                    for(ShoppingCart sc:cartHelper.getAllData()){
                        Log.i("LOG", sc.getName()+":"+sc.getCount());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if(datas!=null&&datas.size()>0){
                return datas.size();
            }
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            private View view;
            private ImageView iv_icon;
            private TextView tv_name;
            private TextView tv_price;
            private Button btn_add;
            public ViewHolder(View itemView) {
                super(itemView);
                this.view=itemView;
                this.iv_icon= (ImageView) view.findViewById(R.id.iv_icon);
                this.tv_name= (TextView) view.findViewById(R.id.tv_name);
                this.tv_price= (TextView) view.findViewById(R.id.tv_price);
                this.btn_add= (Button) view.findViewById(R.id.btn_add);
            }
        }
    }


}
