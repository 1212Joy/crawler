package com.example.spider.jd.process;


import com.example.spider.kaolao.process.BaseProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zhuzhihang on 2016/12/15.
 */
@Slf4j
public class JingDongProcessor extends BaseProcessor {
    String detailUrl = "(http|ftp|https)://item.jd.com/\\w+.html";
}
