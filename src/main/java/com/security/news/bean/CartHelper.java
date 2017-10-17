package com.security.news.bean;

import android.util.Log;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 购物车工具类;
 */
public class CartHelper {

    private static CartHelper instance;

    /**
     * 私有化构造方法：
     */
    private CartHelper(){

    }

    /**
     * 单例模式;
     */
    public static CartHelper getInstance(){
        if(instance==null){
            synchronized (CartHelper.class){
                if(instance==null){
                    instance=new CartHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 返回所有数据：
     * @return
     */
    public static List<ShoppingCart> getAllData(){
        return DataSupport.findAll(ShoppingCart.class);
    }

    /**
     * 增加数据：如果数据库中已经存有当前的数据，则在此基础上数量+1；
     * 若数据库中没有存储当前的数据，则直接保存进入数据库;
     * @param cart
     */
    public static void addData(ShoppingCart cart){
        List<ShoppingCart> shoppingCarts = DataSupport.where("cartId=?", cart.getCartId()+"").find(ShoppingCart.class);
        if(shoppingCarts!=null&&shoppingCarts.size()>0){
            ShoppingCart tempCart=shoppingCarts.get(0);
            if(tempCart!=null){
                tempCart.setCount(tempCart.getCount()+1);
                tempCart.save();
            }else{
                cart.save();
            }
        }else{
            cart.save();
        }
    }

    /**
     * 删除所有数据：
     */
    public static void deleteAllData(){
        DataSupport.deleteAll(ShoppingCart.class);
    }

    /**
     * 从数据库中删除相应的数据;
     * @param cart
     */
    public static void deleteData(ShoppingCart cart){
        DataSupport.deleteAll(ShoppingCart.class,"cartId=?",cart.getCartId()+"");
    }

    /**
     * 从数据库中修改数据;
     * 具体实现的功能为:数据库中某件商品的数量;
     * @param cart
     */
    public static void updateData(ShoppingCart cart){
        List<ShoppingCart> shoppingCarts = DataSupport.where("cartId =? ", cart.getCartId()+"").find(ShoppingCart.class);
        if(shoppingCarts!=null&&shoppingCarts.size()>0){
            ShoppingCart tempCart=shoppingCarts.get(0);
            if(tempCart!=null){
                tempCart.setCount(cart.getCount());
                tempCart.setIsChecked(cart.isChecked());
                tempCart.save();
            }
        }
    }

    /**
     * 将商品详情转化为相应的ShoppingCart;
     * @param bean
     */
    public static ShoppingCart convert(ShoppingBean.Wares bean){
        ShoppingCart cart=new ShoppingCart();
        cart.setCartId(bean.getId());
        cart.setCategoryId(bean.getCategoryId());
        cart.setCampaignId(bean.getCampaignId());
        cart.setName(bean.getName());
        cart.setImgUrl(bean.getImgUrl());
        cart.setPrice(bean.getPrice());
        cart.setSale(bean.getSale());
        return cart;
    }

    /**
     *结算购物车选中商品的价钱：
     * @return
     */
    public static float showMoney(){
        float money=0.0f;
        List<ShoppingCart> carts=getAllData();
        if(carts!=null&&carts.size()>0){
            for(ShoppingCart cart:carts){
                if(cart.isChecked()){
                    money+=(cart.getPrice()*cart.getCount());
                }
            }
        }
        return money;
    }

}
