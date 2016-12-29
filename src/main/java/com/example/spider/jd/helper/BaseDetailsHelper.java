package com.example.spider.jd.helper;


import com.example.configuration.ApplicationConstants;
import com.example.framework.http.HtmlParser;
import com.example.framework.http.HttpRequester;
import com.example.framework.utils.JsonUtils;
import com.example.framework.utils.StringUtils;
import com.example.spider.request.Downloader;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.htmlcleaner.TagNode;
import us.codecraft.webmagic.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhaiJiaYi on 2016/12/23.
 */
@Slf4j
public  class BaseDetailsHelper implements  DetailsHelper {
    protected Page page;
    protected Map<String, String> parameters;
    protected String productContent;
    protected String originKey;
    private String url;

    public String getUrl() {
        return url;
    }

    public String getOriginKey() {
        return originKey;
    }

    public void  init(Page page){
        this.page = page;
        this.url = page.getUrl().get();
        this.originKey = url.split("/")[3].split("\\.")[0];
        this.productContent = page.getRawText();
        this.parameters = getParameters(productContent);
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

    public String getSku() {
        return null;
    }

    public String getShopName() {
        return null;
    }

    public String getBrand() {
        return null;
    }


    public String getTitle() {
        return  page.getHtml().xpath("//div[@id='name']/h1/text()").get();
    }


    public String getWeight() {
        return  parameters.get("商品毛重");
    }

    public String getSourceArea() {
        return parameters.get("商品产地");
    }

    public String getCategory() {
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


    public String getPrice(Downloader downloader) {
        try {
            String priceUrl = "https://p.3.cn/prices/get?skuid=J_" + originKey;
            String data = downloader.get(priceUrl);
            log.info("[price data] - [data] = {}", data);
            data = data.replace("[", "").replace("]", "");
            return JsonUtils.getString(data, "p");
        } catch (Exception e) {
            log.info("[获取价格error] - [e] = {}", ExceptionUtils.getStackTrace(e));
            return "";
        }
    }

    public  String getSales() {
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

            String saleUrl = "https://club.jd.com/clubservice.aspx?method=GetCommentsCount&referenceIds=" + originKey;
            String data = httpRequester.setHeader(map).get(saleUrl).getContent();
            return JsonUtils.toBean(data, JSONObject.class).getJSONArray("CommentsCount").getJSONObject(0).getString("CommentCount");
        } catch (Exception e) {
            String stackTrace = ExceptionUtils.getStackTrace(e);
            log.info("[获取评价error] - [e] = {}", stackTrace);
            return "";
        }
    }

    @Override
    public String getShopType() {
        return null;
    }
}
