package com.security.news.util;

/**
 * 定义常量
 */
public interface Constants {

    /**
     * 联网请求的ip和端口
     */
    public static final String BASE_URL = "http://183.169.248.170:8080/web_home";

    /**
     * 新闻中心的网络地址
     */
    public static final String NEWSCENTER_PAGER_URL = BASE_URL+"/static/api/news/categories.json";

}
