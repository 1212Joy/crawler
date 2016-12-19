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

    @Override
    public MatchOther processPage(Page page) {
        // String detailUrl = "http://item.jd.com/\\w+.html";
        String detailSourceRegion = "//div[@class='jSearchList']/div/ul/li/div/div[@class='jGoodsInfo']/div[@class='jDesc']";
        log.info("[列表页解析] - [page] = {}", page.getUrl());
        log.info("[获取详细地址] - [detailUrl] = {}", detailUrl);
        extractLinks(page, new XpathSelector(detailSourceRegion), Pattern.compile("(" + detailUrl.replace(".", "\\.") + ")"));
        // TODO: 2016/12/15 no more pages now
        // log.info("[获取列表地址] - [detailUrl] = {}", listUrl);
        // extractLinks(page, new XpathSelector(listSourceRegion), Pattern.compile("(" + listUrl.replace(".", "\\.") + ")"));

        page.getResultItems().setSkip(true);
        return null;
    }

    @Override
    public boolean match(Request page) {
        String listUrl = "http://mall.jd.hk/advance_search-610652-1000014861-1000014861-0-0-0-1-1-60.html[^\"'#]*";
        return new PlainText(page.getUrl()).regex(listUrl).match();
    }
}