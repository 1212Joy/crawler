package com.example.spider.jd.helper;


import com.example.configuration.ApplicationConstants;
import com.example.framework.utils.StringUtils;

/**
 * Created by ZhaiJiaYi on 2016/12/26.
 */
public class JohnsonDetailsHelper extends  BaseDetailsHelper {

    @Override
    public String getSku() {
        return parameters.get("净含量");
    }

    @Override
    public String getShopName() {
        String shopName =  parameters.get("店铺");
        if (StringUtils.isBlank(shopName)) {
            shopName =  page.getHtml().xpath("//div[@class='seller-infor']/a[@class='name']/text()").get();
            if (StringUtils.isBlank(shopName)) {
                shopName =  page.getHtml().xpath("//div[@id='extInfo']/div[@class='brand-logo']/a/img/@title").get();
            }
        }


        return shopName;
    }

    @Override
    public String getBrand() {
        String  brand = "强生(Johnson)";
        return brand;
    }

    @Override
    public String getCategory() {
        return "沐浴露";
    }

    @Override
    public String getShopType() {
        String shopType = page.getHtml().xpath("//em[@class='u-jd']/text()").get();
        if ("京东自营".equals(shopType)) {
            shopType = ApplicationConstants.ShopType.SELF.toString();
        }else{
            shopType = ApplicationConstants.ShopType.THIRD_PARTY.toString();
        }
    return shopType;
    }

}
