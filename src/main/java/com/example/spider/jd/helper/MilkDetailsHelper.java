package com.example.spider.jd.helper;


import com.example.configuration.ApplicationConstants;
import com.example.framework.utils.StringUtils;

/**
 * Created by ZhaiJiaYi on 2016/12/23.
 */
public class MilkDetailsHelper extends  BaseDetailsHelper{

    @Override
    public String getSku() {
        return  parameters.get("段位");
    }

    @Override
    public String getShopName() {
        String shopName = page.getHtml().xpath("//div[@class='seller-infor']/a[@class='name']/text()").get();
        if (StringUtils.isEmpty(shopName)) {
            shopName = page.getHtml().xpath("//div[@class='shopName']/strong/a/text()").get();
        }
        return shopName;
    }

    @Override
    public String getBrand() {
        String brand = page.getHtml().xpath("//ul[@id='parameter-brand']/li/a[1]/text()").get();
        if (StringUtils.isEmpty(brand)) {
            brand = parameters.get("品牌");
        }
        return brand;
    }

    @Override
    public String getShopType() {
        return ApplicationConstants.ShopType.SELF.toString();
    }
}
