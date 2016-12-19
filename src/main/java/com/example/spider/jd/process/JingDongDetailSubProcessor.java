package com.example.spider.jd.process;


import com.example.configuration.ApplicationConstants;
import com.example.framework.http.HttpRequester;
import com.example.framework.utils.JsonUtils;
import com.example.spider.request.Downloader;
import com.example.framework.http.HtmlParser;
import com.example.vo.Crawler;
import lombok.extern.slf4j.Slf4j;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.htmlcleaner.TagNode;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.handler.SubPageProcessor;
import us.codecraft.webmagic.selector.PlainText;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuzhihang on 2016/12/15.
 */
@Slf4j
public class JingDongDetailSubProcessor extends JingDongProcessor implements SubPageProcessor {

    private Downloader downloader;

    public JingDongDetailSubProcessor() {
    }

    public JingDongDetailSubProcessor(Downloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public MatchOther processPage(Page page) {
        log.info("url {}", page.getUrl());
        String url = page.getUrl().get();
        String platformId = url.split("/")[3].split("\\.")[0];
        String productContent = page.getRawText();
        Map<String, String> parameters = getParameters(productContent);

        // String title = HtmlParser.parseText(productContent, "//div[@class='itemInfo-wrap']/div[@class='sku-name']");
        // if (StringUtils.isBlank(title)) {
        //     title = HtmlParser.parseText(productContent, "//div[@id='name']//h1").replaceAll("\n", "").replace(" ", "");
        // }

        String title = page.getHtml().xpath("//div[@id='name']/h1/text()").get();

        String price = getPrice(platformId);
        String sales = getSales(platformId);

        String shopName = "P&G海外自营旗舰店";
        String shopType = ApplicationConstants.ShopType.SELF.toString();

        String brand = getBrand(parameters);
        String weight = parameters.get("商品毛重");
        String sourceArea = parameters.get("商品产地");
        String category = getCategory(parameters);

        String sku = page.getHtml().xpath("//div[@id='choose-color']/div/div/a[contains(@href,'" + platformId + "')]//@title").get();

        Crawler result = new Crawler();
        result.setPlatformId(platformId);
        result.setUrl(url);
        result.setTitle(title);
        result.setPrice(price);
        result.setSku(sku);
        result.setBrand(brand);
        result.setCategory(category);
        result.setShopName(shopName);
        result.setShopType(shopType);
        result.setWeight(weight);
        result.setSales(sales);
        result.setSourceArea(sourceArea);
        result.setOrigin(ApplicationConstants.Origin.JD.toString());
        result.setCreatedAt(new Date());

        page.putField("result", result);

        return null;
    }

    private String getCategory(Map<String, String> parameters) {
        String category = parameters.get("分类");
        if (StringUtils.isBlank(category)) {
            String name = parameters.get("商品名称");
            if (StringUtils.isNotBlank(name)) {
                String brand = parameters.get("品牌");
                if (StringUtils.isNotBlank(brand)) {
                    brand = brand.split("（")[0];
                    category = name.replace(brand, "");
                }
            }
        }
        return category;
    }

    private String getBrand(Map<String, String> parameters) {
        String brand = parameters.get("品牌");
        if (StringUtils.isNotBlank(brand)) {
            brand = "P&G宝洁".concat(" ").concat(brand.split("（")[0]);
        }
        return brand;
    }

    private Map<String, String> getParameters(String productContent) {
        List<TagNode> parameters = HtmlParser.parseNode(productContent, "//ul[@id='parameter2']/li");
        if (parameters.isEmpty()) {
            parameters = HtmlParser.parseNode(productContent, "//ul[@class='parameter2 p-parameter-list']/li");
        }
        HashMap<String, String> params = new HashMap<>();
        parameters.forEach(tagNode -> {
            StringBuffer text = (StringBuffer) tagNode.getText();
            String substring = text.substring(0, text.indexOf("："));
            params.put(substring, tagNode.getAttributeByName("title"));
        });
        return params;
    }

    @Override
    public boolean match(Request page) {
        return new PlainText(page.getUrl()).regex(detailUrl).match();
    }

    private String getPrice(String platformId) {
        try {
            String priceUrl = "https://p.3.cn/prices/get?skuid=J_" + platformId;
            String data = downloader.get(priceUrl);
            // String data = httpRequester.get(priceUrl).getContent();
            log.info("[price data] - [data] = {}", data);
            data = data.replace("[", "").replace("]", "");
            return JsonUtils.getString(data, "p");
        } catch (Exception e) {
            log.info("[获取价格error] - [e] = {}", ExceptionUtils.getStackTrace(e));
            return "";
        }
    }

    private String getSales(String platformId) {
        try {
            HttpRequester httpRequester = HttpRequester.newInstance();
            Map<String, String> map = new HashMap<>();
            map.put("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            map.put("Accept-Encoding", "gzip, deflate, sdch");
            map.put("Accept-Language",
                    "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,zh-TW;q=0.2");
            map.put("Cache-Control", "no-cache");
            map.put("Connection", "keep-alive");
            map.put("DNT", "1");
            map.put("Pragma", "no-cache");
            map.put("Upgrade-Insecure-Requests", "1");

            String saleUrl = "https://club.jd.com/clubservice.aspx?method=GetCommentsCount&referenceIds=" + platformId;
            String data = httpRequester.setHeader(map).get(saleUrl).getContent();
            return JsonUtils.toBean(data, JSONObject.class).getJSONArray("CommentsCount").getJSONObject(0).getString("CommentCount");
            // return JsonUtils.getString(
            //         JsonUtils.getString(data, "CommentsCount").replace("[", "").replace("]", ""),
            //         "CommentCount");
        } catch (Exception e) {
            log.info("[获取评价error] - [e] = {}", ExceptionUtils.getStackTrace(e));
            return "";
        }
    }
}
