package com.example.spider.process;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.handler.SubPageProcessor;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.XpathSelector;

import java.util.regex.Pattern;

/**
 * Created by zhuzhihang on 2016/12/13.
 */
@Slf4j
public class KaoLaListSubProcessor extends KaoLaProcessor implements SubPageProcessor {

    private String listUrl;

    public KaoLaListSubProcessor(String listUrlPattern) {
        this.listUrl = listUrlPattern;
    }

    @Override
    public MatchOther processPage(Page page) {
        log.info("[列表页解析] - [page] = {}", page.getUrl());
        log.info("[获取详细地址] - [detailUrl] = {}", detailUrl);
        extractLinks(page, new XpathSelector(detailSourceRegion), Pattern.compile("(" + detailUrl.replace(".", "\\.") + ")"));
        log.info("[获取列表地址] - [detailUrl] = {}", listUrl);
        extractLinks(page, new XpathSelector(listSourceRegion), Pattern.compile("(" + listUrl.replace(".", "\\.") + ")"));

        return null;

    }

    @Override
    public boolean match(Request page) {
        return new PlainText(page.getUrl()).regex(listUrl).match();
    }


}
