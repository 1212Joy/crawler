package com.example.spider.jd.helper;


import com.example.spider.request.Downloader;
import us.codecraft.webmagic.Page;

/**
 * Created by ZhaiJiaYi on 2016/12/23.
 */
public interface DetailsHelper {
    void  init(Page page);

    String getOriginKey();

    String getUrl();

    String getTitle();

    String getPrice(Downloader downloader);

    String getSku();

    String getBrand();

    String getCategory();

    String getShopName();

    String getWeight();

    String getSales();

    String getSourceArea();

    String getShopType();







}
