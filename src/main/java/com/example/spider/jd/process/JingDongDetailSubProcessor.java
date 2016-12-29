package com.example.spider.jd.process;


import com.example.configuration.ApplicationConstants;
import com.example.framework.http.HttpRequester;
import com.example.framework.utils.JsonUtils;
import com.example.spider.jd.helper.DetailsHelper;
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
    private Class detailsHelperClass;

    public JingDongDetailSubProcessor() {
    }

    public JingDongDetailSubProcessor(Downloader downloader,Class detailsHelperClass) {
        this.downloader = downloader;
        this.detailsHelperClass = detailsHelperClass;
    }



    @Override
    public MatchOther processPage(Page page){
        log.info("url {}", page.getUrl());
        DetailsHelper detailsHelper = getHelper();
        detailsHelper.init(page);
        Crawler result = new Crawler();
        result.setOriginKey(detailsHelper.getOriginKey());
        result.setUrl(detailsHelper.getUrl());
        result.setTitle(detailsHelper.getTitle());
        result.setPrice(detailsHelper.getPrice(downloader));
        result.setSku(detailsHelper.getSku());
        result.setBrand(detailsHelper.getBrand());
        result.setCategory(detailsHelper.getCategory());
        result.setShopName(detailsHelper.getShopName());
        result.setWeight(detailsHelper.getWeight());
        result.setSales(detailsHelper.getSales());
        result.setSourceArea(detailsHelper.getSourceArea());
        result.setShopType(detailsHelper.getShopType());

        result.setOrigin(ApplicationConstants.Origin.JD.toString());
        result.setCrawledAt(new Date());

        page.putField("result", result);

        return null;
    }

    private DetailsHelper getHelper() {
        try{
            return (DetailsHelper) detailsHelperClass.newInstance();
        }catch (Exception e){
            throw new IllegalArgumentException("helper is not right");
        }
    }

    @Override
    public boolean match(Request page) {
        return new PlainText(page.getUrl()).regex(detailUrl).match();
    }


}