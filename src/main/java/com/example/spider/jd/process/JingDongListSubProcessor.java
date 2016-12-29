package com.example.spider.jd.process;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.handler.SubPageProcessor;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.XpathSelector;

import java.util.regex.Pattern;

/**
 * Created by zhuzhihang on 2016/12/15.
 */
@Slf4j
public class JingDongListSubProcessor extends JingDongProcessor implements SubPageProcessor {

    private String listUrl;

    public JingDongListSubProcessor(String listUrlPattern) {
        this.listUrl = listUrlPattern;
    }

    @Override
    public MatchOther processPage(Page page) {
        log.info("[列表页解析] - [page] = {}", page.getUrl());
        //detail 1 - 京东自营店铺
        String detailSourceRegion1 ="//div[@class='jScrollWrap']/ul/li/@data-href";  //li/div[@class='gl-i-wrap']/div[@class='p-img']/a
        //detail 2 - 京东搜索详情
        String detailSourceRegion2 ="//div[@class='gl-i-wrap']/div[@class='p-img']/a/@href";//div[@class='gl-i-wrap']/div[@class='p-img']/a/@href
        log.info("[获取详细地址] - [detailUrl] = {}", detailUrl);
        extractLinks(page, detailSourceRegion1, Pattern.compile("(" + detailUrl.replace(".", "\\.") + ")"));
        extractLinks(page, detailSourceRegion2, Pattern.compile("(" + detailUrl.replace(".", "\\.") + ")"));
        // TODO: 2016/12/15 no more pages now
       /*  log.info("[获取列表地址] - [detailUrl] = {}", listUrl);
         extractListUrls(page,"", Pattern.compile("(" + listUrl.replace(".", "\\.") + ")"));*/

        page.getResultItems().setSkip(true);
        return null;
    }

    @Override
    public boolean match(Request page) {
        return new PlainText(page.getUrl()).regex(listUrl).match();
    }
}