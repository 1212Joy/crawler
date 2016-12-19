package com.example.spider.kaolao.repo;

import lombok.Data;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.Date;

/**
 * Created by zhuzhihang on 2016/12/13.
 */
@Data
@HelpUrl(value = "http://www.kaola.com/category/2620.html?pageSize=60*", sourceRegion = "//div[@class='m-search']/div/div[@class='splitPages']")
@TargetUrl(value = "http://www.kaola.com/product/*.html", sourceRegion = "//div[@class='m-search']/div/div[@id='searchresult']")
public class KLMilkRepo {
    private int id;
    //商品编号
    private String platformId;
    //页面url
    private String url;
    //标题
    @ExtractBy("//div[@id='j-producthead']/div/span/text()")
    private String title;
    //价格
    private String price;
    //sku种类
    private String sku;
    //品牌
    private String brand;
    //类别，枚举ApplicationConstants.Category
    private String category;
    //店铺名称
    private String shopName;
    //店铺类型，枚举ApplicationConstants.ShopType
    private String shopType;
    //毛重、净重
    private String weight;
    //销量、评价数
    private String sales;
    //抓取来源，枚举ApplicationConstants.Origin
    private String origin;
    private Date createdAt;
    private int version;
}

