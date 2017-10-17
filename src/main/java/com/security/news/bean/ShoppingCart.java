package com.security.news.bean;

import org.litepal.crud.DataSupport;

/**
 * 购物选项,包含数量和是否选中；
 */
public class ShoppingCart extends DataSupport {

    /**
     * id : 1
     * categoryId : 5
     * campaignId : 1
     * name : 联想（Lenovo）拯救者14.0英寸游戏本（i7-4720HQ 4G 1T硬盘 GTX960M 2G独显 FHD IPS屏 背光键盘）黑
     * imgUrl : http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_55c1e8f7N4b99de71.jpg
     * price : 5979
     * sale : 8654
     */
    private int cartId;
    private int categoryId;
    private int campaignId;
    private String name;
    private String imgUrl;
    private float price;
    private int sale;

    /**
     * 购买的数量
     */
    private int count = 1;

    /**
     * 是否勾选
     */
    private boolean isChecked = true;

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "cartId=" + cartId +
                ", categoryId=" + categoryId +
                ", campaignId=" + campaignId +
                ", name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", price=" + price +
                ", sale=" + sale +
                ", count=" + count +
                ", isChecked=" + isChecked +
                '}';
    }

}
