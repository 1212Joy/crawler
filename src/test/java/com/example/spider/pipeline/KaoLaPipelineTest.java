package com.example.spider.pipeline;


import com.alibaba.fastjson.JSON;
import com.example.configuration.ApplicationConstants;
import com.example.spider.process.KaoLaDetailSubProcessor;
import com.example.spider.process.KaoLaListSubProcessor;
import com.example.spider.process.KaoLaProcessor;
import com.example.vo.CrawlerProps;
import com.example.vo.KaoLaDetail;
import org.junit.Test;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.handler.CompositePageProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhaiJiaYi on 2016/12/15.
 */
public class KaoLaPipelineTest {

    @Test
    public void testProcess() throws Exception {

    }

    @Test
    public void KaoLaMilkProcess() {
        List<KaoLaDetail> milkList = new ArrayList<>();
        Site site = Site.me().setSleepTime(1000);
        // site.setHttpProxy(?)

        String startListUrl = "http://www.kaola.com/category/2620.html";
        String listUrlPattern = startListUrl + "[^\"'#]*";

        CompositePageProcessor compositePageProcessor = new CompositePageProcessor(site);
        compositePageProcessor.addSubPageProcessor(new KaoLaDetailSubProcessor(KaoLaProcessor.JS_SCRIPT_MILK));
        compositePageProcessor.addSubPageProcessor(new KaoLaListSubProcessor(listUrlPattern));

        Spider spider = Spider.create(compositePageProcessor);
        spider.addPipeline(new KaoLaPipeline(ApplicationConstants.Category.Milk,milkList));

        spider.addUrl(startListUrl);
        spider.thread(5).run();
       /* JSONArray jsonObject = JSONArray.fromObject(milkList);
        System.out.println("【-------------start-------------长度】"+milkList.size());
        System.out.println(jsonObject.toString());
        System.out.println("【-------------end-------------】");*/

        insertDB(milkList);
    }

    @Test
    public void KaoLaJohnsonProcess() {
        List<KaoLaDetail> johnsonList = new ArrayList<>();
        Site site = Site.me().setSleepTime(1000);
        // site.setHttpProxy(?)

        String startListUrl = "http://www.kaola.com/brand/1208-5119.html";
        String listUrlPattern = startListUrl + "[^\"'#]*";

        CompositePageProcessor compositePageProcessor = new CompositePageProcessor(site);
        compositePageProcessor.addSubPageProcessor(new KaoLaDetailSubProcessor(KaoLaProcessor.JS_SCRIPT_JOHNSON));
        compositePageProcessor.addSubPageProcessor(new KaoLaListSubProcessor(listUrlPattern));

        Spider spider = Spider.create(compositePageProcessor);
        spider.addPipeline(new KaoLaPipeline(ApplicationConstants.Category.JOHNSON,johnsonList));

        spider.addUrl(startListUrl);
        spider.thread(5).run();

     /*   JSONArray jsonObject = JSONArray.fromObject(johnsonList);
        System.out.println("【-------------start-------------长度】"+johnsonList.size());
        System.out.println(jsonObject.toString());
        System.out.println("【-------------end-------------】");*/

        insertDB(johnsonList);
    }
    @Test
    public void KaoLaPGProcess() {
        List<KaoLaDetail> johnsonList = new ArrayList<>();
        Site site = Site.me().setSleepTime(1000);
        // site.setHttpProxy(?)

        String startListUrl = "http://www.kaola.com/brand/1146.html";
        String listUrlPattern = startListUrl + "[^\"'#]*";

        CompositePageProcessor compositePageProcessor = new CompositePageProcessor(site);
        compositePageProcessor.addSubPageProcessor(new KaoLaDetailSubProcessor(KaoLaProcessor.JS_SCRIPT_PG));
        compositePageProcessor.addSubPageProcessor(new KaoLaListSubProcessor(listUrlPattern));

        Spider spider = Spider.create(compositePageProcessor);
        spider.addPipeline(new KaoLaPipeline(ApplicationConstants.Category.DYNAMIC,johnsonList));

        spider.addUrl(startListUrl);
        spider.thread(5).run();

        String jsonObjectStr = JSON.toJSONString(johnsonList);
        System.out.println("【-------------start-------------长度】"+johnsonList.size());
        System.out.println(jsonObjectStr);
        System.out.println("【-------------end-------------】");

      //  insertDB(johnsonList);
    }

    private void insertDB(List<KaoLaDetail> crawlerPropss) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://192.168.0.221:3306/glp?useUnicode=true&characterEncoding=utf-8",
                    "simba", "BeGYVXnw7&Kj@A!j");
            for (CrawlerProps crawlerProps:crawlerPropss) {
                PreparedStatement preparedStatementInsert = connection.prepareStatement("INSERT INTO glp.crawler_props (`platform_Id`, `url`, `title`, `price`, `sku`, `brand`, `category`, `shopName`, `shopType`, `weight`, `sales`, `origin`,`sourceArea`) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                preparedStatementInsert.setString(1, crawlerProps.getPlatformId());
                preparedStatementInsert.setString(2, crawlerProps.getUrl());
                preparedStatementInsert.setString(3, crawlerProps.getTitle());
                preparedStatementInsert.setString(4, crawlerProps.getPrice());
                preparedStatementInsert.setString(5, crawlerProps.getSku());
                preparedStatementInsert.setString(6, crawlerProps.getBrand());
                preparedStatementInsert.setString(7, crawlerProps.getCategory());
                preparedStatementInsert.setString(8, crawlerProps.getShopName());
                preparedStatementInsert.setString(9, crawlerProps.getShopType());
                preparedStatementInsert.setString(10, crawlerProps.getWeight());
                preparedStatementInsert.setString(11, crawlerProps.getSales());
                preparedStatementInsert.setString(12, crawlerProps.getOrigin());
                preparedStatementInsert.setString(13, crawlerProps.getSourceArea());
                preparedStatementInsert.execute();
            }

            System.out.print(crawlerPropss);
        } catch (Exception e) {

        }
    }

    @Test
    public  void readTxtFile(){
        String filePath = "C:\\Users\\ZhaiJiaYi\\Desktop\\Tasks\\普洛斯\\kaola-data.log";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://192.168.0.221:3306/glp?useUnicode=true&characterEncoding=utf-8",
                    "simba", "BeGYVXnw7&Kj@A!j");

            String encoding="utf-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = "";
                String kaoLaDetailStr = "";
                while((lineTxt = bufferedReader.readLine()) != null){
                    boolean b=lineTxt.startsWith("-- [zzh] -- [kaola result] = ");
                    if(b){
                        kaoLaDetailStr = lineTxt.split("=")[1];
                        KaoLaDetail kl = JSON.parseObject(kaoLaDetailStr, KaoLaDetail.class);
                        kl.setPlatformId(kl.getPlatformId().split("-")[0]);
                        PreparedStatement preparedStatementInsert = connection.prepareStatement("update glp.crawler_props set price=? where platform_Id=?");
                        preparedStatementInsert.setString(1,kl.getPrice());
                        preparedStatementInsert.setString(2,kl.getPlatformId());
                        preparedStatementInsert.executeUpdate();
                    }
                    System.out.println(kaoLaDetailStr);
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }

}