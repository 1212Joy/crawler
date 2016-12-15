package com.example.spider.process;

import com.example.spider.process.BaseProcessor;

/**
 * Created by zhuzhihang on 2016/12/13.
 */
public class KaoLaProcessor extends BaseProcessor {

    public static String JS_SCRIPT_MILK = "var method = {\n" +
            "    getShopName: function() {\n" +
            "        if (g.self) {\n" +
            "            return \"网易考拉自营\";\n" +
            "        } else {\n" +
            "            if (g.shopInfo) {\n" +
            "                return g.shopInfo.shopName;\n" +
            "            } else {\n" +
            "                return \"考拉商家\";\n" +
            "            }\n" +
            "        }\n" +
            "    },\n" +
            "    getInfo: function(infoId) {\n" +
            "        for (var i = 0; i < infoSize; i++) {\n" +
            "            var info = infos[i];\n" +
            "            if (infoId === info.propertyNameId) {\n" +
            "                return info.propertyValues[0].propertyValue;\n" +
            "            }\n" +
            "        }\n" +
            "        return \"\";\n" +
            "    }\n" +
            "};\n" +
            "var d = __kaolaProductData;\n" +
            "var g = d.goods;\n" +
            "var infos = g.goodsPropertyList;\n" +
            "var infoSize = infos.length;\n" +
            "var e = {};\n" +
            "e.price = g.actualCurrentPrice;\n" +
            "e.title = g.title;\n" +
            "e.platformId = g.skuList[0].skuId;\n" +
            "e.brand = g.brandName;\n" +
            "e.sku = method.getInfo(\"358\");\n" +
            "e.self = g.self;\n" +
            "e.shopName = method.getShopName();\n" +
            "e.weight = method.getInfo(\"100367\");\n" +
            "e.sales = d.commentStat.commentsCount;\n" +
            "e.sourceArea = method.getInfo(\"100339\");";
    public static String JS_SCRIPT_JOHNSON = "var method = {\n" +
            "    getShopName: function() {\n" +
            "        if (g.self) {\n" +
            "            return \"网易考拉自营\";\n" +
            "        } else {\n" +
            "            if (g.shopInfo) {\n" +
            "                return g.shopInfo.shopName;\n" +
            "            } else {\n" +
            "                return \"考拉商家\";\n" +
            "            }\n" +
            "        }\n" +
            "    },\n" +
            "    getInfo: function(infoId) {\n" +
            "        for (var i = 0; i < infoSize; i++) {\n" +
            "            var info = infos[i];\n" +
            "            if (infoId === info.propertyNameId) {\n" +
            "                return info.propertyValues[0].propertyValue;\n" +
            "            }\n" +
            "        }\n" +
            "        return \"\";\n" +
            "    }\n" +
            "};\n" +
            "var d = __kaolaProductData;\n" +
            "var g = d.goods;\n" +
            "var infos = g.goodsPropertyList;\n" +
            "var infoSize = infos.length;\n" +
            "var e = {};\n" +
            "e.price = g.actualCurrentPrice;\n" +
            "e.title = g.title;\n" +
            "e.platformId = g.skuList[0].skuId;\n" +
            "e.brand = g.brandName;\n" +
            "e.sku = method.getInfo(\"100202\");\n" +
            "e.self = g.self;\n" +
            "e.shopName = method.getShopName();\n" +
            "e.weight = method.getInfo(\"100341\");\n" +
            "e.sales = d.commentStat.commentsCount;\n" +
            "e.sourceArea = method.getInfo(\"100339\");";
    public static String JS_SCRIPT_PG  = "var method = {\n" +
            "    getShopName: function() {\n" +
            "        if (g.self) {\n" +
            "            return \"网易考拉自营\";\n" +
            "        } else {\n" +
            "            if (g.shopInfo) {\n" +
            "                return g.shopInfo.shopName;\n" +
            "            } else {\n" +
            "                return \"考拉商家\";\n" +
            "            }\n" +
            "        }\n" +
            "    },\n" +
            "    getInfo: function(infoId) {\n" +
            "        for (var i = 0; i < infoSize; i++) {\n" +
            "            var info = infos[i];\n" +
            "            if (infoId === info.propertyNameId) {\n" +
            "                return info.propertyValues[0].propertyValue;\n" +
            "            }\n" +
            "        }\n" +
            "        return \"\";\n" +
            "    }\n" +
            "};\n" +
            "var d = __kaolaProductData;\n" +
            "var g = d.goods;\n" +
            "var infos = g.goodsPropertyList;\n" +
            "var infoSize = infos.length;\n" +
            "var e = {};\n" +
            "e.price = g.actualCurrentPrice;\n" +
            "e.title = g.title;\n" +
            "e.platformId = g.skuList[0].skuId;\n" +
            "e.brand = g.brandName;\n" +
            "e.sku = method.getInfo(\"100341\");\n" +
            "e.self = g.self;\n" +
            "e.shopName = method.getShopName();\n" +
            "e.weight = method.getInfo(\"100281\");\n" +
            "e.sales = d.commentStat.commentsCount;\n" +
            "e.category = g.categoryPath[5].categoryName;\n" +
            "e.sourceArea = '美国';";
    String detailSourceRegion = "//div[@class='m-search']/div/div[@id='searchresult']";
    // String listUrl = "http://www.kaola.com/category/2620.html*";
    String detailUrl = "http://www.kaola.com/product/\\w+.html";
    String listSourceRegion = "//div[@class='m-search']/div/div[@class='splitPages']";
}
