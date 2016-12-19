package com.example.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by wanghongmeng on 2016/11/23.
 */
@Data
public class Crawler {
    private int id;
    //商品编号
    private String platformId;
    //页面url
    private String url;
    //标题
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
    //原产地
    private String sourceArea;
    //抓取来源，枚举ApplicationConstants.Origin
    private String origin;
    private Date createdAt;
    private int version;
}
