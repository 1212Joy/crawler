package com.example.spider;


import com.example.spider.listener.SimpleListener;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.handler.CompositePageProcessor;
import us.codecraft.webmagic.handler.SubPageProcessor;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuzhihang on 2016/12/26.
 */
public class DefaultSpider extends Spider {

    public DefaultSpider(PageProcessor pageProcessor) {
        super(pageProcessor);
    }

    public static DefaultSpider create() {
        Site site = Site.me().setSleepTime(1000).setCycleRetryTimes(1000);
        CompositePageProcessor compositePageProcessor = new CompositePageProcessor(site);
        DefaultSpider defaultSpider = new DefaultSpider(compositePageProcessor);
        defaultSpider.thread(8);
        defaultSpider.addSpiderListener(new SimpleListener());

        return defaultSpider;
    }

    public void addSubPageProcessor(SubPageProcessor subPageProcessor) {
        ((CompositePageProcessor) pageProcessor).addSubPageProcessor(subPageProcessor);
    }

    public void addSpiderListener(SpiderListener spiderListener) {
        List<SpiderListener> spiderListeners = this.getSpiderListeners();
        if (spiderListeners == null) {
            spiderListeners = new ArrayList<>();
            this.setSpiderListeners(spiderListeners);
        }
        spiderListeners.add(spiderListener);
    }
}
