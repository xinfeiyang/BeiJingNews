package com.security.news;

import com.security.news.bean.ShoppingCart;

import org.junit.Test;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit item_lv_shopping, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void subTest() {
        String url = "http://183.169.248.170:8080/web_home/static/api/news/10007/list_1.json";
        url = url.substring(url.lastIndexOf("/") + 1, url.length());
        System.out.println(url);
    }

    @Test
    public void testShowDatas() {
        List<ShoppingCart> carts = DataSupport.findAll(ShoppingCart.class);
        System.out.println(Arrays.asList(carts));
    }

    @Test
    public void testCollection() {
        List<String> list = new ArrayList<>();
        list.add("冯朗");
        list.add("冯跃");
        list.add("李晓丹");
        list.add("李佩丹");
        list.add("朱蕊");
        list.add("郑艳玲");
        list.add("郑玲玲");

        for (int i = 0; i < list.size(); i++) {
            if (i == 2) {
                list.remove(2);
            }
        }

        for (String v : list) {
            System.out.println(v);
        }

    }

    @Test
    public void testDelete() {
        List<String> list = new ArrayList<>();
        list.add("冯朗");
        list.add("冯跃");
        list.add("李晓丹");
        list.add("李佩丹");
        list.add("朱蕊");
        list.add("郑艳玲");
        list.add("郑玲玲");

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            if ("李晓丹".equals(name)) {
                iterator.remove();
            }
        }
        for (String v : list) {
            System.out.println(v);
        }
    }

    @Test
    public void testDel() {
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        System.out.println("Original list : " + list);
        System.out.println();
        /*for(int i=list.size()-1;i>=0;i--){
            list.remove(i);
        }*/
        Iterator<String> iterator = list.iterator();
        while(iterator.hasNext()){
            String number=iterator.next();
            iterator.remove();
        }
        System.out.println("Removed list : " + list);
    }

}