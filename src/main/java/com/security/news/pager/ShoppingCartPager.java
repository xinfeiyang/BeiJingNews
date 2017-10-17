package com.security.news.pager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.security.news.R;
import com.security.news.bean.CartHelper;
import com.security.news.bean.ShoppingCart;
import com.security.news.util.LogUtil;
import com.security.news.util.UIUtil;
import com.security.view.NumberAddAndSubstractView;

import java.util.Iterator;
import java.util.List;

/**
 * 政务
 */
public class ShoppingCartPager extends BasePager {

    private Context context;
    private RecyclerView recyclerView;
    private CheckBox checkbox;
    private TextView tv_total;
    private Button btnCounter;
    private Button btnDelete;
    private TextView tv_empty;
    private CartHelper helper;
    private List<ShoppingCart> carts;

    /**
     * 编辑状态
     */
    private static final int STATUS_EDIT = 0;
    /**
     * 完成状态
     */
    private static final int STATUS_COMPLETE = 1;
    private LinearLayoutManager linearLayoutManager;
    private MyRecyclerAdaper adaper;
    private RelativeLayout rl_bottom;


    public ShoppingCartPager(Context context) {
        super(context);
        this.context = context;
        helper=CartHelper.getInstance();
        //默认是编辑状态;
        btn_cart.setTag(STATUS_EDIT);
    }

    @Override
    public void initData() {
        super.initData();
        //1、设置标题;
        tv_title.setText("购物车");
        btn_cart.setVisibility(View.VISIBLE);
        //2.联网请求，得到数据，创建视图
        View view = View.inflate(UIUtil.getContext(), R.layout.pager_affair, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        rl_bottom = (RelativeLayout) view.findViewById(R.id.rl_bottom);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        checkbox = (CheckBox) view.findViewById(R.id.checkbox);
        tv_total = (TextView) view.findViewById(R.id.tv_total);
        btnCounter = (Button) view.findViewById(R.id.btn_counter);
        btnDelete = (Button) view.findViewById(R.id.btn_delete);
        initEvent();

        //3.把子视图添加到BasePager的FrameLayout中
        if(fl_content!=null){
            fl_content.removeAllViews();
        }
        fl_content.addView(view);

        //4.绑定数据
        showData();

        //5、展示商品总价;
       showMoney();

        //6、验证是否全选或者非全选;
        validateSelectAllOrNot();

        //7、验证购物车是否是空的;
        validateCartIsEmptyOrNot();


    }

    /**
     * 6、验证是否全选或者非全选;
     */
    private void validateSelectAllOrNot() {
        List<ShoppingCart> shoppingcarts=CartHelper.getAllData();
        for(ShoppingCart cart:shoppingcarts){
            if(!cart.isChecked()){
                checkbox.setChecked(false);
                return;
            }
        }
        checkbox.setChecked(true);
    }

    /**
     * 展示商品总价
     */
    private void showMoney() {
        tv_total.setText("¥"+CartHelper.showMoney()+"");
    }

    /**
     * 视图、按钮的点击事件：
     */
    private void initEvent() {

        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status= (int) btn_cart.getTag();
                if(status==STATUS_EDIT){//编辑状态;
                    //展示btnDelete按钮,隐藏结算按钮;
                    btn_cart.setText("完成");
                    btn_cart.setTag(STATUS_COMPLETE);
                    tv_total.setVisibility(View.GONE);
                    btn_cart.setTag(STATUS_COMPLETE);
                    btnDelete.setVisibility(View.VISIBLE);
                    btnCounter.setVisibility(View.GONE);
                    checkbox.setChecked(false);
                    setCheckAll_None(false);
                }else if(status==STATUS_COMPLETE){
                    //展示结算按钮,隐藏删除按钮;
                    btn_cart.setText("编辑");
                    //改变状态;
                    btn_cart.setTag(STATUS_EDIT);
                    tv_total.setVisibility(View.VISIBLE);
                    btn_cart.setTag(STATUS_EDIT);
                    btnCounter.setVisibility(View.VISIBLE);
                    btnDelete.setVisibility(View.GONE);
                    if(carts.size()==0){
                        checkbox.setChecked(false);
                    }else{
                        checkbox.setChecked(true);
                    }
                    setCheckAll_None(true);
                    showMoney();
                }
            }
        });

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck=checkbox.isChecked();
                setCheckAll_None(isCheck);
                showMoney();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCheckedShoppingCart();
            }
        });

    }

    /**
     * 删除选中的商品数量;
     */
    private void deleteCheckedShoppingCart() {
        for(Iterator iterator=carts.iterator();iterator.hasNext();){
            ShoppingCart cart= (ShoppingCart) iterator.next();
            if(cart.isChecked()){
                int position=carts.indexOf(cart);
                CartHelper.deleteData(cart);
                iterator.remove();
                adaper.notifyItemRemoved(position);
            }
        }
        validateCartIsEmptyOrNot();
    }


    /**
     * 验证购物车是否是空的：
     */
    private void validateCartIsEmptyOrNot() {
        //验证购物车是否已经空;
        if(carts.size()==0){
            tv_empty.setVisibility(View.VISIBLE);
            btn_cart.setTag(STATUS_EDIT);
            btn_cart.setText("编辑");
            rl_bottom.setVisibility(View.GONE);
        }else{
            tv_empty.setVisibility(View.GONE);
            rl_bottom.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置全选或者非全选aun;
     * @param isCheck
     */
    private void setCheckAll_None(boolean isCheck) {
        if(carts!=null&&carts.size()>0){
            for(int i=0;i<carts.size();i++){
                ShoppingCart cart=carts.get(i);
                cart.setIsChecked(isCheck);
                CartHelper.updateData(cart);
                adaper.notifyItemChanged(i);
            }
        }
    }

    /**
     * 为适配器设置数据;
     */
    private void showData() {
        carts = CartHelper.getAllData();
        if(carts!=null&&carts.size()>0){
            tv_empty.setVisibility(View.GONE);
            linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
            adaper = new MyRecyclerAdaper();
            recyclerView.setAdapter(adaper);
        }else{
            tv_empty.setVisibility(View.VISIBLE);
        }
    }


    /**
     * RecyclerView的适配器;
     */
    private class MyRecyclerAdaper extends RecyclerView.Adapter<MyRecyclerAdaper.ViewHolder>{


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=View.inflate(UIUtil.getContext(),R.layout.item_lv_cart,null);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final ShoppingCart cart=carts.get(position);
            Glide.with(context).load(cart.getImgUrl())
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .into(holder.iv_image);
            holder.cb_check.setChecked(cart.isChecked());
            holder.tv_name.setText(cart.getName());
            holder.tv_price.setText(cart.getPrice()+"");
            holder.view_add_sub.setValue(cart.getCount());
            holder.view_add_sub.setOnNumberAddAndSubstractListener(new NumberAddAndSubstractView.OnNumberAddAndSubstractListener() {
                @Override
                public void onNumberAdd(View view, Integer value) {
                    cart.setCount(value);
                    CartHelper.updateData(cart);
                    showMoney();
                    notifyItemChanged(holder.getLayoutPosition());
                }

                @Override
                public void onNumberSubstract(View view, Integer value) {
                    cart.setCount(value);
                    CartHelper.updateData(cart);
                    showMoney();
                    notifyItemChanged(holder.getLayoutPosition());
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.cb_check.setChecked(!cart.isChecked());
                    cart.setIsChecked(!cart.isChecked());
                    notifyItemChanged(holder.getLayoutPosition());
                    CartHelper.updateData(cart);
                    //展示商品总价;
                    showMoney();
                    //展示全选和非全选;
                    validateSelectAllOrNot();
                }
            });

        }

        @Override
        public int getItemCount() {
            return carts.size();
        }

        /**
         * ViewHolder:展示item中的各个子控件;
         */
        public class ViewHolder extends RecyclerView.ViewHolder{

            private CheckBox cb_check;
            private ImageView iv_image;
            private TextView tv_name,tv_price;
            private NumberAddAndSubstractView view_add_sub;
            private View itemView;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                cb_check= (CheckBox) itemView.findViewById(R.id.cb_check);
                iv_image= (ImageView) itemView.findViewById(R.id.iv_image);
                tv_name= (TextView) itemView.findViewById(R.id.tv_name);
                tv_price= (TextView) itemView.findViewById(R.id.tv_price);
                view_add_sub=(NumberAddAndSubstractView)itemView.findViewById(R.id.view_add_sub);
            }
        }

    }

}