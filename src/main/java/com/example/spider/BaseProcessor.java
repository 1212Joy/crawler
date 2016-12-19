package com.example.spider;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Selector;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhuzhihang on 2016/12/13.
 */
@Slf4j
public class BaseProcessor {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public void extractLinks(Page page, Selector urlRegionSelector, Pattern urlPattern) {
        List<String> links;
        if (urlRegionSelector == null) {
            links = page.getHtml().links().all();
        } else {
            links = page.getHtml().selectList(urlRegionSelector).links().all();
        }
        for (String link : links) {
            Matcher matcher = urlPattern.matcher(link);
            if (matcher.find()) {
                Request request = new Request(matcher.group(1));
                page.addTargetRequest(request);
                log.info("[new request] - [link] = {}", link);
            }

        }
    }
}
