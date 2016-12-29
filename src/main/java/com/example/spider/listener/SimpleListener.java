package com.example.spider.listener;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.SpiderListener;

/**
 * Created by zhuzhihang on 2016/12/26.
 */
@Slf4j
public class SimpleListener implements SpiderListener {
    @Override
    public void onSuccess(Request request) {

    }

    @Override
    public void onError(Request request) {
        log.info("[error url] - [request] = {}", request.getUrl());
    }
}