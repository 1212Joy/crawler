package com.example;

import com.alibaba.fastjson.JSON;
import com.example.vo.Crawler;
import org.junit.Test;

import java.util.Date;

/**
 * Created by dongchangkun on 2016-12-12.
 */
public class SimpleTest {

    @Test
    public void fastjsonTest() {
        Crawler crawler = new Crawler();
        crawler.setOriginKey("555");
        crawler.setSales("666");
        crawler.setCrawledAt(new Date());
         String jsonString = JSON.toJSONString(crawler);
        System.out.println("[zzj - print] : "+ jsonString);
        Crawler crawlerTrans = JSON.parseObject(jsonString, Crawler.class);
        System.out.println("[zzj - print] : "+ crawlerTrans.getCrawledAt());

    }
}
