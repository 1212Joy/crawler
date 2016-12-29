package com.example.spider.jd.helper;


import com.example.configuration.ApplicationConstants;
import com.example.framework.utils.StringUtils;

/**
 * Created by ZhaiJiaYi on 2016/12/23.
 */
public class PGDetailsHelper extends  BaseDetailsHelper{

    @Override
    public String getSku() {

        String sku = page.getHtml().xpath("//div[@id='choose-color']/div/div/a[contains(@href,'" + originKey + "')]//@title").get();
        return sku;
    }

    @Override
    public String getShopName() {
        return  parameters.get("P&G海外自营旗舰店");
    }

    @Override
    public String getBrand() {
        String brand = parameters.get("品牌");
        if (StringUtils.isNotBlank(brand)) {
            brand = "P&G宝洁".concat(" ").concat(brand.split("（")[0]);
        }
        return brand;
    }

    @Override
    public String getShopType() {
        return ApplicationConstants.ShopType.SELF.toString();
    }
}
