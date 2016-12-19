package com.example.spider.pipeline;


import com.example.spider.jd.pipeline.JDPipeline;
import com.example.spider.jd.process.JingDongDetailSubProcessor;
import com.example.spider.jd.process.JingDongListSubProcessor;
import com.example.spider.request.Downloader;
import com.example.spider.request.ProxyConfig;
import org.junit.Test;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.handler.CompositePageProcessor;

/**
 * Created by ZhaiJiaYi on 2016/12/15.
 */
public class JDPipelineTest {

    @Test
    public void testProcess() throws Exception {

    }

    @Test
    public void JDPGProcess() {
        Site site = Site.me().setSleepTime(1000);
        String startListUrl = "http://mall.jd.hk/advance_search-610652-1000014861-1000014861-0-0-0-1-1-60.html";

        Downloader downloader = new Downloader(ProxyConfig.build());
        CompositePageProcessor compositePageProcessor = new CompositePageProcessor(site);
        compositePageProcessor.addSubPageProcessor(new JingDongDetailSubProcessor(downloader));
        compositePageProcessor.addSubPageProcessor(new JingDongListSubProcessor());

        Spider spider = Spider.create(compositePageProcessor);
        spider.addPipeline(new JDPipeline());
        // spider.setDownloader(new Downloader(ProxyConfig.build()););

        spider.addUrl(startListUrl);
        spider.thread(5).run();
    }
}