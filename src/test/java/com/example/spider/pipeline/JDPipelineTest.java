package com.example.spider.pipeline;


import com.example.spider.DefaultSpider;
import com.example.spider.jd.helper.MilkDetailsHelper;
import com.example.spider.jd.helper.PGDetailsHelper;
import com.example.spider.jd.pipeline.JDPipeline;
import com.example.spider.jd.process.JingDongDetailSubProcessor;
import com.example.spider.jd.process.JingDongListSubProcessor;
import com.example.spider.request.Downloader;
import com.example.spider.request.ProxyConfig;
import org.junit.Test;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.handler.CompositePageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhaiJiaYi on 2016/12/15.
 */
public class JDPipelineTest {

    @Test
    public void testProcess() throws Exception {

    }

    @Test
    public void JDPGProcess() {
        DefaultSpider spider = DefaultSpider.create();

        String startListUrl = "http://mall.jd.hk/advance_search-610652-1000014861-1000014861-0-0-0-1-1-60.html";
        String listUrlPattern = startListUrl + "[^\"'#]*";
        spider.addSubPageProcessor(new JingDongDetailSubProcessor(new Downloader(ProxyConfig.build()), PGDetailsHelper.class));
        spider.addSubPageProcessor(new JingDongListSubProcessor(listUrlPattern));

        spider.addPipeline(new JDPipeline());
        spider.addUrl(startListUrl);

        spider.setDownloader(new Downloader(ProxyConfig.build()));

        spider.start();
    }

    @Test
    public void JDMilkProcess() {
        DefaultSpider spider = DefaultSpider.create();
        List<String> urlList = new ArrayList<>();
        //HiPP喜宝官方旗舰店
        urlList.add("http://mall.jd.com/advance_search-75128-31474-30356-0-0-0-1-1-60.html?keyword=%25E5%25A5%25B6%25E7%25B2%2589");
        //合生元官方旗舰店
        urlList.add("http://mall.jd.com/advance_search-405883-1000002672-1000002672-0-0-0-1-1-60.html?keyword=%25E5%25A5%25B6%25E7%25B2%2589");
        //美赞臣官方旗舰店
        urlList.add("http://mall.jd.com/advance_search-418713-1000003179-1000003179-0-0-0-1-1-60.html?keyword=%25E5%25A5%25B6%25E7%25B2%2589");
        //诺优能官方旗舰店
        urlList.add("http://mall.jd.com/advance_search-405881-1000002688-1000002688-0-0-0-1-1-60.html?keyword=%25E5%25A5%25B6%25E7%25B2%2589");
        //雀巢母婴官方旗舰店
        urlList.add("http://mall.jd.com/advance_search-418341-1000003112-1000003112-0-0-0-1-1-60.html?keyword=%25E5%25A5%25B6%25E7%25B2%2589");
        //全球购母婴自营旗舰店
        urlList.add("http://mall.jd.hk/advance_search-618086-1000010824-1000010824-0-0-0-1-1-60.html?keyword=%25E5%25A5%25B6%25E7%25B2%2589");
        //惠氏海外官方自营旗舰店
        urlList.add("http://mall.jd.hk/advance_search-638332-1000076286-1000076286-0-0-0-1-1-60.html?keyword=%25E5%25A5%25B6%25E7%25B2%2589");

        String listUrlPattern = ".*/advance_search-.*";

        spider.addSubPageProcessor(new JingDongDetailSubProcessor(new Downloader(ProxyConfig.build()), MilkDetailsHelper.class));
        spider.addSubPageProcessor(new JingDongListSubProcessor(listUrlPattern));

        spider.addPipeline(new JDPipeline());
        urlList.forEach(spider::addUrl);

        spider.setDownloader(new Downloader(ProxyConfig.build()));

        spider.start();

    }
}